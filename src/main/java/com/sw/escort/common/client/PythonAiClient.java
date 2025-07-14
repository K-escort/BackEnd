package com.sw.escort.common.client;

import com.sw.escort.chat.dto.req.ChatStartReq;
import com.sw.escort.chat.dto.res.ChatResponse;
import com.sw.escort.daily.dto.res.DailyDtoRes;
import com.sw.escort.media.entity.UserInfoPhoto;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PythonAiClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://ai:5000")//http://localhost:5000
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public ChatResponse.ChatDetail sendChatToPython(User user, UserInfo info, ChatStartReq req) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", String.valueOf(user.getId()));
        payload.put("topic", req.getTopic());

        String prompt = req.getUserPrompt(); //null 이면 대화 시작으로 인식
        if (prompt != null && !prompt.trim().isEmpty()) {
            payload.put("user_prompt", prompt);
        }

        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("age", String.valueOf(info.getAge()));
        userInfoMap.put("gender", info.getGender()); // MALE, FEMALE
        userInfoMap.put("cognitiveStatus", info.getCognitiveStatus().name());
        userInfoMap.put("hometown", info.getHometown());
        userInfoMap.put("lifeHistory", info.getLifeHistory());
        userInfoMap.put("familyInfo", info.getFamilyInfo());
        userInfoMap.put("education", info.getEducation());
        userInfoMap.put("occupation", info.getOccupation());
        userInfoMap.put("forbiddenKeywords", info.getForbiddenKeywords());
        userInfoMap.put("lifetimeline", info.getLifetimeline());

        payload.put("user_info", userInfoMap);

        return webClient.post() //HTTP POST 요청 시작
                .uri("/ai/chat")// Python 서버의 상태 URI 설정
                .bodyValue(payload)// 요청 본문(body)에 payload(Map<String, object>) 설정
                .retrieve() //응답을 가져오겠다는 의미(send요청)
                .bodyToMono(ChatResponse.class)
                //.toBodilessEntity() // 응답 본문은 무시하고 상태 코드, 헤더만 받음 (ResponseEntity<Void>)
                .block() //요청을 **동기 처리** (응답이 올 때까지 대기)
                .getResponse(); // 내부 message, evaluation, topic 등만 반환
    }

    public byte[] generateVideo(String prompt, MultipartFile inputImage) {
        // 멀티파트 데이터 생성
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("prompt", prompt);
        builder.part("input_image", inputImage.getResource());

        return webClient.post()
                .uri("/ai/generate-video")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    public DailyDtoRes.ConversationRes fetchAiConversation(Long userId, LocalDate localDate) {
        // WebClient를 사용하여 FastAPI 엔드포인트 호출
        return webClient.get() // GET 요청으로 변경 (FastAPI 코드가 @app.get 사용)
                .uri(uriBuilder -> uriBuilder
                        .path("/ai/conversation-by-date")
                        .queryParam("user_id", userId.toString())
                        .queryParam("date", localDate.toString())
                        .build())
                .retrieve()
                .bodyToMono(DailyDtoRes.ConversationRes.class) // 응답을 String으로 받음
                .block(); // 동기적으로 결과 반환 (필요에 따라 비동기 처리 가능)
    }


    public List<MultipartFile> requestImageGeneration(User user, List<UserInfoPhoto> photos) {
        List<Map<String, Object>> photoList = photos.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("description", p.getDescription());
            map.put("relation_to_patient", p.getRelationToPatient());
            map.put("url", p.getUrl());
            return map;
        }).toList();

        Map<String, Object> request = new HashMap<>();
        request.put("user_id", String.valueOf(user.getId()));
        request.put("photos", photoList);

        // Python 서버 호출 → 이미지 파일 목록(Multipart) 응답
        return webClient.post()
                .uri("/ai/generate-recall-video-frames")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(MultipartFile.class)
                .collectList()
                .block();
    }
}


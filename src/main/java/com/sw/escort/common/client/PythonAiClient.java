package com.sw.escort.common.client;

import com.sw.escort.chat.dto.res.ChatResponse;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PythonAiClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://ai:5000")//http://localhost:5000
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public ChatResponse.ChatDetail sendChatToPython(User user, UserInfo info, String userPrompt) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", String.valueOf(user.getId()));
        payload.put("user_prompt", userPrompt); //null 이면 대화 시작으로 인식

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
}


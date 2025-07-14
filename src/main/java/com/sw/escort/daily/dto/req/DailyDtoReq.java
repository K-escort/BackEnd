package com.sw.escort.daily.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class DailyDtoReq {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordDailyReq {
        private String conversation;
        private String feedback;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate dailyDayRecording;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyImageGenerationReq {
        private Long dailyId;
        private List<Long> photoIds;
    }
}

package com.sw.escort.media.dto.res;

import com.sw.escort.media.entity.UserInfoPhoto;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

public class UserInfoPhotoDtoRes {

    @Data
    @Builder
    public static class Detail {
        private Long id;
        private String description;
        private String relationToPatient;
        private String originalFileName;
        private String fileSizeMb;
        private UUID uuid;
        private String url;
        private String lastModifiedBy;

        public static Detail from(UserInfoPhoto photo) {
            return Detail.builder()
                    .id(photo.getId())
                    .description(photo.getDescription())
                    .relationToPatient(photo.getRelationToPatient())
                    .originalFileName(photo.getOriginalFileName())
                    .fileSizeMb(formatBytesToMb(photo.getFileSize()))
                    .uuid(photo.getUuid())
                    .url(photo.getUrl())
                    .lastModifiedBy(photo.getLastModifiedBy())
                    .build();
        }
        private static String formatBytesToMb(Long bytes) {
            if (bytes == null) return "0 MB";
            double mb = bytes / (1024.0 * 1024.0);
            return String.format("%.2f MB", mb);
        }
    }
}


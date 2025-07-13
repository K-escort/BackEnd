package com.sw.escort.media.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PublicDataPhotoDtoReq {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicDataPhotoUrlReq {
        private List<String> imageList;
    }

}

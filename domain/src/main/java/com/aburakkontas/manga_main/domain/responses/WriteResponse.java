package com.aburakkontas.manga_main.domain.responses;

import lombok.Data;

import java.util.ArrayList;

@Data
public class WriteResponse {
    private Boolean debug;
    private String last_image;
    private ArrayList<WriteResult> results;

    @Data
    public static class WriteResult {
        private String color;
        private Integer fontSize;
        private String text;
    }
}

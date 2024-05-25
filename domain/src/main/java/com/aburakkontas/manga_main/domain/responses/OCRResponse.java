package com.aburakkontas.manga_main.domain.responses;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OCRResponse {
    private ArrayList<ArrayList<OCRData>> ocrData;

    @Data
    public static class OCRData {
        private String text;
        private String image;
        private Double confidence;
    }
}

package com.aburakkontas.manga_main.domain.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OCRResponse {
    private ArrayList<ArrayList<OCRData>> ocrData;

    @Data
    public static class OCRData {
        private String text;
        private String image;
        private Double confidence;
    }
}

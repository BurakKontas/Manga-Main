package com.aburakkontas.manga_main.domain.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetectronResponse {
    private Instance instances;
    private String originalImage;
    private String outputImage;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Instance {
        private ArrayList<ArrayList<Double>> predBoxes;
        private ArrayList<Integer> predClasses;
        private ArrayList<Double> scores;
    }
}
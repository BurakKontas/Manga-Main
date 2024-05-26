package com.aburakkontas.manga_main.domain.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WriteResponse {
    private Boolean debug;
    private String lastImage;
    private ArrayList<WriteResult> results;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class WriteResult {
        private String color;
        private Integer fontSize;
        private String text;
    }
}

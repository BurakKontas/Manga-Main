package com.aburakkontas.manga_main.domain.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MADFResponse {
    private String gt;
    private String mask;
    private String output;
    private String outputComp;
    private String outputImage;
}

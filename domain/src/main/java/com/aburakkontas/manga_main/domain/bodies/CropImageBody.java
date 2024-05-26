package com.aburakkontas.manga_main.domain.bodies;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CropImageBody {
    private ArrayList<ArrayList<Double>> predBoxes;
    private String originalImage;
}

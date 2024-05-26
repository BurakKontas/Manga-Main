package com.aburakkontas.manga_main.domain.bodies;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OCRBody {
    private ArrayList<ArrayList<Integer>> polyTxt;
    private ArrayList<ArrayList<ArrayList<Double>>> polys;
    private ArrayList<ArrayList<ArrayList<Double>>> boxes;
    private String image;
    private int index;
    private String originalImage;
}

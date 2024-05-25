package com.aburakkontas.manga_main.domain.bodies;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CropImageBody {
    private ArrayList<ArrayList<Double>> predBoxes;
    private String originalImage;
}

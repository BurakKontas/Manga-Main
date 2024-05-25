package com.aburakkontas.manga_main.domain.bodies;

import lombok.Data;
import java.util.ArrayList;

@Data
public class GenerateMaskBody {
    private ArrayList<ArrayList<Double>> boxes;
    private String image;
}
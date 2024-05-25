package com.aburakkontas.manga_main.domain.bodies;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OCRBody {
    private ArrayList<ArrayList<Integer>> polyTxt;
    private ArrayList<ArrayList<ArrayList<Double>>> polys;
    private ArrayList<ArrayList<ArrayList<Double>>> boxes;
    private String image;
    private int index;
    private String originalImage;
}

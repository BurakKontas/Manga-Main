package com.aburakkontas.manga_main.domain.responses;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CropImageResponse {
    private ArrayList<Double> bbox;
    private String image;
}

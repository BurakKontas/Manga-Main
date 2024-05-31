package com.aburakkontas.manga_main.contracts.request;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OCRRestRequest {
    private ArrayList<ArrayList<Double>> predBoxes;
}

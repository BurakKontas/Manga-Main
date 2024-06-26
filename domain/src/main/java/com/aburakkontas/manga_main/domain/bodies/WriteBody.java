package com.aburakkontas.manga_main.domain.bodies;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
public class WriteBody {
    private Boolean debug;
    private ArrayList<WriteData> data;
    private String image;

    @Data
    @AllArgsConstructor
    public static class WriteData {
        private ArrayList<Double> bbox;
        private String text;
        private String color;
    }
}

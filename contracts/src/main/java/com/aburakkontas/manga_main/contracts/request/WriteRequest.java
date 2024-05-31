package com.aburakkontas.manga_main.contracts.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
public class WriteRequest {
    private ArrayList<WriteData> data;

    @Data
    @AllArgsConstructor
    public static class WriteData {
        private ArrayList<Double> bbox;
        private String text;
        private String color;
    }
}

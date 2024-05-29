package com.aburakkontas.manga_main.contracts.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class CleanResponse {
    private ArrayList<ArrayList<Double>> predBoxes;
    private byte[] gt;
    private byte[] mask;
    private byte[] output;
    private byte[] outputComp;
    private byte[] outputImage;
}

package com.aburakkontas.manga_main.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum ModelPricing {
    CLEAN(5.0),
    OCR(3.0),
    WRITE(2.0);


    private final Double price;

    ModelPricing(Double price) {
        this.price = price;
    }
}

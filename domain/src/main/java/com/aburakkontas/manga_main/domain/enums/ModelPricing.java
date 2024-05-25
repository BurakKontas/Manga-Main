package com.aburakkontas.manga_main.domain.enums;

import lombok.Getter;

@Getter
public enum ModelPricing {
    DETECTRON(1.5),
    GENERATE_MASK(0.25),
    MADF(5.0),
    META(1.5),
    CROP_IMAGE(0.25),
    OCR(7.5),
    TRANSLATE(1.0),
    WRITE(3.5);


    private final Double price;

    ModelPricing(Double price) {
        this.price = price;
    }
}

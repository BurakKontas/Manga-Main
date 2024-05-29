package com.aburakkontas.manga_main.contracts.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ModelPricingResponse {
    private Map<String, Double> prices;
}

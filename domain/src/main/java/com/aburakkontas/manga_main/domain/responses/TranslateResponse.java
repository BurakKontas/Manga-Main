package com.aburakkontas.manga_main.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranslateResponse {
    private String target;
    private String source;
    private String text;
    private String result;
}

package com.aburakkontas.manga_main.domain.responses;

import lombok.Data;

@Data
public class TranslateResponse {
    private String target;
    private String source;
    private String text;
    private String result;
}

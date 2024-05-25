package com.aburakkontas.manga_main.domain.bodies;

import lombok.Data;

@Data
public class TranslateBody {
    private String target;
    private String source;
    private String text;
}

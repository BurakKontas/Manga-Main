package com.aburakkontas.manga_main.domain;

import lombok.Getter;

@Getter
public class ExceptionWithErrorCode extends RuntimeException {
    private final int code;

    public ExceptionWithErrorCode(String message, int code) {
        super(message);
        this.code = code;
    }
}
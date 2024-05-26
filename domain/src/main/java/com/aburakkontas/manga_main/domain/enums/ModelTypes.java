package com.aburakkontas.manga_main.domain.enums;

import com.aburakkontas.manga_main.domain.bodies.*;
import com.aburakkontas.manga_main.domain.responses.*;
import lombok.Getter;

@Getter
public enum ModelTypes {
    DETECTRON(byte[].class, DetectronResponse.class),
    GENERATE_MASK(GenerateMaskBody.class, GenerateMaskResponse.class),
    MADF(MADFBody.class, MADFResponse.class),
    META(MetaBody.class, MetaResponse.class),
    CROP_IMAGE(CropImageBody.class, CropImageResponse.class),
    OCR(OCRBody.class, OCRResponse.class),
    TRANSLATE(TranslateBody.class, TranslateResponse.class),
    WRITE(WriteBody.class, WriteResponse.class),
    ALL(byte[].class, WriteResponse.class);

    private final Class<?> inputType;
    private final Class<?> resultType;

    ModelTypes(Class<?> inputType, Class<?> resultType) {
        this.inputType = inputType;
        this.resultType = resultType;
    }
}

package com.aburakkontas.manga_main.domain.repositories;

import com.aburakkontas.manga_main.domain.bodies.*;
import com.aburakkontas.manga_main.domain.responses.*;

import java.io.IOException;

public interface ModelRepository {
    DetectronResponse detectron(byte[] file);

    GenerateMaskResponse generateMask(GenerateMaskBody request);

    MADFResponse madf(MADFBody request);

    MetaResponse metaCraft(MetaBody request);

    CropImageResponse cropImage(CropImageBody request);

    OCRResponse ocr(OCRBody request);

    TranslateResponse translate(TranslateBody request);

    WriteResponse write(WriteBody request);
}

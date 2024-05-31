package com.aburakkontas.manga_main.domain.repositories;

import com.aburakkontas.manga_main.domain.bodies.*;
import com.aburakkontas.manga_main.domain.responses.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public interface ModelRepository {

    Map<String, Double> getModelPrices();

    DetectronResponse detectron(byte[] file);

    GenerateMaskResponse generateMask(GenerateMaskBody request);

    MADFResponse madf(MADFBody request);

    ArrayList<CropImageResponse> cropImage(CropImageBody request);

    ArrayList<MetaResponse> metaCraft(MetaBody request);

    ArrayList<OCRResponse> ocr(ArrayList<OCRBody> request);

    TranslateResponse translate(TranslateBody request);

    WriteResponse write(WriteBody request);
}

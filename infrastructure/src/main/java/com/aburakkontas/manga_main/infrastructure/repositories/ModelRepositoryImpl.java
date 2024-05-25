package com.aburakkontas.manga_main.infrastructure.repositories;

import com.aburakkontas.manga_main.domain.bodies.*;
import com.aburakkontas.manga_main.domain.responses.*;
import com.aburakkontas.manga_main.domain.repositories.ModelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import java.util.LinkedHashMap;

@Service
public class ModelRepositoryImpl implements ModelRepository {

    @Value("${model.baseurl}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public ModelRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public DetectronResponse detectron(byte[] file) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource fileResource = new ByteArrayResource(file) {
            @Override
            public String getFilename() {
                return "file";
            }
        };
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        var data = restTemplate.postForObject(baseUrl + "/detectron", requestEntity, DetectronResponse.class);

        return data;
    }

    @Override
    public GenerateMaskResponse generateMask(GenerateMaskBody request) {
        return execute("/generate_mask", request, GenerateMaskResponse.class);
    }

    @Override
    public MADFResponse madf(MADFBody request) {
        return execute("/madf", request, MADFResponse.class);
    }

    @Override
    public MetaResponse metaCraft(MetaBody request) {
        return execute("/meta_craft", request, MetaResponse.class);
    }

    @Override
    public CropImageResponse cropImage(CropImageBody request) {
        return execute("/crop_image", request, CropImageResponse.class);
    }

    @Override
    public OCRResponse ocr(OCRBody request) {
        return execute("/ocr", request, OCRResponse.class);
    }

    @Override
    public TranslateResponse translate(TranslateBody request) {
        return execute("/translate", request, TranslateResponse.class);
    }

    @Override
    public WriteResponse write(WriteBody request) {
        return execute("/write", request, WriteResponse.class);
    }

    public <T> T execute(String url, Object body, Class<T> responseType) {
        return restTemplate.postForObject(baseUrl + url, body, responseType);
    }
}

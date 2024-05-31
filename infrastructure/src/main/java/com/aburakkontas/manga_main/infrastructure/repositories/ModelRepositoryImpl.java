package com.aburakkontas.manga_main.infrastructure.repositories;

import com.aburakkontas.manga_main.domain.bodies.*;
import com.aburakkontas.manga_main.domain.enums.ModelPricing;
import com.aburakkontas.manga_main.domain.responses.*;
import com.aburakkontas.manga_main.domain.repositories.ModelRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ModelRepositoryImpl implements ModelRepository {

    @Value("${model.baseurl}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public ModelRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, Double> getModelPrices() {
        return Arrays.stream(ModelPricing.values())
                .collect(Collectors.toMap(Enum::name, ModelPricing::getPrice));
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

    @SneakyThrows
    @Override
    public ArrayList<MetaResponse> metaCraft(MetaBody request) {
        ArrayList<LinkedHashMap<?, ?>> data = execute("/meta_craft", request, ArrayList.class);
        return data.stream().map(map -> mapToResponse(map, MetaResponse.class)).collect(Collectors.toCollection(ArrayList::new));
    }

    @SneakyThrows
    @Override
    public ArrayList<CropImageResponse> cropImage(CropImageBody request) {
        ArrayList<LinkedHashMap<?, ?>> data = execute("/crop_image", request, ArrayList.class);
        return data.stream().map(map -> mapToResponse(map, CropImageResponse.class)).collect(Collectors.toCollection(ArrayList::new));
    }

    @SneakyThrows
    @Override
    public ArrayList<OCRResponse> ocr(ArrayList<OCRBody> request) {
        ArrayList<ArrayList<LinkedHashMap<?, ?>>> data = execute("/ocr", request, ArrayList.class);
        ObjectMapper mapper = new ObjectMapper();
        var typeReference = new TypeReference<ArrayList<OCRResponse.OCRData>>() {};
        ArrayList<OCRResponse> ocrResponses = new ArrayList<>();
        for (ArrayList<LinkedHashMap<?, ?>> map : data) {
            String jsonData = mapper.writeValueAsString(map);
            ArrayList<OCRResponse.OCRData> ocrData = mapper.readValue(jsonData, typeReference);
            OCRResponse ocrResponse = new OCRResponse();
            ocrResponse.setOcrData(ocrData);
            ocrResponses.add(ocrResponse);
        }
        return ocrResponses;
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

    @SneakyThrows
    private static <T> T mapToResponse(LinkedHashMap<?, ?> map, Class<T> responseType) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(mapper.writeValueAsString(map), responseType);

    }
}

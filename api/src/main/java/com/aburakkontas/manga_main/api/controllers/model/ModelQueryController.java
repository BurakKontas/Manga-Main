package com.aburakkontas.manga_main.api.controllers.model;

import com.aburakkontas.manga.common.main.queries.*;
import com.aburakkontas.manga.common.main.queries.results.*;
import com.aburakkontas.manga_main.contracts.request.OCRRestRequest;
import com.aburakkontas.manga_main.contracts.request.WriteRequest;
import com.aburakkontas.manga_main.contracts.response.CleanResponse;
import com.aburakkontas.manga_main.contracts.response.ModelPricingResponse;
import com.aburakkontas.manga_main.domain.bodies.TranslateBody;
import com.aburakkontas.manga_main.domain.bodies.WriteBody;
import com.aburakkontas.manga_main.domain.primitives.Result;
import com.aburakkontas.manga_main.domain.responses.DetectronResponse;
import com.aburakkontas.manga_main.domain.responses.TranslateResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.SneakyThrows;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/model")
public class ModelQueryController {

    private static final Logger log = LoggerFactory.getLogger(ModelQueryController.class);
    private final QueryGateway queryGateway;
    private final Environment env;

    @Autowired
    public ModelQueryController(QueryGateway queryGateway, Environment env) {
        this.queryGateway = queryGateway;
        this.env = env;
    }

    @GetMapping("/model-pricing")
    public ResponseEntity<Result<ModelPricingResponse>> getModelPricing() {
        var query = new GetModelPricesQuery();

        var result = queryGateway.query(query, GetModelPricesResult.class).join();

        return ResponseEntity.ok(Result.success(new ModelPricingResponse(result.getPrices())));
    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "File to be processed",
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
    )
    @PostMapping("/clean")
    public ResponseEntity<Result<CleanResponse>> clean(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        var userId = UUID.fromString(authentication.getCredentials().toString());
        var query = new CleanQuery(file.getBytes(), file.getContentType(), userId);

        var result = queryGateway.query(query, CleanQueryResult.class).join();

        var response = new CleanResponse(result.getPredBoxes(), result.getGt(), result.getMask(), result.getOutput(), result.getOutputComp(), result.getOutputImage());

        return ResponseEntity.ok(Result.success(response));
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/ocr")
    public ResponseEntity<Result<OCRQueryResult>> findAndTranslate(
            @RequestParam("file") MultipartFile file,
            @RequestParam("predBoxes") String predBoxes,
            Authentication authentication) throws IOException {
        var objectMapper = new ObjectMapper();
        var data = objectMapper.readValue(predBoxes, new TypeReference<ArrayList<ArrayList<Double>>>() {});

        var userId = UUID.fromString(authentication.getCredentials().toString());
        var query = new OCRQuery(data, file.getBytes(), userId);

        var result = queryGateway.query(query, OCRQueryResult.class).join();

        return ResponseEntity.ok(Result.success(result));
    }

    @PostMapping("/translate")
    public ResponseEntity<Result<TranslateResponse>> translate(@RequestBody TranslateBody body, Authentication authentication) {
        var userId = UUID.fromString(authentication.getCredentials().toString());
        var query = new TranslateQuery(body.getTarget(), body.getSource(), body.getText(), userId);

        var result = queryGateway.query(query, TranslateQueryResult.class).join();

        var response = new TranslateResponse(result.getTarget(), result.getSource(), result.getText(), result.getResult());

        return ResponseEntity.ok(Result.success(response));
    }

    @SneakyThrows
    @PostMapping("/write")
    public ResponseEntity<Result<WriteQueryResult>> write(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String data,
            Authentication authentication) {
        var objectMapper = new ObjectMapper();
        var type = new TypeReference<ArrayList<WriteQuery.WriteData>>() {};
        var writeData = objectMapper.readValue(data, type);

        var userId = UUID.fromString(authentication.getCredentials().toString());
        var query = new WriteQuery(false, writeData, file.getBytes(), userId);

        var result = queryGateway.query(query, WriteQueryResult.class).join();

        return ResponseEntity.ok(Result.success(result));
    }

    
}

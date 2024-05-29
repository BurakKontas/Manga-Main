package com.aburakkontas.manga_main.api.controllers.model;

import com.aburakkontas.manga.common.main.queries.CleanQuery;
import com.aburakkontas.manga.common.main.queries.GetModelPricesQuery;
import com.aburakkontas.manga.common.main.queries.results.CleanQueryResult;
import com.aburakkontas.manga.common.main.queries.results.GetModelPricesResult;
import com.aburakkontas.manga_main.contracts.response.CleanResponse;
import com.aburakkontas.manga_main.contracts.response.ModelPricingResponse;
import com.aburakkontas.manga_main.domain.primitives.Result;
import com.aburakkontas.manga_main.domain.responses.DetectronResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/v1/model")
public class ModelQueryController {

    private static final Logger log = LoggerFactory.getLogger(ModelCommandController.class);
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

        var result = queryGateway.query(new GetModelPricesQuery(), GetModelPricesResult.class).join();

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
    public ResponseEntity<Result<CleanResponse>> clean(@RequestParam("file") MultipartFile file) throws IOException {
        var query = new CleanQuery(file.getBytes(), file.getContentType());

        var result = queryGateway.query(new CleanQuery(query.getImage(), query.getFileType()), CleanQueryResult.class).join();

        var response = new CleanResponse(result.getPredBoxes(), result.getGt(), result.getMask(), result.getOutput(), result.getOutputComp(), result.getOutputImage());

        return ResponseEntity.ok(Result.success(response));
    }

    @PostMapping("/find-and-translate")
    public Void findAndTranslate() {
        return null;
    }

    @PostMapping("/translate")
    public Void translate() {
        return null;
    }

    @PostMapping("/write")
    public Void write() {
        return null;
    }

    
}

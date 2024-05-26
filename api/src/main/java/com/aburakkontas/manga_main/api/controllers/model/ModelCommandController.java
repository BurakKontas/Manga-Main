package com.aburakkontas.manga_main.api.controllers.model;

import com.aburakkontas.manga_main.domain.bodies.*;
import com.aburakkontas.manga_main.domain.repositories.ModelRepository;
import com.aburakkontas.manga_main.domain.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/v1/model")
public class ModelCommandController {

    private static final Logger log = LoggerFactory.getLogger(ModelCommandController.class);
    private final CommandGateway commandGateway;
    private final Environment env;
    private final ModelRepository modelRepository;

    @Autowired
    public ModelCommandController(CommandGateway commandGateway, Environment env, ModelRepository modelRepository) {
        this.commandGateway = commandGateway;
        this.env = env;
        this.modelRepository = modelRepository;
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
    @PostMapping("/detectron")
    public DetectronResponse detectron(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return modelRepository.detectron(file.getBytes());
    }

    @PostMapping("/generate_mask")
    public GenerateMaskResponse generateMask(@RequestBody GenerateMaskBody request) {
        return modelRepository.generateMask(request);
    }

    @PostMapping("/madf")
    public MADFResponse madf(@RequestBody MADFBody request) {
        return modelRepository.madf(request);
    }

    @PostMapping("/meta_craft")
    public ArrayList<MetaResponse> metaCraft(@RequestBody MetaBody request) {
        return modelRepository.metaCraft(request);
    }

    @PostMapping("/crop_image")
    public ArrayList<CropImageResponse> cropImage(@RequestBody CropImageBody request) {
        return modelRepository.cropImage(request);
    }

    @PostMapping("/ocr")
    public ArrayList<OCRResponse> ocr(@RequestBody ArrayList<OCRBody> request) {
        return modelRepository.ocr(request);
    }

    @PostMapping("/translate")
    public TranslateResponse translate(@RequestBody TranslateBody request) {
        return modelRepository.translate(request);
    }

    @PostMapping("/write")
    public WriteResponse write(@RequestBody WriteBody request) {
        return modelRepository.write(request);
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
    @PostMapping("/all")
    public WriteResponse all(@RequestParam("file") MultipartFile file) {
        return null;
    }
    
}

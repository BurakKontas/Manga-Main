package com.aburakkontas.manga_main.api.controllers.model;

import com.aburakkontas.manga_main.domain.repositories.ModelRepository;
import com.aburakkontas.manga_main.domain.responses.DetectronResponse;
import com.google.common.net.MediaType;
import lombok.SneakyThrows;
import okhttp3.MultipartBody;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping("/detectron")
    public DetectronResponse detectron(@RequestParam("file") MultipartFile file) throws IOException {
        return modelRepository.detectron(file.getBytes());
    }
    
}

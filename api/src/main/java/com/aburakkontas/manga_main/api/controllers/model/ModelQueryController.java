package com.aburakkontas.manga_main.api.controllers.model;

import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String getModelPricing() {
        return "Model Pricing";
    }
    
}

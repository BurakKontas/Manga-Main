package com.aburakkontas.manga_main.api.controllers;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/model")
public class ModelController {

    private static final Logger log = LoggerFactory.getLogger(ModelController.class);
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final Environment env;

    @Autowired
    public ModelController(CommandGateway commandGateway, QueryGateway queryGateway, Environment env) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.env = env;
    }

    
}

package com.aburakkontas.manga_main.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.aburakkontas.manga_main.infrastructure")
@EnableJpaRepositories("com.aburakkontas.manga_main.infrastructure.repositories")
public class InfrastructureInjection { }

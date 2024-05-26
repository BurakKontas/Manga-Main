package com.aburakkontas.manga_main.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan({
        "com.aburakkontas.manga_main.domain.entities",
        "org.axonframework"
})
public class DomainInjection {
}
package com.aburakkontas.manga_main.domain.primitives;


import lombok.Getter;

import java.util.UUID;

@Getter
public class DomainEvent {
    private final UUID id;

    public DomainEvent(UUID id) {
        this.id = id;
    }

}
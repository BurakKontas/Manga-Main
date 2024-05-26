package com.aburakkontas.manga_main.domain.entities.work;

import com.aburakkontas.manga_main.domain.enums.ModelPricing;
import com.aburakkontas.manga_main.domain.enums.ModelTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "work_histories")
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class WorkHistory {
    @Id
    private final UUID id;
    private final UUID userId;
    private final UUID workId;
    private final Double creditsSpend;
    private final ModelTypes model;
    private final ZonedDateTime createdAt;
    private boolean success;
    private final String errorMessage;
    private final String errorCode;

    public static WorkHistory success(UUID userId, UUID workId, ModelTypes model) {
        return new WorkHistory(
                UUID.randomUUID(),
                userId,
                workId,
                ModelPricing.valueOf(model.name()).getPrice(),
                model,
                ZonedDateTime.now(),
                true,
                null,
                null
        );
    }

    public static WorkHistory error(UUID userId, UUID workId, ModelTypes model, String errorMessage, String errorCode) {
        return new WorkHistory(
                UUID.randomUUID(),
                userId,
                workId,
                0.0,
                model,
                ZonedDateTime.now(),
                false,
                errorMessage,
                errorCode
        );
    }
}

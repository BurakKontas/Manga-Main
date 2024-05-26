package com.aburakkontas.manga_main.domain.entities.work;

import com.aburakkontas.manga_main.domain.enums.ModelTypes;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "work_data")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class WorkData {
    @Id
    private final UUID id = UUID.randomUUID();
    private final UUID workId;

    private final ModelTypes modelType;

    @Convert(converter = JsonAttributeConverter.class)
    private final Object data;
    private final String type;

    public WorkData(ModelTypes modelType, Object data, UUID workId) {
        this.modelType = Objects.requireNonNull(modelType, "ModelType cannot be null");
        this.workId = Objects.requireNonNull(workId, "WorkId cannot be null");
        Objects.requireNonNull(data, "Data cannot be null");
        if (modelType.getInputType().isInstance(data)) {
            this.data = data;
            this.type = "input";
        } else if (modelType.getResultType().isInstance(data)) {
            this.data = data;
            this.type = "result";
        } else {
            throw new IllegalArgumentException("Data must be of type: " + modelType.getInputType().getName() + " or " + modelType.getResultType().getName());
        }
    }

    public <T> T getData(Class<T> clazz) {
        Objects.requireNonNull(clazz, "Class type cannot be null");

        if (type.equals("input") && !modelType.getInputType().equals(clazz)) {
            throw new IllegalArgumentException("Data must be of type: " + modelType.getInputType().getName());
        }

        if (type.equals("result") && !modelType.getResultType().equals(clazz)) {
            throw new IllegalArgumentException("Data must be of type: " + modelType.getResultType().getName());
        }

        return clazz.cast(data);
    }
}

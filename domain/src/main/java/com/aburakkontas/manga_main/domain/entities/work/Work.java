package com.aburakkontas.manga_main.domain.entities.work;

import com.aburakkontas.manga_main.domain.enums.ModelTypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "works")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Work {
    @Id
    private final UUID id = UUID.randomUUID();
    private final UUID workspaceId;
    private final String originalImage;
    private String name;
    private String description;
    private Boolean isFinished;
    private String image;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WorkHistory> history = List.of();
    @OneToMany(cascade = CascadeType.ALL)
    private List<WorkData> data = List.of();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private final List<WorkData> results = getData().stream().filter(workData -> workData.getType().equals("result")).toList();
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private final List<WorkData> inputs = getData().stream().filter(workData -> workData.getType().equals("input")).toList();

    public Work(UUID workspaceId, String name, String description, String originalImage) {
        this.workspaceId = workspaceId;
        this.name = name;
        this.description = description;
        this.isFinished = false;
        this.image = originalImage;
        this.originalImage = originalImage;
    }

    public void addSuccessWork(ModelTypes modelType, Object data, UUID userId) {
        var workData = new WorkData(modelType, data, userId);
        this.data.add(workData);
        var history = WorkHistory.success(userId, this.id, modelType);
        this.history.add(history);
    }

    public void addFailureWork(ModelTypes modelType, String errorMessage, String errorCode, UUID userId) {
        var history = WorkHistory.error(userId, this.id, modelType, errorMessage, errorCode);
        this.history.add(history);
    }

    public void updateWorkImage(String image) {
        this.image = image;
    }

    public void finishWork() {
        this.isFinished = true;
    }

    public void updateWorkName(String name) {
        this.name = name;
    }

    public void updateWorkDescription(String description) {
        this.description = description;
    }

    public void updateWorkData(String modelName, Object data, UUID userId) {
        var model = ModelTypes.valueOf(modelName);
        var workData = new WorkData(model, data, userId);
        this.data.add(workData);
    }
}

package com.aburakkontas.manga_main.domain.entities.workspace;

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
@Table
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class WorkspaceHistory {
    @Id
    private final UUID id;
    private final UUID userId;
    private final UUID workspaceId;
    private final String action;
    private final ZonedDateTime createdAt;
    private final boolean success;
    private final String errorMessage;
    private final String errorCode;

    public static WorkspaceHistory success(UUID userId, UUID workspaceId, String action) {
        return new WorkspaceHistory(
                UUID.randomUUID(),
                userId,
                workspaceId,
                action,
                ZonedDateTime.now(),
                true,
                null,
                null
        );
    }

    public static WorkspaceHistory error(UUID userId, UUID workspaceId, String action, String errorMessage, String errorCode) {
        return new WorkspaceHistory(
                UUID.randomUUID(),
                userId,
                workspaceId,
                action,
                ZonedDateTime.now(),
                false,
                errorMessage,
                errorCode
        );
    }
}

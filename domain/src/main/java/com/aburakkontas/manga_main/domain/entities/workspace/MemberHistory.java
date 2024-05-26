package com.aburakkontas.manga_main.domain.entities.workspace;

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
@Table(name = "member_histories")
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class MemberHistory {
    @Id
    private final UUID id = UUID.randomUUID();
    private final UUID memberId;
    private final String action;
    private final ZonedDateTime createdAt;
    private final boolean success;
    private final String errorMessage;
    private final String errorCode;

    public static MemberHistory success(UUID userId, String action) {
        return new MemberHistory(
                userId,
                action,
                ZonedDateTime.now(),
                true,
                null,
                null
        );
    }

    public static MemberHistory error(UUID userId, String action, String errorMessage, String errorCode) {
        return new MemberHistory(
                userId,
                action,
                ZonedDateTime.now(),
                false,
                errorMessage,
                errorCode
        );
    }
}

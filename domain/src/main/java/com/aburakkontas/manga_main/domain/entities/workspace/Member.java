package com.aburakkontas.manga_main.domain.entities.workspace;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "members")
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Member {

    @Id
    private final UUID id = UUID.randomUUID();

    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    private final UUID workspaceId;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = WorkspacePermissions.class)
    private List<WorkspacePermissions> permissions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberHistory> history = List.of();

    public Member(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void addPermission(WorkspacePermissions permission, UUID userId) {
        if(permissions.contains(permission)) throw new IllegalArgumentException("Permission already exists");
        permissions.add(permission);
        history.add(MemberHistory.success(userId, "addPermission"));
    }

    public void removePermission(WorkspacePermissions permission, UUID userId) {
        if(!permissions.contains(permission)) throw new IllegalArgumentException("Permission not found");
        permissions.remove(permission);
        history.add(MemberHistory.success(userId, "removePermission"));
    }

    public boolean hasPermission(WorkspacePermissions permission) {
        return permissions.contains(permission);
    }
}

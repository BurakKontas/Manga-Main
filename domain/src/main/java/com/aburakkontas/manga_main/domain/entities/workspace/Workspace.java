package com.aburakkontas.manga_main.domain.entities.workspace;

import com.aburakkontas.manga_main.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_main.domain.entities.work.Work;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workspaces")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Workspace {
    @Id
    private final UUID workspaceId = UUID.randomUUID();
    private final String workspaceName;
    private final String workspaceDescription;
    private final UUID ownerId;
    @ElementCollection
    private List<UUID> members = List.of();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Work> works = List.of();

    @OneToMany(cascade = CascadeType.ALL)
    private List<WorkspaceHistory> history = List.of();

    public Workspace(String workspaceName, String workspaceDescription, UUID ownerId) {
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
        this.ownerId = ownerId;
    }

    public void addWork(String name, String description, String image, UUID userId) {
        if(works.stream().anyMatch(w -> w.getName().equals(name))) throw new ExceptionWithErrorCode("Work already exists", 400);
        var work = new Work(this.workspaceId, name, description, image);
        this.works.add(work);
        this.history.add(WorkspaceHistory.success(userId, this.workspaceId, "addWork"));
    }

    public void removeWork(UUID workId, UUID userId) {
        var work = this.works.stream().filter(w -> w.getId().equals(workId)).findFirst().orElseThrow(() -> new ExceptionWithErrorCode("Work not found", 404));
        this.works.remove(work);
        this.history.add(WorkspaceHistory.success(userId, this.workspaceId, "removeWork"));
    }

    public void addMember(UUID memberId, UUID userId) {
        if(members.contains(memberId)) throw new ExceptionWithErrorCode("Member already exists", 400);
        this.members.add(memberId);
        this.history.add(WorkspaceHistory.success(userId, this.workspaceId, "addMember"));
    }

    public void removeMember(UUID memberId, UUID userId) {
        if(!members.contains(memberId)) throw new ExceptionWithErrorCode("Member not found", 404);
        this.members.remove(memberId);
        this.history.add(WorkspaceHistory.success(userId, this.workspaceId, "removeMember"));
    }
}

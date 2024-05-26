package com.aburakkontas.manga_main.application.aggregates;

import com.aburakkontas.manga.common.main.commands.workspace.*;
import com.aburakkontas.manga.common.main.events.workspace.*;
import com.aburakkontas.manga_main.domain.entities.work.Work;
import com.aburakkontas.manga_main.domain.entities.workspace.Workspace;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Aggregate
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkspaceAggregate {

    @AggregateIdentifier
    private UUID workspaceId;
    private Workspace workspace;

    @AggregateMember
    private List<WorkAggregate> works = new ArrayList<>();

    @CommandHandler
    public WorkspaceAggregate(WorkspaceCreatedCommand command) {
        AggregateLifecycle.apply(new WorkspaceCreatedEvent(command.getName(), command.getDescription(), command.getOwnerId()));
    }

    @EventSourcingHandler
    protected void on(WorkspaceCreatedEvent event) {
        this.workspace = new Workspace(event.getName(), event.getDescription(), event.getOwnerId());
        this.workspaceId = this.workspace.getWorkspaceId();
    }

    @CommandHandler
    public void handle(AddWorkToWorkspaceCommand command) {
        AggregateLifecycle.apply(new AddWorkToWorkspaceEvent(this.workspaceId, command.getName(), command.getDescription(), command.getOriginalImage()));
    }

    @EventSourcingHandler
    protected void on(AddWorkToWorkspaceEvent event) {
        var work = new Work(this.workspaceId, event.getName(), event.getDescription(), event.getOriginalImage());
        this.works.add(new WorkAggregate(work.getId(), work));
    }

    @CommandHandler
    public void handle(AddMemberToWorkspaceCommand command) {
        AggregateLifecycle.apply(new AddMemberToWorkspaceEvent(this.workspaceId, command.getMemberId(), command.getUserId()));
    }

    @EventSourcingHandler
    protected void on(AddMemberToWorkspaceEvent event) {
        this.workspace.addMember(event.getMemberId(), event.getUserId());
    }

    @CommandHandler
    public void handle(RemoveMemberFromWorkspaceCommand command) {
        AggregateLifecycle.apply(new RemoveMemberFromWorkspaceEvent(this.workspaceId, command.getMemberId(), command.getUserId()));
    }

    @EventSourcingHandler
    protected void on(RemoveMemberFromWorkspaceEvent event) {
        this.workspace.removeMember(event.getMemberId(), event.getUserId());
    }

    @CommandHandler
    public void handle(RemoveWorkFromWorkspaceCommand command) {
        AggregateLifecycle.apply(new RemoveWorkFromWorkspaceEvent(this.workspaceId, command.getWorkId(), command.getUserId()));
    }

    @EventSourcingHandler
    protected void on(RemoveWorkFromWorkspaceEvent event) {
        this.works.removeIf(workAggregate -> workAggregate.getWorkId().equals(event.getWorkId()));
    }

    @CommandHandler
    public void handle(WorkspaceDeletedCommand command) {
        AggregateLifecycle.apply(new WorkspaceDeletedEvent(this.workspaceId, command.getUserId()));
    }

    @EventSourcingHandler
    protected void on(WorkspaceDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }

}

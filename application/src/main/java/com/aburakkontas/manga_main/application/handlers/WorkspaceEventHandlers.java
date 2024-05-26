package com.aburakkontas.manga_main.application.handlers;

import com.aburakkontas.manga.common.main.events.workspace.*;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceEventHandlers {

    @EventHandler
    public void on(WorkspaceCreatedEvent event) {

    }

    @EventHandler
    public void on(AddWorkToWorkspaceEvent event) {

    }

    @EventHandler
    public void on(AddMemberToWorkspaceEvent event) {

    }

    @EventHandler
    public void on(RemoveMemberFromWorkspaceEvent event) {

    }

    @EventHandler
    public void on(RemoveWorkFromWorkspaceEvent event) {

    }

    @EventHandler
    public void on(WorkspaceDeletedEvent event) {

    }
}

package com.aburakkontas.manga_main.application.handlers;

import com.aburakkontas.manga.common.main.events.work.*;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class WorkEventHandlers {

    @EventHandler
    public void on(UpdateWorkEvent event) {

    }

    @EventHandler
    public void on(UpdateWorkDataEvent event) {

    }

    @EventHandler
    public void on(UpdateWorkImageEvent event) {

    }

    @EventHandler
    public void on(UseModelOnWorkEvent event) {

    }
}

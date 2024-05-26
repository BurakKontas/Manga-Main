package com.aburakkontas.manga_main.application.aggregates;

import com.aburakkontas.manga.common.main.commands.work.UpdateWorkCommand;
import com.aburakkontas.manga.common.main.commands.work.UpdateWorkDataCommand;
import com.aburakkontas.manga.common.main.commands.work.UseModelOnWorkCommand;
import com.aburakkontas.manga.common.main.events.work.*;
import com.aburakkontas.manga_main.domain.entities.work.Work;
import com.aburakkontas.manga_main.domain.enums.ModelTypes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.EntityId;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkAggregate {

    @EntityId
    @Getter
    private UUID workId;
    private Work work;

    public WorkAggregate(UUID workId, Work work) {
        this.workId = workId;
        this.work = work;
    }

    @CommandHandler
    public void handle(UpdateWorkCommand command) {
        AggregateLifecycle.apply(new UpdateWorkEvent(this.workId, command.getName(), command.getDescription(), command.isFinished()));
    }

    @EventSourcingHandler
    protected void on(UpdateWorkEvent event) {
        if(event.getName() != null) this.work.updateWorkName(event.getName());
        if(event.getDescription() != null) this.work.updateWorkDescription(event.getDescription());
        if(event.isFinished()) this.work.finishWork();
    }

    @CommandHandler
    public void handle(UseModelOnWorkCommand command) {
        AggregateLifecycle.apply(new UseModelOnWorkEvent(this.workId, command.getModelName(), command.getUserId(), command.getData()));
    }

    @EventSourcingHandler
    protected void on(UseModelOnWorkSuccessCommand event) {
        var model = ModelTypes.valueOf(event.getModelName());
        this.work.addSuccessWork(model, event.getData(), event.getUserId());
    }

    @EventSourcingHandler
    protected void on(UseModelOnWorkFailureCommand event) {
        var model = ModelTypes.valueOf(event.getModelName());
        this.work.addFailureWork(model, event.getErrorMessage(), event.getErrorCode(), event.getUserId());
    }

    @CommandHandler
    public void handle(UpdateWorkDataCommand command) {
        AggregateLifecycle.apply(new UpdateWorkDataEvent(this.workId, command.getModelName(), command.getData(), command.getUserId()));
    }

    @EventSourcingHandler
    protected void on(UpdateWorkDataEvent event) {
        var model = ModelTypes.valueOf(event.getModelName());
        this.work.updateWorkData(event.getModelName(), event.getData(), event.getUserId());
    }

}

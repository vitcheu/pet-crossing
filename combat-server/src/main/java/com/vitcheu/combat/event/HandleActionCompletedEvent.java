
package com.vitcheu.combat.event;

import org.springframework.context.ApplicationEvent;

import com.vitcheu.common.model.combat.messages.TurnResult;

import lombok.Getter;


public class HandleActionCompletedEvent extends ApplicationEvent {

    @Getter
    private final TurnResult turnResult;

    public HandleActionCompletedEvent(TurnResult turnResult) {
        super(turnResult);
        this.turnResult = turnResult;
    }
}

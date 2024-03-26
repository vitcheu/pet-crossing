
package com.vitcheu.combat.event;

import org.springframework.context.ApplicationEvent;

import com.vitcheu.common.model.combat.messages.CombatMessage;

import lombok.Getter;

public class MessageReceivedEvent extends ApplicationEvent{
    @Getter
    private long ownerId;
    @Getter
    private CombatMessage message;
    public MessageReceivedEvent(long ownerId,CombatMessage message) {
        super(message);
        this.ownerId=ownerId;
        this.message=message;
    }
}

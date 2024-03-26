
package com.vitcheu.owner.model.event;

public final class CombatReadyEvent extends CombatEvent{
    public CombatReadyEvent(long userId) {
        super(userId);
    }
}


package com.vitcheu.common.model.combat.messages;

import com.vitcheu.common.model.combat.MessageType;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public abstract class AbstactCombatData implements CombatData {
    private MessageType type;
}

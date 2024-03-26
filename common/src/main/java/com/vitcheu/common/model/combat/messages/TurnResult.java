package com.vitcheu.common.model.combat.messages;

import java.util.List;
import java.util.Map;

import com.vitcheu.common.model.combat.*;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class TurnResult extends AbstactCombatData {

  Map<Integer, List<PropertyChangeDto>> result;

  public TurnResult(Map<Integer, List<PropertyChangeDto>> result) {
    super(MessageType.RESULT);
    this.result = Map.copyOf(result); 
  }

  private TurnResult() {
    super(MessageType.RESULT);
    result = null;
  }

  @Override
  public void accept(MesageDataHandler handler) {
    handler.handle(this);
  }

  public static TurnResult of(Map<Integer, List<PropertyChangeDto>> result) {
    return new TurnResult(result);
  }

  public Map<Integer, List<PropertyChangeDto>> getResult() {
    return Map.copyOf(result);
  }
}

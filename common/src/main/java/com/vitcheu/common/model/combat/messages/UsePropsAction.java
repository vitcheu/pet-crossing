package com.vitcheu.common.model.combat.messages;

import com.vitcheu.common.model.combat.MesageDataHandler;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsePropsAction extends TurnAction {

  private int propId;
  private int count;

  public UsePropsAction(int propId, int count) {
    super(TurnActionType.PROPS);
    this.propId = propId;
    this.count = count;
  }

  protected UsePropsAction() {
    super(TurnActionType.PROPS);
  }

  @Override
  public void accept(MesageDataHandler handler) {
    handler.handle(this);
  }
}

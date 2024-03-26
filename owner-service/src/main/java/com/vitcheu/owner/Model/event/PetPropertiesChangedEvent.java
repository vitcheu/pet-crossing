package com.vitcheu.owner.model.event;

import com.vitcheu.common.model.Propertychange;
import java.util.List;
import lombok.Getter;

public class PetPropertiesChangedEvent extends GameEvent {

  @Getter
  private long userId;

  @Getter
  private boolean fromProps = true;

  private Propertychange[] changes;

  public PetPropertiesChangedEvent(
    long userId,
    boolean fromProps,
    Propertychange... event
  ) {
    super(event);
    this.userId = userId;
    this.fromProps = fromProps;
    this.changes = event;
  }

  public List<Propertychange> getChanges() {
    return List.of(changes);
  }
}

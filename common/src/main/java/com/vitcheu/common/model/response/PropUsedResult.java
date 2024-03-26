package com.vitcheu.common.model.response;

import java.util.List;

import com.vitcheu.common.model.combat.PropertyChangeDto;

public record PropUsedResult(List<PropertyChangeDto> changes) {
}

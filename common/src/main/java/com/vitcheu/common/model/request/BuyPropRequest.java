package com.vitcheu.common.model.request;

import jakarta.validation.constraints.Min;

public record BuyPropRequest(@Min(0) Integer propId, @Min(1) int quantity) {}

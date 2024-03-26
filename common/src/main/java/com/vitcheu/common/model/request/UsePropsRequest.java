package com.vitcheu.common.model.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;

/**
 * represents the action of using a prop by a user
 */
@Builder
public record UsePropsRequest(
  @Min(0) int petId,
  @Min(0) int propId,
  @Min(1) int count
) 
{
}

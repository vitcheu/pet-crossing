package com.vitcheu.common.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.FieldDefaults;

/**
 * Represents the response entity during Http calls;
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  String message;

  @Default
  T body = null;

  public static ApiResponse<Object> error(String errMsg) {
    ApiResponse<Object> apiResponse = ApiResponse
      .builder()
      .message("error:  " + errMsg)
      .build();

    return apiResponse;
  }
}

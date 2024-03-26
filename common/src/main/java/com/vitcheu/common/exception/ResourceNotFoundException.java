package com.vitcheu.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String resourceName;
  private String fieldName;
  private transient Object fieldValue;

  public ResourceNotFoundException(
    String resourceName,
    String fieldName,
    Object fieldValue
  ) {
    super(
      String.format(
        "%s not found with %s : '%s'",
        resourceName,
        fieldName,
        fieldValue
      )
    );
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public ResourceNotFoundException(String message) {
    super(message);
  }

}

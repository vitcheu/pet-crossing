package com.vitcheu.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class RemoteAccessException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String resourceName;

  public RemoteAccessException(String resourceName) {
    super(String.format("Accessing %s from remote failed.", resourceName));
    this.resourceName = resourceName;
  }
}

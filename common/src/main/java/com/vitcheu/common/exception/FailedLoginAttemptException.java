package com.vitcheu.common.exception;

public class FailedLoginAttemptException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public FailedLoginAttemptException() {
    super("Failed login attempt...");
  }
}

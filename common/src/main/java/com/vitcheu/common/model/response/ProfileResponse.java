package com.vitcheu.common.model.response;

public record ProfileResponse(String userName, String email)
  implements java.io.Serializable {
  private static final long serialVersionUID = 1L;
}

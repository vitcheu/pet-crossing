package com.vitcheu.gateway.service;

import com.vitcheu.common.model.AuthenticationToken;

public interface AuthenticationService {
    AuthenticationToken  loadAuthentication(String Username);
}
package com.auth_service.auth_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RoleNotFoundException extends AppException {
    public RoleNotFoundException(String roleName) {
        super("Role not found: " + roleName);
    }
}
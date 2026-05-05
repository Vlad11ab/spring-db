package com.example.springdb.config.security;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserPermissions {
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;

    public String getPermission(){
        return permission;
    }
}

package com.example.springdb.config.security;

public class SecurityConstants {
    public static final String ISSUER = "";
    public static final String AUDIENCE = "";
    public static final String AUTHORITIES = "authorities";
    public static final String[] PUBLIC_URLS = {
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    };

    private SecurityConstants(){

    }
}

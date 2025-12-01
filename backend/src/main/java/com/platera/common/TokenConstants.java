package com.platera.common;

public class TokenConstants {
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60; // 5h
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTHORITIES_KEY = "auth";
}

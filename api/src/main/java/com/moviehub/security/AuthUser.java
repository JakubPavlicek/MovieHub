package com.moviehub.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUser {

    private AuthUser() {
    }

    public static String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}

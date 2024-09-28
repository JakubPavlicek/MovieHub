package com.moviehub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender {
    UNSPECIFIED("Unspecified"),
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-binary");

    private final String value;
}

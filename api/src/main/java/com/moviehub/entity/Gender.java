package com.moviehub.entity;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@RequiredArgsConstructor
@Getter
public enum Gender {
    UNSPECIFIED("Unspecified"),
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-binary");

    private final String value;
}

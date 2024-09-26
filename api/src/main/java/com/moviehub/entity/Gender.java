package com.moviehub.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    UNSPECIFIED("Unspecified"),
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-binary");

    private String value;
}

package com.moviehub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Enum representing the gender of a person.
@RequiredArgsConstructor
@Getter
public enum Gender {
    UNSPECIFIED("Unspecified"),
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-binary");

    private final String value;
}

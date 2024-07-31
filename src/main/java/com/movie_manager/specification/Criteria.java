package com.movie_manager.specification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Criteria {

    EQ("eq");

    private final String name;
}

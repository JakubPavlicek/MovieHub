package com.moviehub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInfo(
    @JsonProperty("sub") String id,
    @JsonProperty("nickname") String name,
    @JsonProperty("email") String email,
    @JsonProperty("picture") String pictureUrl
) {}

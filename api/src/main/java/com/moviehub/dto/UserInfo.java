package com.moviehub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Record representing user information fetched from Auth0 Management API.
public record UserInfo(
    @JsonProperty("sub") String id,
    @JsonProperty("nickname") String name,
    @JsonProperty("email") String email,
    @JsonProperty("picture") String pictureUrl
) {

}

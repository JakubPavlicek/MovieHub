package com.moviehub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client")
@Getter
@Setter
public class ClientUrlProperties {

    private String url;

}

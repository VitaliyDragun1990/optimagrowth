package com.optimagrowh.authentication.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties("jwt.key.private")
public class JwtKeyProperties {

    private final String name;

    private final String alias;

    private final String password;
}

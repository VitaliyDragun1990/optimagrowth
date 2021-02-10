package com.optimagrowth.license.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties("jwt")
public class JwtProperties {

    private final String publicKey;
}

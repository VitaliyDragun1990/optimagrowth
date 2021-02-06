package com.optimagrowth.license.domain.service.license.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties("example")
public class ExampleProperties {

    private final String property;
}

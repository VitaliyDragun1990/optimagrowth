package com.optimagrowh.authentication.config;

import com.optimagrowh.authentication.AuthenticationServiceApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = AuthenticationServiceApplication.class)
public class ConfigurationPropertiesConfig {

}

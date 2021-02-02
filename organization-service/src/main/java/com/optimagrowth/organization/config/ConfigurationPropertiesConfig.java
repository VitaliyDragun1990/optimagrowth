package com.optimagrowth.organization.config;

import com.optimagrowth.organization.OrganizationServiceApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = OrganizationServiceApplication.class)
public class ConfigurationPropertiesConfig {

}

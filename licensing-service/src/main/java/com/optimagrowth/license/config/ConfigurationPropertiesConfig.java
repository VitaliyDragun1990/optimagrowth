package com.optimagrowth.license.config;

import com.optimagrowth.license.LicenseServiceApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = LicenseServiceApplication.class)
public class ConfigurationPropertiesConfig {

}

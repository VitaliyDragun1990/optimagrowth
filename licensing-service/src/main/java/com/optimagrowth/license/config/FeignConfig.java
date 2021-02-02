package com.optimagrowth.license.config;

import com.optimagrowth.license.LicenseServiceApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = LicenseServiceApplication.class)
@Configuration
public class FeignConfig {

}

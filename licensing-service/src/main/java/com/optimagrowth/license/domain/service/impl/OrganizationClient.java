package com.optimagrowth.license.domain.service.impl;

import com.optimagrowth.license.config.feign.OrganizationServiceFeignClientConfig;
import com.optimagrowth.license.domain.service.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "organization-service", configuration = OrganizationServiceFeignClientConfig.class)
public interface OrganizationClient {

    @GetMapping(
            value = "/v1/organizations/{organizationId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Organization findById(@PathVariable("organizationId") String organizationId);
}

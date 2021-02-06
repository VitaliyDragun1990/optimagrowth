package com.optimagrowth.license.domain.repository.redis;

import com.optimagrowth.license.domain.service.organization.Organization;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface OrganizationCache extends KeyValueRepository<Organization, String> {

}

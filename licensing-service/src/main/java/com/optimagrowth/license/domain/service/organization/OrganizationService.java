package com.optimagrowth.license.domain.service.organization;

import com.optimagrowth.license.domain.service.organization.Organization;
import com.optimagrowth.license.exception.ResourceNotFoundException;

public interface OrganizationService {

    Organization findById(String organizationId) throws ResourceNotFoundException;
}

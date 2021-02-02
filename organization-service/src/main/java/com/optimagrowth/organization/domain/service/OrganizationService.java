package com.optimagrowth.organization.domain.service;

import com.optimagrowth.organization.domain.service.model.Organization;

import java.util.List;

public interface OrganizationService {

    Organization findById(String organizationId);

    List<Organization> findAll();

    Organization create(Organization organization);

    Organization update(Organization organization);

    void deleteById(String organizationId);
}

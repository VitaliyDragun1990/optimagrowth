package com.optimagrowth.license.domain.service.organization.impl;

import com.optimagrowth.license.domain.repository.redis.OrganizationCache;
import com.optimagrowth.license.domain.service.organization.Organization;
import com.optimagrowth.license.domain.service.organization.OrganizationClient;
import com.optimagrowth.license.domain.service.organization.OrganizationService;
import com.optimagrowth.license.usercontext.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrganizationServiceFacade implements OrganizationService {

    private final OrganizationClient organizationClient;

    private final OrganizationCache organizationCache;

    @Override
    public Organization findById(String organizationId) {
        LOG.debug("Searching for organization with id:[{}], Correlation Id:[{}]",
                organizationId, UserContextHolder.getContext().getCorrelationId());

        Optional<Organization> organizationFromCache = findInCache(organizationId);

        if (organizationFromCache.isEmpty()) {
            LOG.debug("Unable to locate organization with id:[{}] in the cache", organizationId);

            Organization organization = organizationClient.findById(organizationId);
            LOG.debug("Get organization with id:[{}] using client", organization);

            putIntoCache(organization);

            return organization;
        }

        LOG.debug("Retrieved organization with id:[{}] from cache", organizationId);

        return organizationFromCache.get();
    }

    private void putIntoCache(Organization organization) {
        try {
            organizationCache.save(organization);
            LOG.debug("Put organization with id:[{}] into cache", organization.getId());
        } catch (Exception e) {
            LOG.warn("Unable to put organization with id:[{}] into cache:{}",
                    organization.getId(), e.getMessage(), e);
        }
    }

    private Optional<Organization> findInCache(String organizationId) {
        try {
            return organizationCache.findById(organizationId);
        } catch (Exception e) {
            LOG.warn("Error while looking organization with id:[{}] in the cache:{}", organizationId, e.getMessage(), e);
            return Optional.empty();
        }
    }
}

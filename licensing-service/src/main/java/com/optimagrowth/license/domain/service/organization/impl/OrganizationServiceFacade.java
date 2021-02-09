package com.optimagrowth.license.domain.service.organization.impl;

import com.optimagrowth.license.domain.repository.redis.OrganizationCache;
import com.optimagrowth.license.domain.service.organization.Organization;
import com.optimagrowth.license.domain.service.organization.OrganizationClient;
import com.optimagrowth.license.domain.service.organization.OrganizationService;
import com.optimagrowth.license.usercontext.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.ScopedSpan;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrganizationServiceFacade implements OrganizationService {

    private static final String SPAN_READ_ORGANIZATION_FROM_CACHE = "readOrganizationDataFromCache";

    private static final String CACHE_PROVIDER_REDIS = "redis";

    private final OrganizationClient organizationClient;

    private final OrganizationCache organizationCache;

    private final Tracer tracer;

    @Override
    public Organization findById(String organizationId) {
        LOG.debug("Searching for organization with id:[{}], Correlation Id:[{}]",
                organizationId, UserContextHolder.getContext().getCorrelationId());

        Optional<Organization> organizationFromCache = findInCache(organizationId);

        if (organizationFromCache.isEmpty()) {
            LOG.debug("Unable to locate organization with id:[{}] in the cache", organizationId);

            LOG.debug("Sending request to get organization with id:[{}] using client", organizationId);
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
        ScopedSpan newSpan = tracer.startScopedSpan(SPAN_READ_ORGANIZATION_FROM_CACHE);
        try {
            return organizationCache.findById(organizationId);
        } catch (Exception e) {
            LOG.warn("Error while looking organization with id:[{}] in the cache:{}", organizationId, e.getMessage(), e);
            return Optional.empty();
        } finally {
            newSpan.tag("peer.service", CACHE_PROVIDER_REDIS);
            newSpan.event("Client received");
            newSpan.end();
        }
    }
}

package com.optimagrowth.organization.domain.service.impl;

import com.optimagrowth.organization.domain.entity.OrganizationEntity;
import com.optimagrowth.organization.domain.event.publisher.OrganizationEventPublisher;
import com.optimagrowth.organization.domain.event.model.EventType;
import com.optimagrowth.organization.domain.repository.OrganizationRepository;
import com.optimagrowth.organization.domain.service.OrganizationService;
import com.optimagrowth.organization.domain.service.model.Organization;
import com.optimagrowth.organization.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.sleuth.ScopedSpan;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private static final String SPAN_READ_ORGANIZATION_FROM_DB = "readOrganizationFromDB";

    private static final String DB_POSTGRES = "postgres";

    private final OrganizationRepository organizationRepository;

    private final ModelMapper mapper;

    private final OrganizationEventPublisher eventPublisher;

    private final Tracer tracer;

    @Override
    public Organization findById(String organizationId) {
        LOG.debug("Searching for organization with id:[{}]", organizationId);

        OrganizationEntity organizationEntity = getById(organizationId);

        Organization organization = mapper.map(organizationEntity, Organization.class);

        eventPublisher.publishOrganizationChange(EventType.READ, organizationId);

        LOG.debug("Found organization with id:[{}]", organizationId);

        return organization;
    }

    @Override
    public List<Organization> findAll() {
        LOG.debug("Searching all existing organizations");

        List<OrganizationEntity> organizationEntities = organizationRepository.findAll();

        List<Organization> organizations = organizationEntities.stream().map(o -> mapper.map(o, Organization.class)).collect(toList());

        LOG.debug("Found {} organizations", organizations.size());

        return organizations;
    }

    @Transactional
    @Override
    public Organization create(Organization organization) {
        LOG.debug("Creating organization using data:[{}]", organization);

        OrganizationEntity organizationEntity = mapper.map(organization, OrganizationEntity.class);
        organizationEntity.setId(UUID.randomUUID().toString());

        organizationRepository.save(organizationEntity);

        eventPublisher.publishOrganizationChange(EventType.CREATE, organizationEntity.getId());

        LOG.debug("Created organization with data:{}", organizationEntity);

        return mapper.map(organizationEntity, Organization.class);
    }

    @Transactional
    @Override
    public Organization update(Organization organization) {
        LOG.debug("Updating organization using data:{}", organization);

        OrganizationEntity organizationEntity = getById(organization.getId());

        updateOrganizationData(organizationEntity, organization);
        organizationRepository.save(organizationEntity);

        eventPublisher.publishOrganizationChange(EventType.UPDATE, organizationEntity.getId());

        LOG.debug("Updated organization with data:{}", organizationEntity);

        return mapper.map(organizationEntity, Organization.class);
    }

    @Override
    public void deleteById(String organizationId) {
        LOG.debug("Deleting organization with id:[{}]", organizationId);

        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization with id:%s not found", organizationId);
        }

        organizationRepository.deleteById(organizationId);

        eventPublisher.publishOrganizationChange(EventType.DELETE, organizationId);

        LOG.debug("Deleted organization with id:[{}]", organizationId);
    }

    private OrganizationEntity getById(String organizationId) {
        ScopedSpan newSpan = tracer.startScopedSpan(SPAN_READ_ORGANIZATION_FROM_DB);
        try {
            return organizationRepository
                    .findById(organizationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Organization with id:%s not found", organizationId));
        } finally {
            newSpan.tag("peer.service", DB_POSTGRES);
            newSpan.event("Client received");
            newSpan.end();
        }
    }

    private void updateOrganizationData(OrganizationEntity entity, Organization data) {
        entity.setName(data.getName());
        entity.setContactEmail(data.getContactEmail());
        entity.setContactPhone(data.getContactPhone());
        entity.setContactName(data.getContactName());
    }
}

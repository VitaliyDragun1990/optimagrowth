package com.optimagrowth.organization.domain.event.publisher;

import com.optimagrowth.organization.domain.event.model.EventType;

public interface OrganizationEventPublisher {

    void publishOrganizationChange(EventType eventType, String organizationId);
}

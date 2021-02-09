package com.optimagrowth.license.domain.event.consumer;

import com.optimagrowth.license.domain.event.model.EventTypes;
import com.optimagrowth.license.domain.event.model.ResourceChangeEvent;
import com.optimagrowth.license.domain.event.model.ResourceTypes;
import com.optimagrowth.license.domain.repository.redis.OrganizationCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class ResourceEventConsumer {

    private final OrganizationCache organizationCache;

    @Bean
    public Consumer<ResourceChangeEvent<String>> organizationChange() {
        return event -> {
            if (ResourceTypes.ORGANIZATION.equals(event.getResourceType())) {
                handleOrganizationEvent(event.getEventType(), event.getResourceId());
            }
        };
    }

    // TODO: define event handler for each resource type
    public void handleOrganizationEvent(String eventType, String organizationId) {
        LOG.debug("Received a {} event for Organization with id:[{}]", eventType, organizationId);

        if (EventTypes.DELETE.equals(eventType) || EventTypes.UPDATE.equals(eventType)) {
            organizationCache.deleteById(organizationId);
            LOG.debug("Deleted organization with id:[{}] from cache", organizationId);
        }
    }
}

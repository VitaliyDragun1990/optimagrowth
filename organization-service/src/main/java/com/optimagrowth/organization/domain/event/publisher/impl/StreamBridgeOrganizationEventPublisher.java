package com.optimagrowth.organization.domain.event.publisher.impl;

import com.optimagrowth.organization.domain.event.publisher.OrganizationEventPublisher;
import com.optimagrowth.organization.domain.event.model.EventType;
import com.optimagrowth.organization.domain.event.model.ResourceChangeEvent;
import com.optimagrowth.organization.domain.event.model.ResourceType;
import com.optimagrowth.organization.provider.date.DateTimeProvider;
import com.optimagrowth.organization.usercontext.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreamBridgeOrganizationEventPublisher implements OrganizationEventPublisher {

    private final String outputBindingName;

    private final StreamBridge stream;

    private final DateTimeProvider dateTimeProvider;

    public StreamBridgeOrganizationEventPublisher(
            @Value("${spring.cloud.stream.source}") String outputBindingName,
            StreamBridge stream,
            DateTimeProvider dateTimeProvider) {
        this.outputBindingName = outputBindingName;
        this.stream = stream;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public void publishOrganizationChange(EventType eventType, String organizationId) {
        String correlationId = UserContextHolder.getContext().getCorrelationId();

        LOG.debug("Sending event of type [{}] for Organization with id:[{}], Correlation Id:[{}]...",
                eventType, organizationId, correlationId);

        ResourceChangeEvent<String> event = new ResourceChangeEvent<>(
                organizationId,
                ResourceType.ORGANIZATION,
                eventType,
                correlationId,
                dateTimeProvider.currentDateTime());

        boolean success = stream.send(outputBindingName, event);

        LOG.debug("Event of type [{}] for Organization with id:[{}], Correlation Id:[{}] was sent, status:{}",
                eventType, organizationId, correlationId, success ? "success" : "failure");
    }
}

package com.optimagrowth.organization.domain.event.publisher.impl;

import com.optimagrowth.organization.domain.event.model.EventType;
import com.optimagrowth.organization.domain.event.model.ResourceChangeEvent;
import com.optimagrowth.organization.domain.event.model.ResourceType;
import com.optimagrowth.organization.domain.event.publisher.OrganizationEventPublisher;
import com.optimagrowth.organization.provider.date.DateTimeProvider;
import com.optimagrowth.organization.usercontext.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimpleSourceEventPublisher implements OrganizationEventPublisher {

    private final Source source;

    private final DateTimeProvider dateTimeProvider;

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

        boolean success = source.output().send(MessageBuilder.withPayload(event).build());

        LOG.debug("Event of type [{}] for Organization with id:[{}], Correlation Id:[{}] was sent, status:{}",
                eventType, organizationId, correlationId, success ? "success" : "failure");
    }
}

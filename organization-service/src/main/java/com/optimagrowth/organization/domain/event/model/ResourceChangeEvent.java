package com.optimagrowth.organization.domain.event.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceChangeEvent<K> {

    private K resourceId;

    private ResourceType resourceType;

    private EventType eventType;

    private String correlationId;

    private LocalDateTime createdAt;

}

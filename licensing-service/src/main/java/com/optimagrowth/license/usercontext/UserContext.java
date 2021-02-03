package com.optimagrowth.license.usercontext;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Contains information related to the current request
 */
@Getter
@RequiredArgsConstructor
@ToString
public class UserContext {

    private final String correlationId;

    private final String authToken;

    private final String userId;

    private final String organizationId;
}

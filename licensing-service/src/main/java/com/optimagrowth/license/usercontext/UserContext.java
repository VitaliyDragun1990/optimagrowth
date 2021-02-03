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

    public static final String CORRELATION_ID = "tmx-correlation-id";

    public static final String AUTH_TOKEN = "tmx-auth-token";

    public static final String USER_ID = "tmx-user-id";

    public static final String ORGANIZATION_ID = "tmx-organization-id";

    private final String correlationId;

    private final String authToken;

    private final String userId;

    private final String organizationId;
}

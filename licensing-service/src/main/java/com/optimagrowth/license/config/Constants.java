package com.optimagrowth.license.config;

public final class Constants {

    private Constants() {
    }

    public static final class Messages {

        public static final String LICENSE_DELETED = "license.delete.message";

        public static final String SEARCH_ERROR = "license.search.error.message";

        private Messages() {
        }
    }

    public static final class Headers {

        public static final String CORRELATION_ID = "tmx-correlation-id";

        public static final String AUTH_TOKEN = "tmx-auth-token";

        public static final String USER_ID = "tmx-user-id";

        public static final String ORGANIZATION_ID = "tmx-organization-id";

        private Headers() {
        }
    }
}

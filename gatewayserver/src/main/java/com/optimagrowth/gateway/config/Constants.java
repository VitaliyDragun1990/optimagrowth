package com.optimagrowth.gateway.config;

public final class Constants {

    private Constants() {
    }

    public static final class Headers {

        public static final String CORRELATION_ID = "tmx-correlation-id";

        public static final String AUTH_TOKEN = "Authorization";

        public static final String USER_ID = "tmx-user-id";
        
        public static final String ORG_ID = "tmx-org-id";

        private Headers() {
        }
    }
}

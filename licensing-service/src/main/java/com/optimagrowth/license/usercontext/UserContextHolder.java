package com.optimagrowth.license.usercontext;

import org.springframework.util.Assert;

/**
 * Holds {@link UserContext} related to current request
 */
public final class UserContextHolder {

    private static final ThreadLocal<UserContext> USER_CONTEXT = new ThreadLocal<>();

    public static UserContext getContext() {
        UserContext context = USER_CONTEXT.get();

        if (context == null) {
            throw new IllegalStateException("Fail to find UserContext for current request.");
        }

        return context;
    }

    public static void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        USER_CONTEXT.set(context);
    }

    private UserContextHolder() {
    }
}

package com.optimagrowth.gateway.filter;

import java.util.Map;

public interface TokenParser {

    Map<String, Object> parse(String tokenValue);
}

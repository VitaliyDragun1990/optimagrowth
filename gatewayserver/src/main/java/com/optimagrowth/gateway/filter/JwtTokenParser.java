package com.optimagrowth.gateway.filter;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JwtTokenParser implements TokenParser {

    private final Base64 base64 = new Base64(true);

    @Override
    public Map<String, Object> parse(String jwtValue) {
        String[] tokenParts = jwtValue.split("\\.");
        String base64EncodedBody = tokenParts[1];

        String jsonString = new String(base64.decode(base64EncodedBody));

        return new JSONObject(jsonString).toMap();
    }
}

package com.optimagrowth.license.config.security;

import com.optimagrowth.license.config.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String CLAIM_NAME_AUTHORITIES = "authorities";

    private static final String AUTHORITY_PREFIX_EMPTY = "";

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().oauth2ResourceServer(oauth2customizer -> oauth2customizer.jwt(jwtCustomizer -> {
                    jwtCustomizer.decoder(jwtDecoder());

                    JwtAuthenticationConverter jwtAuthenticationConverter = getJwtAuthenticationConverter();

                    jwtCustomizer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                })
        );
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] key = Base64.getDecoder().decode(jwtProperties.publicKey());
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(key);
            RSAPublicKey rsaKey = (RSAPublicKey) keyFactory.generatePublic(x509);
            return NimbusJwtDecoder.withPublicKey(rsaKey).build();
        } catch (Exception e) {
            throw new ConfigurationException(e, "Fail to get public key to validate access token:%s", e.getMessage());
        }
    }

    private JwtAuthenticationConverter getJwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = getJwtGrantedAuthoritiesConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    private JwtGrantedAuthoritiesConverter getJwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(CLAIM_NAME_AUTHORITIES);
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AUTHORITY_PREFIX_EMPTY);
        return jwtGrantedAuthoritiesConverter;
    }
}

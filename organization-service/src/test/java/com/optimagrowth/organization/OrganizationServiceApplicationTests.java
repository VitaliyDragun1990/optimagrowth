package com.optimagrowth.organization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class OrganizationServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

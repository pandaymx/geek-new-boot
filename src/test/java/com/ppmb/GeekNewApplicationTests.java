package com.ppmb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Disabled;
import org.springframework.context.annotation.Import;

@Disabled("Sandbox environment might not support Docker Testcontainers")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class GeekNewApplicationTests {

    @Test
    void contextLoads() {}
}

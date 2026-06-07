package com.ppmb;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Disabled("Testcontainers may fail to run in sandbox environments without a Docker daemon")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class GeekNewApplicationTests {

    @Test
    void contextLoads() {}
}

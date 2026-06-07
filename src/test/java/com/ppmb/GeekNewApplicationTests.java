package com.ppmb;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Disabled("Testcontainers fails to run in sandbox environment due to overlayfs issues")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {"spring.modulith.republish-outstanding-events-on-restart=false"})
class GeekNewApplicationTests {

    @Test
    void contextLoads() {}
}

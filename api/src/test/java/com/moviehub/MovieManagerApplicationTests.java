package com.moviehub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(properties = {"spring.liquibase.contexts=test"})
@Import(TestcontainersConfiguration.class)
class MovieManagerApplicationTests {

    @Test
    void contextLoads() {
    }

}

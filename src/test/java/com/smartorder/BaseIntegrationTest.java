package com.smartorder;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for all integration tests that need a real database.
 *
 * HOW IT WORKS:
 * Testcontainers spins up a real PostgreSQL Docker container before tests run.
 * @DynamicPropertySource overrides the datasource URL to point at that container.
 * Flyway runs migrations automatically — same SQL as production.
 * Container is shared across all tests in the test run (STATIC field).
 *
 * LEARNING NOTE:
 * This means your integration tests hit a real Postgres, not H2.
 * H2 has subtle SQL compatibility differences that hide real bugs.
 * With Testcontainers, what passes locally passes in CI — same container image.
 *
 * PREREQUISITE: Docker must be running on your machine.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public abstract class BaseIntegrationTest {

    static final PostgreSQLContainer<?> POSTGRES =
        new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("smartorder")
            .withUsername("smartorder")
            .withPassword("smartorder");

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}

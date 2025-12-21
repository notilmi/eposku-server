package org.ilmi.eposkuserver;

import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestContainersConfig {
    // No-op configuration since tests now use H2 via @ActiveProfiles("test") and application-test.properties
}

package com.ucm.lib.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Unit test specific configurations; will override
 */
@Profile("test")
@Configuration
public class TestConfiguration {

}

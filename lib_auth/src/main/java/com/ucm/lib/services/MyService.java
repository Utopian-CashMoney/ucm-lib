package com.ucm.lib.services;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Not a part of the library, just exists to test project configuration. May be removed later.
 */
@Service
@EnableConfigurationProperties(ServiceProperties.class)
public class MyService {
    private final ServiceProperties serviceProperties;

    public MyService(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    public String message() {
        return this.serviceProperties.getMessage();
    }
}

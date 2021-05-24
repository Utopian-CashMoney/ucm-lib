package com.ucm.lib.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Not a part of the library, just exists to test project configuration. May be removed later.
 */
@ConfigurationProperties("service")
public class ServiceProperties {
    /**
     * A message for the service.
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

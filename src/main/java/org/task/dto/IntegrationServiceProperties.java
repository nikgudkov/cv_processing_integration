package org.task.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "integration.extraction-service")
@Data
@Component
public class IntegrationServiceProperties {

    private String account;
    private String username;
    private String password;
    private String serverUrl;

}

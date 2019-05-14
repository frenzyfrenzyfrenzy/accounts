package com.svintsov.accounts;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("accounts")
public class AccountsProperties {

    private String requestSchemaPath;
    private String responseSchemaPath;

    public String getRequestSchemaPath() {
        return requestSchemaPath;
    }

    public void setRequestSchemaPath(String requestSchemaPath) {
        this.requestSchemaPath = requestSchemaPath;
    }

    public String getResponseSchemaPath() {
        return responseSchemaPath;
    }

    public void setResponseSchemaPath(String responseSchemaPath) {
        this.responseSchemaPath = responseSchemaPath;
    }
}

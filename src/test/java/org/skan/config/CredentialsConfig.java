package org.skan.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:credentials.properties")
public interface CredentialsConfig extends Config {
    String username();

    String password();

    String token();
}
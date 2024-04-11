package com.company.reports.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class URLConfig {
    @Value("${url.scheme}")
    private String scheme ;

    @Value("${url.host}")
    private String host;

    @Value("${url.path}")
    private String path;

    public String getScheme(){
        return this.scheme;
    }

    public String getHost(){
        return this.host;
    }

    public String getPath(){
        return this.path;
    }
}

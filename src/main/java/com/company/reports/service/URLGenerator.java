package com.company.reports.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class URLGenerator {
    private String scheme , host, paths[] ,domain ;
    private Map<String,String> params;
    private static final Logger logger = LoggerFactory.getLogger(URLGenerator.class);

    public String generateURL(String scheme,String host , String[] paths , Map<String,String> params) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.scheme(scheme).host(host);
            Arrays.stream(paths)
                    .forEach(path -> builder.path(path));

            params.forEach((key, value) -> builder.queryParam(key, value));

            // Build the URI
            String url = builder.build().toUriString();

            return url;

        }catch (Exception e){
            logger.error("Error in URL generation : " + e.getMessage());
            return e.getMessage();
        }
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public void setHost(String host){
        this.host = host;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
    public void setPath(String[] paths){
        this.paths = paths;
    }

    public void setParam(Map<String,String>  params){
        this.params = params;
    }
}

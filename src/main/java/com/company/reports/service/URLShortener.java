package com.company.reports.service;
import com.company.reports.model.thirdParty.RequestObj;
import com.company.reports.model.thirdParty.ResponseObj;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.company.reports.model.thirdParty.*;

@Service
public class URLShortener {

    @Value("${tiny.cc.api.key}")
    private String API_KEY;

    @Value("${tiny.cc.api.token}")
    private String API_TOKEN;

    @Value("${tiny.cc.api.url}")
    private String API_URI;

    @Value("${tiny.cc.api.domain}")
    private String domain;

    @Value("${tiny.cc.api.alias}")
    private String alias;
    @Value("${tiny.cc.api.description}")
    private String description;

    public String shortenURL(String longUrl) throws IOException, InterruptedException {
        String apiKey = API_KEY;
        String apiToken=API_TOKEN;
        String apiUrl = API_URI + apiKey;
        String domain = this.domain;
        String alias = this.alias;
        String description=this.description;

        RequestObj requestObj=new RequestObj();
        requestObj.setUrl(longUrl);
        requestObj.setDomain(domain);
        requestObj.setAlias(alias);
        requestObj.setDescription(description);
        // Create ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(requestObj);

         requestBody =requestBody.replace("alias","Alias");
        System.out.println("api:" + API_KEY);
//        String requestBody =String.format("{\"url\": \"%s\", \"domain\": \"%s\" ,\"Alias\": \"%s\",\"description\": \"%s\"}",
//                longUrl, domain,alias,description);

        System.out.println("req body:"+requestBody);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ResponseObj responseObj = objectMapper.readValue(response.body(), ResponseObj.class);
         Data data= responseObj.getData();
         String shortenedUrl = data.getTiny_url();
        // MyData myData = mapper.readValue(jsonString, MyData.class);
        System.out.println("Shortened URL: " + shortenedUrl);

        return shortenedUrl;
    }
}

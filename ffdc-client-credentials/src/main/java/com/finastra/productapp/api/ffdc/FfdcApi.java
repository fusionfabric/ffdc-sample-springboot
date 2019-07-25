package com.finastra.productapp.api.ffdc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class FfdcApi {

    @Value("${ffdcapi.baseUrl}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> getCurrencyResources () {
        
        UriComponents uriBuilder = UriComponentsBuilder
                .fromUriString(baseUrl+"/referential/v1/currencies")
                .build();
        return restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, null, Object.class);
    }
}

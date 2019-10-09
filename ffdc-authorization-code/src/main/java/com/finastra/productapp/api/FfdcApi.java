package com.finastra.productapp.api;

import com.finastra.productapp.model.TradeCaptureStaticDataList;
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

    public TradeCaptureStaticDataList getReferenceSourcesLegalEntities () {
        UriComponents uriBuilder = UriComponentsBuilder
                .fromUriString(baseUrl + "/capital-market/trade-capture/static-data/v1/reference-sources")
                .queryParam("applicableEntities", "legal-entities")
                .build();
        return restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, null, TradeCaptureStaticDataList.class).getBody();
    }
}

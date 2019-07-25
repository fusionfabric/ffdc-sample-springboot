package com.finastra.productapp.api.ffdc;

import com.finastra.productapp.api.ApiBinding;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class FfdcApi extends ApiBinding {

    private final String apiReferenceResourcesUrl;

    public FfdcApi(String accessToken, String apiReferenceResourcesUrl) {
        super(accessToken);
        this.apiReferenceResourcesUrl = apiReferenceResourcesUrl;
    }

    public ResponseEntity<?> getReferenceSources (String entitiesName) {
        UriComponents uriBuilder = UriComponentsBuilder
                .fromUriString(apiReferenceResourcesUrl).queryParam("applicableEntities", entitiesName)
                .build();
        return restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, null, Object.class);
    }
}

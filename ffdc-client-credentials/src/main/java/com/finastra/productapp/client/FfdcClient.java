package com.finastra.productapp.client;

import com.finastra.productapp.model.Countries;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static com.finastra.productapp.client.WebClientConfiguration.FFDC_API_CLIENT;

@Component
@RequiredArgsConstructor
public class FfdcClient {
    private final String REFERENTIAL_DATA_COUNTRIES_ENDPOINT = "/referential/v1/countries";
    @Qualifier(FFDC_API_CLIENT)
    private final WebClient webClient;

    public Countries getReferentialDataCountries() {
        return webClient
                .get()
                .uri(REFERENTIAL_DATA_COUNTRIES_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Countries.class)
                .block();


    }
}

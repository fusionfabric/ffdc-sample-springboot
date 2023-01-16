package com.finastra.productapp.api.service;

import com.finastra.productapp.client.FfdcClient;
import com.finastra.productapp.model.Countries;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FfdcService {
    private final FfdcClient ffdcClient;

    public Countries getReferentialDataCountries() {
        return ffdcClient.getReferentialDataCountries();
    }
}

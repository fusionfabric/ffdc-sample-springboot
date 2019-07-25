package com.finastra.productapp.endpoint;

import com.finastra.productapp.api.ffdc.FfdcApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class ProductController {

    @Autowired
    private FfdcApi ffdcApi;

    @GetMapping("/")
    public ResponseEntity getCurrencies(){
        return ffdcApi.getCurrencyResources();
    }

}
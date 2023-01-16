package com.finastra.productapp.model;

import lombok.Data;

@Data
public class Country {
    private String name;
    private String alpha2code;
    private String alpha3code;
    private String currency;
}

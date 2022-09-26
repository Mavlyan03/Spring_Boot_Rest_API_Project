package com.example.spring_boot_rest_api_project.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {

    private String companyName;
    private String locatedCountry;
}

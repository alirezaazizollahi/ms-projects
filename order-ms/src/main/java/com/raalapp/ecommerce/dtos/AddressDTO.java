package com.raalapp.ecommerce.dtos;

import lombok.Data;

@Data
public class AddressDTO {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}

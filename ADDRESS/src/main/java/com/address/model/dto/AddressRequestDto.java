package com.address.model.dto;

import com.address.model.enums.AddressType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {
    private Long id;
    private String street;
    private Long pinCode;
    private String city;
    private String country;
    private AddressType addressType;
}

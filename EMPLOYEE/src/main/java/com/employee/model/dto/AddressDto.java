package com.employee.model.dto;

import com.employee.model.enums.AddressType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private Long empId;
    private String street;
    private Long pinCode;
    private String city;
    private String country;

    private AddressType addressType;
}

package com.address.service.impl;

import java.util.List;

import com.address.client.EmployeeClient;
import com.address.model.dto.EmployeeDto;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.address.exceptions.ResourceNotFoundException;
import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.model.entity.Address;
import com.address.repository.AddressRepository;
import com.address.service.AddressService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final EmployeeClient employeeClient;

    @Override
    public List<AddressDto> saveAddress(AddressRequest addressRequest) {

        employeeClient.getSingleEmployee(addressRequest.getEmpId());

        List<Address> addresses = addressRequest.getAddressRequestDtoList()
                .stream()
                .map(addressDto -> {
                    Address address = modelMapper.map(addressDto, Address.class);
                    address.setEmpId(addressRequest.getEmpId()); // empId lives on the wrapper, not the DTO
                    return address;
                })
                .toList();

        addresses = addressRepository.saveAll(addresses);

        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();

    }

    @Override
    public List<AddressDto> updateAddress(AddressRequest addressRequest) {
        // TODO check if employee exists
        employeeClient.getSingleEmployee(addressRequest.getEmpId());
        List<Address> existingAddresses = addressRepository.findAllByEmpId(addressRequest.getEmpId());
        if (existingAddresses.isEmpty()) {
            log.info("Addresses not found for employee id: {}", addressRequest.getEmpId());
            log.info("Creating new addresses for employee id: {}", addressRequest.getEmpId());
            return saveAddress(addressRequest);
        }

        // update existing addresses
        List<Address> upComingAddresses = addressRequest.getAddressRequestDtoList()
                .stream()
                .map(addressDto -> {
                    Address address = modelMapper.map(addressDto, Address.class);
                    address.setEmpId(addressRequest.getEmpId()); // empId lives on the wrapper, not the DTO
                    return address;
                })
                .toList();
        log.info("Addresses to update: {}", upComingAddresses);

        List<Long> nonNullUpComingIds = upComingAddresses.stream()
                .map(address -> address.getId())
                .filter(id -> id != null)
                .toList();

        List<Long> addressIdsToDelete = existingAddresses.stream()
                .map(address -> address.getId())
                .filter(id -> !nonNullUpComingIds.contains(id))
                .toList();

        if (!addressIdsToDelete.isEmpty()) {
            log.info("Deleting addresses with ids: {}", addressIdsToDelete);
            addressRepository.deleteAllById(addressIdsToDelete);
        }

        upComingAddresses = addressRepository.saveAll(upComingAddresses);

        return upComingAddresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();
    }

    @Override
    public AddressDto getSingleAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddresses( Long empId) {
        List<Address> allAddresses = addressRepository.findAllByEmpId(empId);
        if (allAddresses.isEmpty()) {
            throw new ResourceNotFoundException("Address not found with id: " + empId);
        }
        return allAddresses.stream()
            .map(address -> modelMapper.map(address, AddressDto.class))
            .toList();
     }

    @Override
    public void deleteAddress(Long id, Long empId) {
        addressRepository.findByIdAndEmpId(id, empId).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        addressRepository.deleteByIdAndEmpId(id,empId);
        log.info("Address deleted successfully with id: {}", id);
    }

}



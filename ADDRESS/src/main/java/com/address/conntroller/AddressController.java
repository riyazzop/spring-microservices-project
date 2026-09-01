package com.address.conntroller;

import java.util.List;

import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.service.AddressService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/addresses")
@AllArgsConstructor
public class AddressController {
    
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<List<AddressDto>> saveAddresses(@RequestBody AddressRequest addressRequest){
        List<AddressDto> response = addressService.saveAddress(addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<List<AddressDto>> updateAddresses(@RequestBody AddressRequest addressRequest){
        List<AddressDto> response = addressService.updateAddress(addressRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
     }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getSingleAddress(@PathVariable Long id){
        AddressDto response = addressService.getSingleAddress(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<AddressDto>> getAllAddresses(@PathVariable Long id){
        List<AddressDto> allAddresses = addressService.getAllAddresses(id);

        return ResponseEntity.ok(allAddresses);
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteAddress(@RequestParam Long id, @RequestParam Long empId) {
        addressService.deleteAddress(id, empId);
        return ResponseEntity.ok("Address deleted successfully");
    }
}



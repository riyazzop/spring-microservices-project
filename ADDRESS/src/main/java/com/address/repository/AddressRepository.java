package com.address.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.address.model.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

    List<Address> findAllByEmpId(Long empId);

    Optional<Address> findByIdAndEmpId(Long id, Long empId);

    @Transactional
    void deleteByIdAndEmpId(Long id, Long empId);
    
}

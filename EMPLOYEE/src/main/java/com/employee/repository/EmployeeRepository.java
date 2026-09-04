package com.employee.repository;

import com.employee.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findByEmpCodeAndCompanyName(String empCode, String companyName);

    boolean existsByEmpEmail(String empEmail);


}

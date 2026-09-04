package com.employee.service;


import com.employee.model.dto.AddressDto;
import com.employee.model.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);

    EmployeeDto updateEmployee(Long id,EmployeeDto employeeDto);

    EmployeeDto getEmployee(Long id);

    List<EmployeeDto> getAllEmployees();

    void deleteEmployee(Long id);

    EmployeeDto getEmployeeByEmpCodeAndCompanyName(String empCode, String companyName);

}

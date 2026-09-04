package com.employee.service.impl;

import com.employee.client.AddressClient;
import com.employee.exceptions.BadRequestException;
import com.employee.exceptions.ResourceNotFoundException;
import com.employee.model.dto.AddressDto;
import com.employee.model.dto.EmployeeDto;
import com.employee.model.entity.Employee;
import com.employee.repository.EmployeeRepository;
import com.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final AddressClient addressClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        if (employeeDto.getId() != null) {
            throw new BadRequestException("Employee already exists");
        }
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        if (employeeRepository.existsByEmpEmail(employeeDto.getEmpEmail())){
            throw new BadRequestException("Employee already exists with email: "+employeeDto.getEmpEmail());
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDto.class);


    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        if (id == null || employeeDto.getId() == null) {
            throw new BadRequestException("Employee id is null");
        }
        if ( !id.equals(employeeDto.getId())) {
            throw new BadRequestException("Employee id does not match");
        }
        employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id : "+id));
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id : " + id));

        List<AddressDto> allAddresses = new ArrayList<>();
        try {
            allAddresses = addressClient.getAllAddresses(id);
        } catch (Exception e) {
            log.error("Address not found with id : {}", id);
        }

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        employeeDto.setAddresses(allAddresses);

        return employeeDto;



    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        var employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found");
        }
        List<EmployeeDto> employeeDtoList = employees.stream()
                .map((element) -> modelMapper.map(element, EmployeeDto.class))
                .toList();
        for (EmployeeDto employeeDto : employeeDtoList) {
            List<AddressDto> allAddresses = new ArrayList<>();
            try {
                allAddresses = addressClient.getAllAddresses(employeeDto.getId());
                employeeDto.setAddresses(allAddresses);

            } catch (Exception e) {

                log.error("Address not found with id: {}" ,employeeDto.getId());
            }
        }
        return employeeDtoList;
    }

    @Override
    public void deleteEmployee(Long id) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id : "+id));

        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeDto getEmployeeByEmpCodeAndCompanyName(String empCode, String companyName) {
        Employee employee = employeeRepository.findByEmpCodeAndCompanyName(empCode,companyName)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with : "+empCode +" and " + companyName));
        return modelMapper.map(employee, EmployeeDto.class);
    }



}

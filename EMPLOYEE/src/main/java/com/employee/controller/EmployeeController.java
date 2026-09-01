package com.employee.controller;

import com.employee.exceptions.MissingParametersException;
import com.employee.model.dto.EmployeeDto;
import com.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto response = employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,@RequestBody EmployeeDto employeeDto) {
    EmployeeDto response = employeeService.updateEmployee(id,employeeDto);
    return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("Employee deleted successfully",HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        EmployeeDto response = employeeService.getEmployee(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<EmployeeDto> getEmployeeByEmpCodeAndCompanyName(@RequestParam(required = false) String empCode, @RequestParam(required = false) String companyName) {
        List<String> missingParams = new ArrayList<>();
        if (empCode == null || empCode.trim().isEmpty()){
            missingParams.add("empCode");
        }
        if (companyName == null || companyName.trim().isEmpty()){
            missingParams.add("companyName");
        }
        if(!missingParams.isEmpty()){
            String finalMessage = missingParams.stream().collect(Collectors.joining(","));
            throw new MissingParametersException("Please provide : " + finalMessage);
        }
        EmployeeDto employeeDto = employeeService.getEmployeeByEmpCodeAndCompanyName(empCode,companyName);
        return ResponseEntity.ok(employeeDto);
    }


}

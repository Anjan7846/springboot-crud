package com.example.crud.service;

import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.model.Employee;
import com.example.crud.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        emp.setName(employeeDetails.getName());
        emp.setDepartment(employeeDetails.getDepartment());
        emp.setSalary(employeeDetails.getSalary());
        return repository.save(emp);
    }

    public void deleteEmployee(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}

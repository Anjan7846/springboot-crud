package com.example.crud.service;

import com.example.crud.dto.EmployeeDTO;
import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.model.Employee;
import com.example.crud.repository.EmployeeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "employees")  // ✅ caches the list of employees
    public List<Employee> getAllEmployees() {
        System.out.println("Fetching all employees from DB...");
        return repository.findAll();
    }

    @Cacheable(value = "employee", key = "#id")  // ✅ caches individual employee
    public Employee getEmployeeById(Long id) {
        System.out.println("Fetching employee from DB...");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    @Transactional
    @CacheEvict(value = {"employees"}, allEntries = true) // ✅ clear employee list cache
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        validateEmployee(employeeDTO);
        Employee emp = new Employee();
        emp.setName(employeeDTO.getName());
        emp.setDepartment(employeeDTO.getDepartment());
        emp.setSalary(calculateNetSalary(employeeDTO.getSalary(), employeeDTO.getDepartment()));
        return repository.save(emp);
    }

    @Transactional
    @CachePut(value = "employee", key = "#id")  // ✅ update cache for the specific employee
    @CacheEvict(value = {"employees"}, allEntries = true)
    public Employee updateEmployee(Long id, EmployeeDTO employeeDTO) {
        validateEmployee(employeeDTO);
        Employee emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        emp.setName(employeeDTO.getName());
        emp.setDepartment(employeeDTO.getDepartment());
        emp.setSalary(calculateNetSalary(employeeDTO.getSalary(), employeeDTO.getDepartment()));
        return repository.save(emp);
    }

    @CacheEvict(value = {"employee", "employees"}, allEntries = true)
    public void deleteEmployee(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    private double calculateNetSalary(double baseSalary, String department) {
        double bonusPercentage = switch (department.toLowerCase()) {
            case "engineering" -> 0.15;
            case "hr" -> 0.10;
            case "sales" -> 0.20;
            default -> 0.05;
        };
        double tax = baseSalary * 0.10;
        double bonus = baseSalary * bonusPercentage;
        return baseSalary + bonus - tax;
    }

    private void validateEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getName() == null || employeeDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Employee name cannot be empty");
        }
        if (employeeDTO.getSalary() <= 0) {
            throw new IllegalArgumentException("Salary must be positive");
        }
    }

    @Cacheable(value = "highEarners", key = "#minSalary")  // ✅ cache high earners
    public List<Employee> getHighEarners(double minSalary) {
        System.out.println("Fetching high earners from DB...");
        return repository.findAll().stream()
                .filter(emp -> emp.getSalary() > minSalary)
                .collect(Collectors.toList());
    }
}

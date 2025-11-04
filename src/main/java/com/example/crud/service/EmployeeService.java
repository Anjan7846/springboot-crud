package com.example.crud.service;

import com.example.crud.dto.EmployeeDTO;
import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.model.Employee;
import com.example.crud.repository.EmployeeRepository;
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

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    @Transactional
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        validateEmployee(employeeDTO);

        Employee emp = new Employee();
        emp.setName(employeeDTO.getName());
        emp.setDepartment(employeeDTO.getDepartment());
        emp.setSalary(calculateNetSalary(employeeDTO.getSalary(), employeeDTO.getDepartment()));

        return repository.save(emp);
    }

    @Transactional
    public Employee updateEmployee(Long id, EmployeeDTO employeeDTO) {
        validateEmployee(employeeDTO);

        Employee emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        emp.setName(employeeDTO.getName());
        emp.setDepartment(employeeDTO.getDepartment());
        emp.setSalary(calculateNetSalary(employeeDTO.getSalary(), employeeDTO.getDepartment()));

        return repository.save(emp);
    }

    public void deleteEmployee(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Complex business logic:
     * Calculates final salary based on department and bonus/tax rules.
     */
    private double calculateNetSalary(double baseSalary, String department) {
        double bonusPercentage = switch (department.toLowerCase()) {
            case "engineering" -> 0.15;
            case "hr" -> 0.10;
            case "sales" -> 0.20;
            default -> 0.05;
        };

        double tax = baseSalary * 0.10;  // 10% tax
        double bonus = baseSalary * bonusPercentage;
        double netSalary = baseSalary + bonus - tax;

        if (netSalary < 0) throw new IllegalArgumentException("Invalid salary calculation result.");
        return netSalary;
    }

    /**
     * Validate employee data before processing.
     */
    private void validateEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getName() == null || employeeDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Employee name cannot be empty");
        }
        if (employeeDTO.getSalary() <= 0) {
            throw new IllegalArgumentException("Salary must be positive");
        }
    }

    /**
     * Get employees earning above a certain threshold (business reporting logic)
     */
    public List<Employee> getHighEarners(double minSalary) {
        return repository.findAll().stream()
                .filter(emp -> emp.getSalary() > minSalary)
                .collect(Collectors.toList());
    }
}

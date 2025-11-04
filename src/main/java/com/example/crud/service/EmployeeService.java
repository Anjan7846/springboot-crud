package com.example.crud.service;

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
        return repository.findById(id).orElse(null);
    }

    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee emp = repository.findById(id).orElse(null);
        if (emp != null) {
            emp.setName(employeeDetails.getName());
            emp.setDepartment(employeeDetails.getDepartment());
            emp.setSalary(employeeDetails.getSalary());
            return repository.save(emp);
        }
        return null;
    }

    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
}

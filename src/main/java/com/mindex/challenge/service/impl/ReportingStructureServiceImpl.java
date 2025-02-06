package com.mindex.challenge.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure generate(String employeeId) {
        try {
            Employee employee = employeeRepository.findByEmployeeId(employeeId);
            if (employee == null) {
                throw new IllegalArgumentException("Invalid employeeId: " + employeeId);
            }
            int totalReports = countReports(employee, new HashSet<>());
            return new ReportingStructure(employee, totalReports);
        } catch (Exception e) {
            throw e;
        }
    }

    private int countReports(Employee employee, Set<String> visited) {
        if (employee == null || visited.contains(employee.getEmployeeId())) {
            return 0;
        }
        visited.add(employee.getEmployeeId());
        int count = 0;
        if (employee.getDirectReports() != null) {
            for (Employee directReport : employee.getDirectReports()) {
                Employee report = employeeRepository.findByEmployeeId(directReport.getEmployeeId());
                if (report != null) {
                    count += 1 + countReports(report, visited);
                }
            }
        }
        return count;
    }
}

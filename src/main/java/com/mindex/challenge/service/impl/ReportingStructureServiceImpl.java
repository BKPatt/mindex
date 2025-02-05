package com.mindex.challenge.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure generate(String employeeId) {
        LOG.debug("Generating reporting structure for id [{}]", employeeId);
        
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        // Use Set to calculate total reports to handle circular references
        int totalReports = countReports(employee, new HashSet<>());
        
        return new ReportingStructure(employee, totalReports);
    }

    private int countReports(Employee employee, Set<String> counted) {
        if (employee.getDirectReports() == null) {
            return 0;
        }

        int total = 0;
        for (Employee report : employee.getDirectReports()) {
            // Skip if we've already counted employee to prevent infinite loops
            if (counted.contains(report.getEmployeeId())) {
                continue;
            }
            
            // Marking this employee as counted
            counted.add(report.getEmployeeId());
            
            // Get full employee data and add their reports
            Employee fullReport = employeeRepository.findByEmployeeId(report.getEmployeeId());
            total += 1 + countReports(fullReport, counted);
        }
        
        return total;
    }
}
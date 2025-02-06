package com.mindex.challenge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.CompensationNotFoundException;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        String employeeId = compensation.getEmployeeId();
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
        }

        compensation.setEmployee(employee);
        Compensation savedCompensation = compensationRepository.save(compensation);        
        savedCompensation.setEmployee(employee);
        
        return savedCompensation;
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation for employee [{}]", employeeId);

        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
        }

        return compensationRepository.findFirstByEmployeeId(employeeId)
            .map(compensation -> {
                compensation.setEmployee(employee);
                return compensation;
            })
            .orElseThrow(() -> new CompensationNotFoundException(employeeId));
    }
}
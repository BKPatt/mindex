package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

// Service interface for generating report structures
public interface ReportingStructureService {
    ReportingStructure generate(String employeeId);
}
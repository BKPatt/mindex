package com.mindex.challenge.exception;

public class CompensationNotFoundException extends RuntimeException {
    public CompensationNotFoundException(String employeeId) {
        super("No compensation found for employee ID: " + employeeId);
    }
}
package com.mindex.challenge.data;

import java.time.LocalDate;

/**
 * Built to represent an employee's comp details
 * Persisted in MongoDB with employee reference
 */
public class Compensation {
    private String employeeId;
    private Employee employee;
    private int salary;
    private LocalDate effectiveDate;

    public Compensation() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee != null ? employee.getEmployeeId() : null;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
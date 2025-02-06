package com.mindex.challenge.service.impl;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.exception.ErrorResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImplTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String compensationUrl;
    private String compensationByIdUrl;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationByIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateCompensationForInvalidEmployee() {
        String nonExistentEmployeeId = "non-existent-employee-id";
        Compensation compensation = new Compensation();
        compensation.setEmployeeId(nonExistentEmployeeId);
        compensation.setSalary(50000);
        compensation.setEffectiveDate(LocalDate.of(2024, 1, 1));

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            compensationUrl,
            compensation,
            ErrorResponse.class
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Employee not found with ID: " + nonExistentEmployeeId, response.getBody().getMessage());
    }

    @Test
    public void testReadCompensationForEmployeeWithoutCompensation() {
        String employeeId = "b7839309-3348-463b-a7e3-5de1c168beb3";

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
            compensationByIdUrl,
            ErrorResponse.class,
            employeeId
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No compensation found for employee ID: " + employeeId, response.getBody().getMessage());
    }

    @Test
    public void testCreateAndReadCompensationForValidEmployee() {
        String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";

        LOG.info("Starting test with employeeId: {}", employeeId);

        Compensation newComp = new Compensation();
        newComp.setEmployeeId(employeeId);
        newComp.setSalary(120000);
        newComp.setEffectiveDate(LocalDate.of(2023, 6, 1));

        LOG.info("Sending create request to: {}", compensationUrl);
        ResponseEntity<Compensation> createResponse = restTemplate.postForEntity(compensationUrl, newComp, Compensation.class);
        LOG.info("Create response status: {}", createResponse.getStatusCode());
        LOG.info("Create response body: {}", createResponse.getBody());

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull("Create response body should not be null", createResponse.getBody());

        String readUrl = compensationUrl + "/" + employeeId;
        LOG.info("Sending read request to: {}", readUrl);
        ResponseEntity<Compensation> readResponse = restTemplate.getForEntity(readUrl, Compensation.class);
        LOG.info("Read response status: {}", readResponse.getStatusCode());
        LOG.info("Read response body: {}", readResponse.getBody());

        assertEquals("Read response status should be OK", HttpStatus.OK, readResponse.getStatusCode());
        Compensation readComp = readResponse.getBody();
        assertNotNull("Read response body should not be null", readComp);

        if (readComp != null) {
            LOG.info("Verification - Compensation ID: {}", readComp.getId());
            LOG.info("Verification - Employee: {}", readComp.getEmployee());
            LOG.info("Verification - Salary: {}", readComp.getSalary());
            LOG.info("Verification - Date: {}", readComp.getEffectiveDate());

            assertEquals("Salary should match", 120000, readComp.getSalary());
            assertEquals("Date should match", LocalDate.of(2023, 6, 1), readComp.getEffectiveDate());
            assertNotNull("Employee should not be null", readComp.getEmployee());

            if (readComp.getEmployee() != null) {
                assertEquals("Employee ID should match", employeeId, readComp.getEmployee().getEmployeeId());
                assertEquals("First name should be John", "John", readComp.getEmployee().getFirstName());
                assertEquals("Last name should be Lennon", "Lennon", readComp.getEmployee().getLastName());
            }
        }
    }

    @Test
    public void testCreateCompensationWithEmptyEmployeeId() {
        Compensation compensation = new Compensation();
        compensation.setEmployeeId("");
        compensation.setSalary(75000);
        compensation.setEffectiveDate(LocalDate.of(2024, 3, 1));

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            compensationUrl,
            compensation,
            ErrorResponse.class
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Employee ID cannot be null or empty", response.getBody().getMessage());
    }

    @Test
    public void testCreateCompensationWithNullEmployeeId() {
        Compensation compensation = new Compensation();
        compensation.setEmployeeId(null);
        compensation.setSalary(85000);
        compensation.setEffectiveDate(LocalDate.of(2024, 6, 15));

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
            compensationUrl,
            compensation,
            ErrorResponse.class
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Employee ID cannot be null or empty", response.getBody().getMessage());
    }
}
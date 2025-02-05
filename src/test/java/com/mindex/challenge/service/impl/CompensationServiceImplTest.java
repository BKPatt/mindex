package com.mindex.challenge.service.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String compensationUrl;
    private String compensationIdUrl;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateReadCompensation() {
    // Create test employee
    Employee testEmployee = new Employee();
    testEmployee.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
    testEmployee.setFirstName("John");
    testEmployee.setLastName("Lennon");
    testEmployee.setDepartment("Engineering");
    testEmployee.setPosition("Development Manager");

    // Create test compensation
    Compensation testCompensation = new Compensation();
    testCompensation.setEmployee(testEmployee);
    testCompensation.setSalary(150000);
    testCompensation.setEffectiveDate(LocalDate.of(2024, 1, 1));

    // Create compensation
    ResponseEntity<Compensation> createResponse = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class);
    assertEquals(HttpStatus.OK, createResponse.getStatusCode());

    Compensation createdCompensation = createResponse.getBody();
    assertNotNull(createdCompensation);
    assertCompensationEquivalence(testCompensation, createdCompensation);

    // Read compensation
    ResponseEntity<Compensation> readResponse = restTemplate.getForEntity(compensationIdUrl, Compensation.class, testEmployee.getEmployeeId());
    assertEquals(HttpStatus.OK, readResponse.getStatusCode());

    Compensation readCompensation = readResponse.getBody();
    assertNotNull(readCompensation);
    assertCompensationEquivalence(testCompensation, readCompensation);
    }

    @Test
    public void testNonexistentEmployee() {
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId("invalid-id");
        testCompensation.setSalary(100000);
        testCompensation.setEffectiveDate(LocalDate.of(2024, 1, 1));

        ResponseEntity<Compensation> response = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertNotNull(actual.getEmployee());
        assertEquals(expected.getEmployeeId(), actual.getEmployee().getEmployeeId());
    }
}
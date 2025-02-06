package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception.ErrorResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String reportingStructureUrl;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/reporting-structure/{id}";
    }

    @Test
    public void testFullHierarchy() {
        String id = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        ResponseEntity<ReportingStructure> response = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReportingStructure structure = response.getBody();
        assertNotNull(structure);
        assertNotNull(structure.getEmployee());
        assertEquals("John", structure.getEmployee().getFirstName());
        assertEquals("Lennon", structure.getEmployee().getLastName());
        assertEquals(4, structure.getNumberOfReports());
    }

    @Test
    public void testPartialHierarchy() {
        String id = "03aa1462-ffa9-4978-901b-7c001562cf6f";
        ResponseEntity<ReportingStructure> response = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReportingStructure structure = response.getBody();
        assertNotNull(structure);
        assertEquals("Ringo", structure.getEmployee().getFirstName());
        assertEquals(2, structure.getNumberOfReports());
    }

    @Test
    public void testNoReports() {
        String id = "62c1084e-6e34-4630-93fd-9153afb65309";
        ResponseEntity<ReportingStructure> response = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReportingStructure structure = response.getBody();
        assertNotNull(structure);
        assertEquals("Pete", structure.getEmployee().getFirstName());
        assertEquals(0, structure.getNumberOfReports());
    }

    @Test
    public void testInvalidEmployee() {
        String id = "invalid-id";
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(reportingStructureUrl, ErrorResponse.class, id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid employeeId: invalid-id", response.getBody().getMessage());
    }

    @Test
    public void testPaulMcCartney() {
        String id = "b7839309-3348-463b-a7e3-5de1c168beb3";
        ResponseEntity<ReportingStructure> response = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReportingStructure structure = response.getBody();
        assertNotNull(structure);
        assertEquals("Paul", structure.getEmployee().getFirstName());
        assertEquals(0, structure.getNumberOfReports());
    }
}

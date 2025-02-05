package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.ReportingStructure;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testReportingStructure() {
        // Test John Lennon's structure (4 reports)
        String johnLennonId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        ReportingStructure structure = restTemplate.getForEntity(getReportingStructureUrl(johnLennonId), 
                                                               ReportingStructure.class).getBody();
        
        assertNotNull(structure);
        assertNotNull(structure.getEmployee());
        assertEquals(johnLennonId, structure.getEmployee().getEmployeeId());
        assertEquals(4, structure.getNumberOfReports());

        // Test Ringo's structure (2 reports)
        String ringoId = "03aa1462-ffa9-4978-901b-7c001562cf6f";
        structure = restTemplate.getForEntity(getReportingStructureUrl(ringoId), 
                                            ReportingStructure.class).getBody();
        
        assertNotNull(structure);
        assertEquals(2, structure.getNumberOfReports());

        // Test Pete Best's structure (no reports)
        String peteId = "62c1084e-6e34-4630-93fd-9153afb65309";
        structure = restTemplate.getForEntity(getReportingStructureUrl(peteId), 
                                            ReportingStructure.class).getBody();
        
        assertNotNull(structure);
        assertEquals(0, structure.getNumberOfReports());
    }

    private String getReportingStructureUrl(String employeeId) {
        return "http://localhost:" + port + "/reporting-structure/" + employeeId;
    }
}
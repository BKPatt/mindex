package com.mindex.challenge.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mindex.challenge.data.Compensation;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    @Query(sort = "{ 'effectiveDate' : -1 }")
    List<Compensation> findByEmployeeId(String employeeId);
    
    @Query(value = "{ 'employeeId' : ?0 }", sort = "{ 'effectiveDate' : -1 }")
    Optional<Compensation> findFirstByEmployeeId(String employeeId);
}
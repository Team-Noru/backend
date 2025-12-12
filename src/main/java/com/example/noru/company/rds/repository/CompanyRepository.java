package com.example.noru.company.rds.repository;

import com.example.noru.company.rds.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, String> {
    @Query("""
        SELECT c.companyId 
        FROM Company c 
        WHERE c.isDomestic = true 
          AND c.isListed = true
    """)
    List<String> findValidCompanyIds();
}

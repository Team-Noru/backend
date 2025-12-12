package com.example.noru.company.rds.service;

import com.example.noru.company.rds.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<String> getDomesticListedCompanies() {
        return companyRepository.findValidCompanyIds();
    }
}
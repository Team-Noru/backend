package com.example.noru.company.graph.service;

import com.example.noru.company.graph.node.CompanyGraphEntity;
import com.example.noru.company.graph.repository.CompanyGraphRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyGraphService {

    private final CompanyGraphRepository repository;

    @Transactional("neo4jTransactionManager")
    public CompanyGraphEntity getCompanyGraph(String entityId) {
        return repository.findEntityWithRelations(entityId)
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
    }
}
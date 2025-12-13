package com.example.noru.company.graph.repository;

import com.example.noru.company.graph.node.CompanyGraphEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface CompanyGraphRepository
        extends Neo4jRepository<CompanyGraphEntity, String> {

    @Query("""
    MATCH (e:Entity {ticker: $ticker})
    OPTIONAL MATCH (e)-[r:RELATION]->(t:Entity)
    OPTIONAL MATCH (e)<-[r2:RELATION]-(t2:Entity)
    RETURN e,
           collect(r), collect(t),
           collect(r2), collect(t2)
    """)
    Optional<CompanyGraphEntity> findByTicker(String ticker);


}

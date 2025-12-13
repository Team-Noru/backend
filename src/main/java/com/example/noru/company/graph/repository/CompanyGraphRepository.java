package com.example.noru.company.graph.repository;

import com.example.noru.company.graph.node.CompanyGraphEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface CompanyGraphRepository
        extends Neo4jRepository<CompanyGraphEntity, String> {

    @Query("""
    MATCH (e:Entity {corp_code: $id})
    OPTIONAL MATCH (e)<-[r:RELATION]-(t:Entity)
    RETURN e, collect(r) AS relations, collect(t) AS targets
    """)
    Optional<CompanyGraphEntity> findEntityWithRelations(String id);

}

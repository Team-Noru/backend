package com.example.noru.company.graph.node;

import com.example.noru.company.graph.relationship.CompanyGraphRelation;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;

@Node("Entity")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyGraphEntity {

    @Id
    private String id;                 // "corp:01480708"

    @Property("corp_code")
    private String corpCode;

    private String ticker;

    private String name;

    private String market;

    @Property("market_cap_eok")
    private Double marketCapEok;

    @Property("cap_bucket")
    private String capBucket;

    @Property("entity_type")
    private String entityType;

    @Relationship(type = "RELATION", direction = Relationship.Direction.INCOMING)
    private List<CompanyGraphRelation> relations;

}

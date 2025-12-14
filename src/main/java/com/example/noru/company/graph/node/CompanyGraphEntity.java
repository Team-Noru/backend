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

    @Property("entity_type")
    private String entityType;

    private String country;

    @Property("is_listed")
    private boolean isListed;


    @Relationship(type = "RELATION", direction = Relationship.Direction.OUTGOING)
    private List<CompanyGraphRelation> outgoingRelations;

    @Relationship(type = "RELATION", direction = Relationship.Direction.INCOMING)
    private List<CompanyGraphRelation> incomingRelations;

}

package com.example.noru.company.graph.relationship;

import com.example.noru.company.graph.node.CompanyGraphEntity;
import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyGraphRelation {

    @Id
    @GeneratedValue
    private Long internalId;

    @Property("event_date")
    private String eventDate;

    @Property("event_tag")
    private String eventTag;

    @Property("event_type")
    private String eventType;

    @Property("rcept_no")
    private String rceptNo;

    @Property("rel_type")
    private String relType;

    @Property("source_json")
    private String sourceJson;

    @Property("extra_json")
    private String extraJson;

    private String weight;

    @TargetNode
    private CompanyGraphEntity investor;
}

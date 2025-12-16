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

    @Property("event_type")
    private String eventType;

    @Property("rel_type")
    private String relType;

    @Property("extra_json")
    private String extraJson;

    @Property("source_json")
    private String sourceJson;

    @Property("news_id")
    private String newsId;

    @Property("rel_reason")
    private String relReason;

    @TargetNode
    private CompanyGraphEntity investor;
}

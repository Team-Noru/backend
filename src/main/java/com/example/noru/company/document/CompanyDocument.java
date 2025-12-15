package com.example.noru.company.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "company_index")
public class CompanyDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String companyId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;

    @Field(type = FieldType.Boolean)
    @JsonProperty("isDomestic")
    private boolean isDomestic;

    @Field(type = FieldType.Boolean)
    @JsonProperty("isListed")
    private boolean isListed;
}

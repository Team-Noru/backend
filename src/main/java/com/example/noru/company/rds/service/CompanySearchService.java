package com.example.noru.company.rds.service;

import com.example.noru.company.document.CompanyDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanySearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<CompanyDocument> searchByNameOrCode(String keyword) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s
                                        .match(m -> m
                                                .field("name")
                                                .query(keyword)
                                        )
                                )
                                .should(s -> s
                                        .wildcard(w -> w
                                                .field("companyId")
                                                .value("*" + keyword + "*")
                                        )
                                )
                                .minimumShouldMatch("1")
                        )
                )
                .build();

        SearchHits<CompanyDocument> searchHits = elasticsearchOperations.search(query, CompanyDocument.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}

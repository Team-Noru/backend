package com.example.noru.company.rds.repository;

import com.example.noru.company.document.CompanyDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanySearchRepository extends ElasticsearchRepository<CompanyDocument, Long> {

    // 기업명(name)이나 종목코드(stockCode)에 키워드가 포함되면 검색
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name\", \"companyId\"]}}")
    List<CompanyDocument> searchByNameOrCode(String keyword);
}

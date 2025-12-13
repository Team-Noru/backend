package com.example.noru.news.rds.repository;

import com.example.noru.news.document.NewsDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsSearchRepository extends ElasticsearchRepository<NewsDocument, Long> {

    // 제목으로 검색하는 메서드 예시 (나중에 쓸 것)
    List<NewsDocument> findByTitleContaining(String keyword);

    // 회사 코드로 검색
    List<NewsDocument> findByCompanyCode(String companyCode);

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"content\", \"companyName\"]}}")
    List<NewsDocument> searchByKeyword(String keyword);
}
package com.example.noru.news.rds.service;

import co.elastic.clients.elasticsearch.core.search.FieldCollapse;
import com.example.noru.company.rds.entity.Company;
import com.example.noru.company.rds.repository.CompanyRepository;
import com.example.noru.news.document.NewsDocument;
import com.example.noru.news.rds.dto.NewsEsDto;
import com.example.noru.news.rds.entity.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsSearchService {

    private final com.example.noru.news.repository.NewsSearchRepository newsSearchRepository;
    private final CompanyRepository companyRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 1. 통합 검색 (Global Search)
     * 제목(title), 본문(content), 기업명(companyName) 중 하나라도 키워드가 포함되면 반환합니다.
     * * @param keyword 검색어 (예: "삼성", "반도체")
     * @return 검색된 뉴스 리스트
     */
    @Transactional(readOnly = true)
    public List<NewsDocument> searchGlobal(String keyword) {

        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .fields("title^3", "content", "companyName")
                                .query(keyword)
                        )
                )
                .withFieldCollapse(FieldCollapse.of(f -> f.field("title.keyword")))
                .build();

        SearchHits<NewsDocument> searchHits = elasticsearchOperations.search(query, NewsDocument.class);

        return searchHits.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }

    /**
     * 2. 기업별 뉴스 조회
     * 특정 종목코드(companyCode)를 가진 뉴스만 필터링해서 반환합니다.
     * 사용자가 오른쪽 기업 리스트에서 특정 기업을 클릭했을 때 사용됩니다.
     * * @param companyCode 종목코드 (예: "005930")
     * @return 해당 기업의 뉴스 리스트
     */
    @Transactional(readOnly = true)
    public List<NewsDocument> searchByCompanyCode(String companyCode) {
        // Spring Data Elasticsearch가 메서드 이름을 보고 자동으로 쿼리를 생성합니다.
        return newsSearchRepository.findByCompanyCode(companyCode);
    }

    /**
     * [추가] Outbox 스케줄러가 호출하는 메서드
     * News 엔티티를 받아서 ES용 NewsDocument로 변환 후 저장
     */
    @Transactional
    public void saveToElasticsearch(NewsEsDto dto) {
        // 1. 기업명 조회 (오류 났던 부분 수정: parseLong 제거)
        String companyName = "기타";

        if (dto.getCompanyId() != null) {
            // String ID를 그대로 넘깁니다.
            companyName = companyRepository.findById(dto.getCompanyId())
                    .map(Company::getName)
                    .orElse("기타");
        }

        // 2. Document 변환
        NewsDocument document = NewsDocument.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .publisher(dto.getPublisher())
                .publishedAt(dto.getPublishedAt())
                .companyCode(dto.getCompanyId())
                .companyName(companyName)
                .thumbnailUrl(dto.getThumbnailUrl())
                .build();

        // 3. ES 저장
        newsSearchRepository.save(document);
    }
}

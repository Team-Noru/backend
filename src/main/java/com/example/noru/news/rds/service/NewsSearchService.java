package com.example.noru.news.rds.service;

import com.example.noru.news.document.NewsDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsSearchService {

    private final com.example.noru.news.repository.NewsSearchRepository newsSearchRepository;

    /**
     * 1. 통합 검색 (Global Search)
     * 제목(title), 본문(content), 기업명(companyName) 중 하나라도 키워드가 포함되면 반환합니다.
     * * @param keyword 검색어 (예: "삼성", "반도체")
     * @return 검색된 뉴스 리스트
     */
    @Transactional(readOnly = true)
    public List<NewsDocument> searchGlobal(String keyword) {
        // Repository에 @Query로 정의해둔 multi_match 쿼리를 실행합니다.
        return newsSearchRepository.searchByKeyword(keyword);
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
}

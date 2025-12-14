package com.example.noru.company.rds.repository;

import com.example.noru.company.rds.entity.WordCloud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordCloudRepository extends JpaRepository<WordCloud, Long> {
    List<WordCloud> findByCompanyId(String companyId);
}

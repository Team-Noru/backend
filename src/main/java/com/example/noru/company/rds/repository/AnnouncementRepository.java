package com.example.noru.company.rds.repository;

import com.example.noru.company.rds.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByCompanyIdOrderByPublishedAtDesc(String companyId);
}

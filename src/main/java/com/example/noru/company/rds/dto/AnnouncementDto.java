package com.example.noru.company.rds.dto;

import com.example.noru.company.rds.entity.Announcement;

public record AnnouncementDto(
        Long announcementId,
        String companyId,
        String category,
        String title,
        String announcementUrl,
        String publishedAt
) {
    public static AnnouncementDto fromEntity(Announcement a) {
        return new AnnouncementDto(
                a.getId(),
                a.getCompanyId(),
                a.getCategory().name(),
                a.getTitle(),
                a.getAnnouncementUrl(),
                a.getPublishedAt()
        );
    }
}

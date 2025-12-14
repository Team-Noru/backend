package com.example.noru.company.rds.service;

import com.example.noru.common.exception.CompanyException;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.company.rds.dto.AnnouncementDto;
import com.example.noru.company.rds.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    public List<AnnouncementDto> getAnnouncementsByCompany(String companyId) {

        List<AnnouncementDto> result = announcementRepository.findByCompanyIdOrderByPublishedAtDesc(companyId)
                .stream()
                .map(AnnouncementDto::fromEntity)
                .toList();

        if (result.isEmpty()) {
            throw new CompanyException(ResponseCode.ANNOUNCEMENT_NOT_FOUND);
        }

        return result;
    }

}

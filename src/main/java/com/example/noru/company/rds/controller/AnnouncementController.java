package com.example.noru.company.rds.controller;

import com.example.noru.common.response.ApiResponse;
import com.example.noru.common.response.ResponseCode;
import com.example.noru.company.rds.dto.AnnouncementDto;
import com.example.noru.company.rds.entity.Announcement;
import com.example.noru.company.rds.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/{companyId}/announcement")
    public ResponseEntity<?> getAnnouncements(@PathVariable String companyId) {
        List<AnnouncementDto> result = announcementService.getAnnouncementsByCompany(companyId);

        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_ANNOUNCEMENT, result));
    }
}

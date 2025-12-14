package com.example.noru.company.rds.dto;

import java.util.List;

public record WordCloudDto (
        String companyId,
        List<WordDto> wordList
){
}

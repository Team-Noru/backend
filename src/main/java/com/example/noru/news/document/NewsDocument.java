package com.example.noru.news.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "news_index")
public class NewsDocument {

    @Id
    private Long id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "nori"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String content;

    @Field(type = FieldType.Keyword) // 정확한 일치 검색용 (필터링)
    private String publisher;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime publishedAt;

    @Field(type = FieldType.Keyword) // 회사 코드로 필터링할 때 사용
    private String companyCode;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String companyName;

    @Field(type = FieldType.Text)
    private String url;

    @Field(type = FieldType.Text)
    private String thumbnailUrl;
}

package com.example.noru.company.rds.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "word_clouds")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WordCloud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="word_id")
    private long id;

    private String text;

    private int weight;

    private String type;

    private String companyId;


}

package com.example.noru.company.rds.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Companies")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String stockCode;

    private String name;

    private boolean isDomestic;

    private boolean isListed;
}

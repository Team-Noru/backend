package com.example.noru.company.rds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name="company_id")
    String companyId;

    String name;

    private boolean isDomestic;

    private boolean isListed;
}

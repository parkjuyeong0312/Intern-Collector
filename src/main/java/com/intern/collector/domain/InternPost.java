package com.intern.collector.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "intern_post")
@Getter
@NoArgsConstructor
public class InternPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String remoteId;          // Zighang UUID (중복 체크용)

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String companyName;

    private String regions;           // 콤마 구분 (예: "서울,경기")

    private String employeeTypes;     // 콤마 구분 (예: "전환형인턴")

    private String depthOnes;         // 직군 대분류

    private String keywords;          // 키워드 태그

    private String deadlineType;      // 채용시마감 / 상시채용 / 마감일

    private LocalDateTime endDate;

    @Column(columnDefinition = "TEXT")
    private String summary;           // AI 요약 결과

    @Column(nullable = false)
    private String originalUrl;

    private LocalDateTime createdAt;

    @Column(updatable = false)
    private LocalDateTime savedAt;

    @PrePersist
    protected void onCreate() {
        this.savedAt = LocalDateTime.now();
    }

    @Builder
    public InternPost(String remoteId, String title, String companyName,
                      String regions, String employeeTypes, String depthOnes,
                      String keywords, String deadlineType, LocalDateTime endDate,
                      String summary, String originalUrl, LocalDateTime createdAt) {
        this.remoteId = remoteId;
        this.title = title;
        this.companyName = companyName;
        this.regions = regions;
        this.employeeTypes = employeeTypes;
        this.depthOnes = depthOnes;
        this.keywords = keywords;
        this.deadlineType = deadlineType;
        this.endDate = endDate;
        this.summary = summary;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
    }

    public void updateSummary(String summary) {
        this.summary = summary;
    }
}

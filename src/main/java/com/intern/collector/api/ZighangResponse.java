package com.intern.collector.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZighangResponse {

    private boolean success;
    private PageData data;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageData {
        private List<RecruitItem> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecruitItem {
        private String id;
        private Company company;
        private String title;
        private List<String> regions;
        private List<String> employeeTypes;
        private List<String> depthOnes;
        private List<String> keywords;
        private String deadlineType;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Company {
        private String name;
        private String image;
    }
}

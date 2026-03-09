package com.intern.collector.scheduler;

import com.intern.collector.api.ZighangApiClient;
import com.intern.collector.api.ZighangResponse;
import com.intern.collector.domain.InternPost;
import com.intern.collector.domain.InternPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InternCollectScheduler {

    private static final String ZIGHANG_POST_URL = "https://zighang.com/recruitment/";

    private final ZighangApiClient zighangApiClient;
    private final InternPostRepository internPostRepository;

    @Scheduled(cron = "0 0 * * * *") // 매시 정각
    public void collectInternPosts() {
        log.info("=== 인턴 공고 수집 시작 ===");

        ZighangResponse firstPage = zighangApiClient.fetchInternPosts(0);
        if (firstPage == null || firstPage.getData() == null) {
            log.warn("Zighang API 응답 없음");
            return;
        }

        int totalPages = firstPage.getData().getTotalPages();
        List<InternPost> newPosts = new ArrayList<>(processPage(firstPage));

        // 최대 5페이지까지만 수집 (과도한 API 호출 방지)
        int pagesToFetch = Math.min(totalPages, 5);
        for (int page = 1; page < pagesToFetch; page++) {
            ZighangResponse response = zighangApiClient.fetchInternPosts(page);
            if (response != null && response.getData() != null) {
                newPosts.addAll(processPage(response));
            }
        }

        if (!newPosts.isEmpty()) {
            internPostRepository.saveAll(newPosts);
            log.info("신규 공고 {}건 저장 완료", newPosts.size());
        } else {
            log.info("신규 공고 없음");
        }

        log.info("=== 인턴 공고 수집 완료 ===");
    }

    private List<InternPost> processPage(ZighangResponse response) {
        return response.getData().getContent().stream()
                .filter(item -> !internPostRepository.existsByRemoteId(item.getId()))
                .map(item -> InternPost.builder()
                        .remoteId(item.getId())
                        .title(item.getTitle())
                        .companyName(item.getCompany().getName())
                        .regions(String.join(", ", item.getRegions()))
                        .employeeTypes(String.join(", ", item.getEmployeeTypes()))
                        .depthOnes(String.join(", ", item.getDepthOnes()))
                        .keywords(item.getKeywords() != null
                                ? String.join(", ", item.getKeywords()) : "")
                        .deadlineType(item.getDeadlineType())
                        .endDate(item.getEndDate())
                        .originalUrl(ZIGHANG_POST_URL + item.getId())
                        .createdAt(item.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

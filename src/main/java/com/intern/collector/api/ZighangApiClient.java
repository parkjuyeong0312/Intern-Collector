package com.intern.collector.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class ZighangApiClient {

    private final RestTemplate restTemplate;

    @Value("${zighang.api.base-url}")
    private String baseUrl;

    private static final String RECRUIT_PATH = "/recruitments/v3";
    private static final int PAGE_SIZE = 20;

    public ZighangResponse fetchInternPosts(int page) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + RECRUIT_PATH)
                .queryParam("employeeTypes", "전환형인턴")
                .queryParam("page", page)
                .queryParam("size", PAGE_SIZE)
                .build()
                .encode()
                .toUri();

        log.debug("Fetching Zighang API: {}", uri);
        return restTemplate.getForObject(uri, ZighangResponse.class);
    }
}

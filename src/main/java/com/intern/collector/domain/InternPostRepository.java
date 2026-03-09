package com.intern.collector.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InternPostRepository extends JpaRepository<InternPost, Long> {

    boolean existsByRemoteId(String remoteId);
}

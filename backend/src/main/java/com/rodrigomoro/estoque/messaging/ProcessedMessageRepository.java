package com.rodrigomoro.estoque.messaging;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage, Long> {
    boolean existsByEventId(String eventId);
}

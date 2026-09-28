package com.rodrigomoro.inventory.messaging;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OutboxEvent> findTop50ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}

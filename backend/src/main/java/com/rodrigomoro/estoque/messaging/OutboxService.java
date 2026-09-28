package com.rodrigomoro.estoque.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class OutboxService {

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public String enqueue(String eventType, String aggregateType, String aggregateId,
                          String routingKey, String correlationId, Map<String, Object> payload) {
        String eventId = UUID.randomUUID().toString();
        try {
            EventEnvelope envelope = new EventEnvelope(
                    eventId,
                    eventType,
                    aggregateType,
                    aggregateId,
                    Instant.now(),
                    "estoque-monolith",
                    1,
                    correlationId,
                    objectMapper.valueToTree(payload)
            );
            String json = objectMapper.writeValueAsString(envelope);
            repository.save(new OutboxEvent(
                    eventId, eventType, aggregateType, aggregateId, routingKey, json
            ));
            return eventId;
        } catch (Exception ex) {
            throw new IllegalStateException("Não foi possível criar o evento para o Outbox", ex);
        }
    }
}

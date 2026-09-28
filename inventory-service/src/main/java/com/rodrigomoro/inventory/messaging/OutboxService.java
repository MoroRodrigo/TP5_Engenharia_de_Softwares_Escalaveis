package com.rodrigomoro.inventory.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class OutboxService {
    private final OutboxRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxRepository repository, ObjectMapper objectMapper) {
        this.repository = repository; this.objectMapper = objectMapper;
    }

    @Transactional
    public String enqueue(String eventType, String aggregateType, String aggregateId,
                          String routingKey, String correlationId, Map<String,Object> payload) {
        String eventId=UUID.randomUUID().toString();
        try {
            EventEnvelope envelope=new EventEnvelope(eventId,eventType,aggregateType,aggregateId,Instant.now(),"inventory-service",1,correlationId,objectMapper.valueToTree(payload));
            repository.save(new OutboxEvent(eventId,eventType,aggregateType,aggregateId,routingKey,objectMapper.writeValueAsString(envelope)));
            return eventId;
        } catch(Exception ex){ throw new IllegalStateException("Não foi possível criar o evento do inventário",ex); }
    }
}

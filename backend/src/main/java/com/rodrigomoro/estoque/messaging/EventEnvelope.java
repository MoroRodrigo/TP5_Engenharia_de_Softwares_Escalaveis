package com.rodrigomoro.estoque.messaging;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

public record EventEnvelope(
        String eventId,
        String eventType,
        String aggregateType,
        String aggregateId,
        Instant occurredAt,
        String source,
        int version,
        String correlationId,
        JsonNode payload
) {}

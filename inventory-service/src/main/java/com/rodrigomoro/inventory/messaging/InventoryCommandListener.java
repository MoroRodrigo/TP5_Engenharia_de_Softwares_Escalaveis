package com.rodrigomoro.inventory.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoro.inventory.service.MovimentacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InventoryCommandListener {
    private static final Logger log = LoggerFactory.getLogger(InventoryCommandListener.class);

    private final ObjectMapper objectMapper;
    private final MovimentacaoService service;
    private final ProcessedMessageRepository processedRepository;

    public InventoryCommandListener(ObjectMapper objectMapper,
                                    MovimentacaoService service,
                                    ProcessedMessageRepository processedRepository) {
        this.objectMapper = objectMapper;
        this.service = service;
        this.processedRepository = processedRepository;
    }

    @RabbitListener(queues = RabbitMqConfig.INVENTORY_COMMAND_QUEUE)
    @Transactional
    public void receber(String json) {
        try {
            EventEnvelope envelope = objectMapper.readValue(json, EventEnvelope.class);
            log.info("Comando de estoque recebido eventId={} type={} correlationId={}",
                    envelope.eventId(), envelope.eventType(), envelope.correlationId());

            if (processedRepository.existsByEventId(envelope.eventId())) {
                log.info("Comando já processado eventId={}", envelope.eventId());
                return;
            }

            JsonNode payload = envelope.payload();
            service.processarComando(
                    envelope.eventId(),
                    envelope.correlationId(),
                    payload.get("produtoId").asLong(),
                    payload.get("tipo").asText(),
                    payload.get("quantidade").asInt(),
                    payload.get("motivo").asText()
            );

            processedRepository.save(new ProcessedMessage(envelope.eventId()));
            log.info("Comando de estoque processado eventId={}", envelope.eventId());
        } catch (Exception ex) {
            log.error("Falha ao processar comando de estoque", ex);
            throw new IllegalStateException("Falha ao processar comando de estoque", ex);
        }
    }
}

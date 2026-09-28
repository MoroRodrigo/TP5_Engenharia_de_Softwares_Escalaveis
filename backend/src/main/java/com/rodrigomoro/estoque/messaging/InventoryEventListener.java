package com.rodrigomoro.estoque.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoro.estoque.estoque.EstoqueMovimentacaoProjection;
import com.rodrigomoro.estoque.estoque.EstoqueMovimentacaoProjectionRepository;
import com.rodrigomoro.estoque.estoque.EstoqueSaldoProjection;
import com.rodrigomoro.estoque.estoque.EstoqueSaldoProjectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

    private final ObjectMapper objectMapper;
    private final EstoqueMovimentacaoProjectionRepository movimentacaoRepository;
    private final EstoqueSaldoProjectionRepository saldoRepository;
    private final ProcessedMessageRepository processedMessageRepository;

    public InventoryEventListener(ObjectMapper objectMapper,
                                  EstoqueMovimentacaoProjectionRepository movimentacaoRepository,
                                  EstoqueSaldoProjectionRepository saldoRepository,
                                  ProcessedMessageRepository processedMessageRepository) {
        this.objectMapper = objectMapper;
        this.movimentacaoRepository = movimentacaoRepository;
        this.saldoRepository = saldoRepository;
        this.processedMessageRepository = processedMessageRepository;
    }

    @RabbitListener(queues = RabbitMqConfig.MONOLITH_INVENTORY_EVENTS_QUEUE)
    @Transactional
    public void receber(String json) {
        try {
            EventEnvelope envelope = objectMapper.readValue(json, EventEnvelope.class);

            log.info("Evento de inventário recebido eventId={} type={} correlationId={}",
                    envelope.eventId(), envelope.eventType(), envelope.correlationId());

            if (processedMessageRepository.existsByEventId(envelope.eventId())) {
                log.info("Evento já processado eventId={}", envelope.eventId());
                return;
            }

            if ("MovimentacaoEstoqueRegistradaV1".equals(envelope.eventType())) {
                processarRegistro(envelope);
            } else if ("MovimentacaoEstoqueRejeitadaV1".equals(envelope.eventType())) {
                processarRejeicao(envelope);
            }

            processedMessageRepository.save(new ProcessedMessage(envelope.eventId()));
            log.info("Evento de inventário processado eventId={}", envelope.eventId());
        } catch (Exception ex) {
            log.error("Falha ao processar evento do inventário", ex);
            throw new IllegalStateException("Falha ao processar evento do inventário", ex);
        }
    }

    private void processarRegistro(EventEnvelope envelope) throws Exception {
        JsonNode payload = envelope.payload();
        EstoqueMovimentacaoProjection item = movimentacaoRepository
                .findByEventoId(payload.get("solicitacaoEventId").asText())
                .orElse(null);
        if (item != null) {
            item.registrarSucesso();
        }

        Long produtoId = payload.get("produtoId").asLong();
        Integer saldo = payload.get("saldoAtual").asInt();
        EstoqueSaldoProjection saldoProjection = saldoRepository.findByProdutoId(produtoId)
                .orElseGet(() -> new EstoqueSaldoProjection(produtoId, 0));
        saldoProjection.atualizarSaldo(saldo);
        saldoRepository.save(saldoProjection);
    }

    private void processarRejeicao(EventEnvelope envelope) throws Exception {
        JsonNode payload = envelope.payload();
        EstoqueMovimentacaoProjection item = movimentacaoRepository
                .findByEventoId(payload.get("solicitacaoEventId").asText())
                .orElse(null);
        if (item != null) {
            item.registrarErro(payload.get("motivo").asText());
        }
    }
}

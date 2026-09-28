package com.rodrigomoro.estoque.estoque;

import com.rodrigomoro.estoque.messaging.OutboxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class EstoqueCommandService {

    private final OutboxService outboxService;
    private final EstoqueMovimentacaoProjectionRepository movimentacaoRepository;

    public EstoqueCommandService(OutboxService outboxService,
                                 EstoqueMovimentacaoProjectionRepository movimentacaoRepository) {
        this.outboxService = outboxService;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public EstoqueCommandResponse solicitar(Long produtoId, MovimentacaoCommandRequest request) {
        String tipo = request.tipo().trim().toUpperCase();
        if (!tipo.equals("ENTRADA") && !tipo.equals("SAIDA")) {
            throw new IllegalArgumentException("Tipo deve ser ENTRADA ou SAIDA");
        }

        String correlationId = UUID.randomUUID().toString();
        String eventId = outboxService.enqueue(
                "EstoqueMovimentacaoSolicitadaV1",
                "Produto",
                produtoId.toString(),
                "estoque.movimentacao.solicitada.v1",
                correlationId,
                Map.of(
                        "produtoId", produtoId,
                        "tipo", tipo,
                        "quantidade", request.quantidade(),
                        "motivo", request.motivo() == null ? "" : request.motivo()
                )
        );

        movimentacaoRepository.save(new EstoqueMovimentacaoProjection(
                eventId, produtoId, tipo, request.quantidade(), request.motivo()
        ));

        return new EstoqueCommandResponse(
                eventId,
                StatusMovimentacao.PROCESSANDO.name(),
                "Movimentação enviada para processamento assíncrono"
        );
    }
}

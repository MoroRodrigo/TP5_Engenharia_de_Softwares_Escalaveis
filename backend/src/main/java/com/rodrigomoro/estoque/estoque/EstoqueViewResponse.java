package com.rodrigomoro.estoque.estoque;

import java.time.LocalDateTime;

public record EstoqueViewResponse(
        String eventId,
        Long produtoId,
        String tipo,
        Integer quantidade,
        String motivo,
        String status,
        LocalDateTime criadaEm,
        LocalDateTime processadaEm,
        String erro
) {
    public static EstoqueViewResponse from(EstoqueMovimentacaoProjection item) {
        return new EstoqueViewResponse(
                item.getEventoId(), item.getProdutoId(), item.getTipo(), item.getQuantidade(), item.getMotivo(),
                item.getStatus().name(), item.getCriadaEm(), item.getProcessadaEm(), item.getErro()
        );
    }
}

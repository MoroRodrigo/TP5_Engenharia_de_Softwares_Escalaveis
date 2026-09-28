package com.rodrigomoro.inventory.dto;

import com.rodrigomoro.inventory.entity.MovimentacaoEstoque;
import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        Long produtoId,
        String tipo,
        Integer quantidade,
        String motivo,
        LocalDateTime dataHora
) {
    public static MovimentacaoResponse from(MovimentacaoEstoque entity) {
        return new MovimentacaoResponse(
                entity.getId(),
                entity.getProdutoId(),
                entity.getTipo().name(),
                entity.getQuantidade(),
                entity.getMotivo(),
                entity.getDataHora()
        );
    }
}

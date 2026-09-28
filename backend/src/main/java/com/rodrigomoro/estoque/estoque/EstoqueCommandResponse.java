package com.rodrigomoro.estoque.estoque;

public record EstoqueCommandResponse(
        String eventId,
        String status,
        String mensagem
) {}

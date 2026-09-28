package com.rodrigomoro.estoque.estoque;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoCommandRequest(
        @NotBlank(message = "Tipo de movimentação é obrigatório") String tipo,
        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser maior que zero") Integer quantidade,
        String motivo
) {}

package com.rodrigomoro.inventory.service;

import com.rodrigomoro.inventory.entity.MovimentacaoEstoque;
import com.rodrigomoro.inventory.entity.TipoMovimentacao;
import com.rodrigomoro.inventory.messaging.OutboxService;
import com.rodrigomoro.inventory.repository.MovimentacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentacaoServiceTest {
    @Mock private MovimentacaoRepository repository;
    @Mock private OutboxService outboxService;

    @Test
    void deveProcessarEntradaEPublicarEventoDeSucesso() {
        MovimentacaoService service = new MovimentacaoService(repository, outboxService);
        MovimentacaoEstoque salvo = new MovimentacaoEstoque(10L, TipoMovimentacao.ENTRADA, 8, "Recebimento");
        salvo.setId(1L);
        when(repository.save(any(MovimentacaoEstoque.class))).thenReturn(salvo);
        when(repository.calcularSaldo(10L, TipoMovimentacao.ENTRADA)).thenReturn(8);

        service.processarComando("cmd-1", "corr-1", 10L, "ENTRADA", 8, "Recebimento");

        assertThat(salvo.getQuantidade()).isEqualTo(8);
        verify(outboxService).enqueue(
                eq("MovimentacaoEstoqueRegistradaV1"),
                eq("Produto"), eq("10"),
                eq("estoque.movimentacao.registrada.v1"),
                eq("corr-1"), any()
        );
    }
}

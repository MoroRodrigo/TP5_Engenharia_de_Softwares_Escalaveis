package com.rodrigomoro.estoque.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoro.estoque.estoque.EstoqueMovimentacaoProjection;
import com.rodrigomoro.estoque.estoque.EstoqueMovimentacaoProjectionRepository;
import com.rodrigomoro.estoque.estoque.EstoqueSaldoProjection;
import com.rodrigomoro.estoque.estoque.EstoqueSaldoProjectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryEventListenerTest {

    @Mock private EstoqueMovimentacaoProjectionRepository movimentacaoRepository;
    @Mock private EstoqueSaldoProjectionRepository saldoRepository;
    @Mock private ProcessedMessageRepository processedRepository;

    @Test
    void deveAtualizarProjecaoQuandoReceberEventoRegistrado() throws Exception {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        EstoqueMovimentacaoProjection item = new EstoqueMovimentacaoProjection("cmd-1", 7L, "ENTRADA", 10, "Reposicao");
        EstoqueSaldoProjection saldo = new EstoqueSaldoProjection(7L, 0);
        when(processedRepository.existsByEventId("evt-1")).thenReturn(false);
        when(movimentacaoRepository.findByEventoId("cmd-1")).thenReturn(Optional.of(item));
        when(saldoRepository.findByProdutoId(7L)).thenReturn(Optional.of(saldo));

        String json = mapper.writeValueAsString(new EventEnvelope(
                "evt-1", "MovimentacaoEstoqueRegistradaV1", "Produto", "7",
                Instant.now(), "inventory-service", 1, "corr-1",
                mapper.readTree("{\"solicitacaoEventId\":\"cmd-1\",\"produtoId\":7,\"saldoAtual\":10}")
        ));

        new InventoryEventListener(mapper, movimentacaoRepository, saldoRepository, processedRepository).receber(json);

        verify(movimentacaoRepository).findByEventoId("cmd-1");
        verify(saldoRepository).save(saldo);
        verify(processedRepository).save(any(ProcessedMessage.class));
    }
}

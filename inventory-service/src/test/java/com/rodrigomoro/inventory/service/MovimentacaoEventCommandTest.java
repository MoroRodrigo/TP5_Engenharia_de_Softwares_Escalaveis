package com.rodrigomoro.inventory.service;

import com.rodrigomoro.inventory.entity.MovimentacaoEstoque;
import com.rodrigomoro.inventory.entity.TipoMovimentacao;
import com.rodrigomoro.inventory.messaging.OutboxService;
import com.rodrigomoro.inventory.repository.MovimentacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentacaoEventCommandTest {
    @Mock private MovimentacaoRepository repository;
    @Mock private OutboxService outboxService;

    @Test
    void deveGerarEventoDeSucessoAposProcessarComando(){
        MovimentacaoService service=new MovimentacaoService(repository,outboxService);
        MovimentacaoEstoque entity=new MovimentacaoEstoque(7L,TipoMovimentacao.ENTRADA,10,"Reposicao");
        entity.setId(1L);
        when(repository.save(any(MovimentacaoEstoque.class))).thenReturn(entity);
        when(repository.calcularSaldo(7L,TipoMovimentacao.ENTRADA)).thenReturn(10);

        service.processarComando("cmd-1","corr-1",7L,"ENTRADA",10,"Reposicao");

        verify(outboxService).enqueue(eq("MovimentacaoEstoqueRegistradaV1"),eq("Produto"),eq("7"),eq("estoque.movimentacao.registrada.v1"),eq("corr-1"),any());
    }
}

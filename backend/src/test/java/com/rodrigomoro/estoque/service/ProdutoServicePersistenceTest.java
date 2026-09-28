package com.rodrigomoro.estoque.service;

import com.rodrigomoro.estoque.dto.ProdutoRequest;
import com.rodrigomoro.estoque.entity.Produto;
import com.rodrigomoro.estoque.messaging.ProdutoEventPublisher;
import com.rodrigomoro.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProdutoServicePersistenceTest {

    @Mock private ProdutoRepository repository;
    @Mock private ProdutoHistoricoService historicoService;
    @Mock private ProdutoEventPublisher eventPublisher;

    @Test
    void deveCriarProdutoEPublicarEvento() {
        Produto produto = new Produto("Notebook", "Dell", new BigDecimal("3200.00"), 10);
        produto.setId(1L);
        when(repository.save(any(Produto.class))).thenReturn(produto);

        ProdutoService service = new ProdutoService(repository, historicoService, eventPublisher);

        var response = service.criar(new ProdutoRequest("Notebook", "Dell", new BigDecimal("3200.00"), 10));

        assertThat(response.id()).isEqualTo(1L);
        verify(eventPublisher).publicarCriacao(org.mockito.ArgumentMatchers.eq(produto), org.mockito.ArgumentMatchers.anyString());
    }
}

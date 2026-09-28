package com.rodrigomoro.estoque.repository;

import com.rodrigomoro.estoque.entity.ProdutoHistorico;
import com.rodrigomoro.estoque.entity.TipoOperacaoHistorico;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProdutoHistoricoRepositoryTest {

    @Autowired
    private ProdutoHistoricoRepository repository;

    @Test
    void devePersistirEConsultarHistoricoPorProduto() {
        repository.save(new ProdutoHistorico(
                100L,
                TipoOperacaoHistorico.UPDATE,
                "Produto A",
                "Descricao A",
                new BigDecimal("10.00"),
                2,
                "Produto B",
                "Descricao B",
                new BigDecimal("12.00"),
                5,
                LocalDateTime.now()));

        var historicos = repository.findByProdutoIdOrderByDataHoraDescIdDesc(100L);

        assertThat(historicos).hasSize(1);
        assertThat(historicos.getFirst().getOperacao()).isEqualTo(TipoOperacaoHistorico.UPDATE);
        assertThat(historicos.getFirst().getNomeAnterior()).isEqualTo("Produto A");
        assertThat(historicos.getFirst().getNomeNovo()).isEqualTo("Produto B");
    }
}

package com.rodrigomoro.inventory.repository;

import com.rodrigomoro.inventory.entity.MovimentacaoEstoque;
import com.rodrigomoro.inventory.entity.TipoMovimentacao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MovimentacaoRepositoryTest {

    @Autowired
    private MovimentacaoRepository repository;

    @Test
    void deveCalcularSaldoDoProduto() {
        repository.save(new MovimentacaoEstoque(1L, TipoMovimentacao.ENTRADA, 20, "Inicial"));
        repository.save(new MovimentacaoEstoque(1L, TipoMovimentacao.SAIDA, 7, "Venda"));

        Integer saldo = repository.calcularSaldo(1L, TipoMovimentacao.ENTRADA);

        assertThat(saldo).isEqualTo(13);
    }

    @Test
    void deveListarHistoricoEmOrdemDecrescente() {
        repository.save(new MovimentacaoEstoque(2L, TipoMovimentacao.ENTRADA, 5, "A"));
        repository.save(new MovimentacaoEstoque(2L, TipoMovimentacao.ENTRADA, 3, "B"));

        var lista = repository.findByProdutoIdOrderByDataHoraDescIdDesc(2L);

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getId()).isGreaterThan(lista.get(1).getId());
    }
}

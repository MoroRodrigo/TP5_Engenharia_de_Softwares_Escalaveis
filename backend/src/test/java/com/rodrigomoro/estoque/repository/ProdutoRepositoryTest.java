package com.rodrigomoro.estoque.repository;

import com.rodrigomoro.estoque.entity.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    @Test
    void devePersistirEBuscarProdutoPorId() {
        Produto salvo = repository.save(new Produto(
                "Notebook",
                "Equipamento para estoque",
                new BigDecimal("3500.00"),
                10));

        assertThat(salvo.getId()).isNotNull();
        assertThat(repository.findById(salvo.getId())).isPresent();
        assertThat(repository.findById(salvo.getId()).orElseThrow().getNome()).isEqualTo("Notebook");
    }

    @Test
    void deveConsultarPorNomeIgnorandoMaiusculas() {
        repository.save(new Produto("Teclado Mecanico", "", new BigDecimal("250.00"), 5));
        repository.save(new Produto("Mouse", "", new BigDecimal("120.00"), 8));

        Page<Produto> page = repository.findByNomeContainingIgnoreCase("TECLADO", PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getNome()).isEqualTo("Teclado Mecanico");
    }

    @Test
    void deveConsultarProdutosComEstoqueAbaixoDoLimite() {
        repository.save(new Produto("Produto A", "", new BigDecimal("10.00"), 2));
        repository.save(new Produto("Produto B", "", new BigDecimal("20.00"), 10));

        Page<Produto> page = repository.findByQuantidadeLessThanEqual(2, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getQuantidade()).isEqualTo(2);
    }
}

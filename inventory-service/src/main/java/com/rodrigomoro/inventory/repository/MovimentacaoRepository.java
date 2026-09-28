package com.rodrigomoro.inventory.repository;

import com.rodrigomoro.inventory.entity.MovimentacaoEstoque;
import com.rodrigomoro.inventory.entity.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByProdutoIdOrderByDataHoraDescIdDesc(Long produtoId);

    @Query("""
        select coalesce(sum(
            case when m.tipo = :entrada then m.quantidade else -m.quantidade end
        ), 0)
        from MovimentacaoEstoque m
        where m.produtoId = :produtoId
        """)
    Integer calcularSaldo(
            @Param("produtoId") Long produtoId,
            @Param("entrada") TipoMovimentacao entrada);
}

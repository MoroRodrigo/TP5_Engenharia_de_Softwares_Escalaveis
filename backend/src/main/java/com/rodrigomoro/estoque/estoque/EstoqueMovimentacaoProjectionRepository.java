package com.rodrigomoro.estoque.estoque;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstoqueMovimentacaoProjectionRepository extends JpaRepository<EstoqueMovimentacaoProjection, Long> {
    List<EstoqueMovimentacaoProjection> findByProdutoIdOrderByCriadaEmDescIdDesc(Long produtoId);
    Optional<EstoqueMovimentacaoProjection> findByEventoId(String eventoId);
}

package com.rodrigomoro.estoque.estoque;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueSaldoProjectionRepository extends JpaRepository<EstoqueSaldoProjection, Long> {
    Optional<EstoqueSaldoProjection> findByProdutoId(Long produtoId);
}

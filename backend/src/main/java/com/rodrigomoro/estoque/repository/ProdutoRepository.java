package com.rodrigomoro.estoque.repository;

import com.rodrigomoro.estoque.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Produto> findByQuantidadeLessThanEqual(Integer quantidade, Pageable pageable);
}

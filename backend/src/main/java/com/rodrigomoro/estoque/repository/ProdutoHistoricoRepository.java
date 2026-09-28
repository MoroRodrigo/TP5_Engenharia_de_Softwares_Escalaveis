package com.rodrigomoro.estoque.repository;

import com.rodrigomoro.estoque.entity.ProdutoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoHistoricoRepository extends JpaRepository<ProdutoHistorico, Long> {
    List<ProdutoHistorico> findByProdutoIdOrderByDataHoraDescIdDesc(Long produtoId);
}

package com.rodrigomoro.estoque.dto;

import com.rodrigomoro.estoque.entity.Produto;

import java.math.BigDecimal;

public record ProdutoResponse(Long id, String nome, String descricao, BigDecimal preco, Integer quantidade, Long versao) {
    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getVersao());
    }
}

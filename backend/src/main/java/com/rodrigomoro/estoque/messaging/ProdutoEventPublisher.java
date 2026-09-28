package com.rodrigomoro.estoque.messaging;

import com.rodrigomoro.estoque.entity.Produto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProdutoEventPublisher {

    private final OutboxService outboxService;

    public ProdutoEventPublisher(OutboxService outboxService) {
        this.outboxService = outboxService;
    }

    public String publicarCriacao(Produto produto, String correlationId) {
        return outboxService.enqueue(
                "ProdutoCriadoV1",
                "Produto",
                produto.getId().toString(),
                "produto.criado.v1",
                correlationId,
                Map.of(
                        "produtoId", produto.getId(),
                        "nome", produto.getNome(),
                        "descricao", produto.getDescricao() == null ? "" : produto.getDescricao(),
                        "preco", produto.getPreco(),
                        "quantidadeInicial", produto.getQuantidade()
                )
        );
    }

    public String publicarAtualizacao(Produto produto, String correlationId) {
        return outboxService.enqueue(
                "ProdutoAtualizadoV1",
                "Produto",
                produto.getId().toString(),
                "produto.atualizado.v1",
                correlationId,
                Map.of(
                        "produtoId", produto.getId(),
                        "nome", produto.getNome(),
                        "descricao", produto.getDescricao() == null ? "" : produto.getDescricao(),
                        "preco", produto.getPreco(),
                        "quantidadeAtual", produto.getQuantidade()
                )
        );
    }

    public String publicarExclusao(Long produtoId, String correlationId) {
        return outboxService.enqueue(
                "ProdutoExcluidoV1",
                "Produto",
                produtoId.toString(),
                "produto.excluido.v1",
                correlationId,
                Map.of("produtoId", produtoId)
        );
    }
}

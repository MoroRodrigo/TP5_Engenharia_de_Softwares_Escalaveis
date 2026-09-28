package com.rodrigomoro.estoque.service;

import com.rodrigomoro.estoque.entity.Produto;
import com.rodrigomoro.estoque.entity.ProdutoHistorico;
import com.rodrigomoro.estoque.entity.TipoOperacaoHistorico;
import com.rodrigomoro.estoque.repository.ProdutoHistoricoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoHistoricoService {

    private final ProdutoHistoricoRepository repository;

    public ProdutoHistoricoService(ProdutoHistoricoRepository repository) {
        this.repository = repository;
    }

    public void registrarCriacao(Produto produto) {
        registrar(produto.getId(), TipoOperacaoHistorico.CREATE, null, produto);
    }

    public void registrarAtualizacao(Produto anterior, Produto novo) {
        registrar(novo.getId(), TipoOperacaoHistorico.UPDATE, anterior, novo);
    }

    public void registrarExclusao(Produto produto) {
        registrar(produto.getId(), TipoOperacaoHistorico.DELETE, produto, null);
    }

    public List<ProdutoHistorico> listarPorProduto(Long produtoId) {
        return repository.findByProdutoIdOrderByDataHoraDescIdDesc(produtoId);
    }

    private void registrar(Long produtoId, TipoOperacaoHistorico operacao, Produto anterior, Produto novo) {
        ProdutoHistorico historico = new ProdutoHistorico(
                produtoId,
                operacao,
                anterior != null ? anterior.getNome() : null,
                anterior != null ? anterior.getDescricao() : null,
                anterior != null ? anterior.getPreco() : null,
                anterior != null ? anterior.getQuantidade() : null,
                novo != null ? novo.getNome() : null,
                novo != null ? novo.getDescricao() : null,
                novo != null ? novo.getPreco() : null,
                novo != null ? novo.getQuantidade() : null,
                LocalDateTime.now());
        repository.save(historico);
    }
}

package com.rodrigomoro.estoque.service;

import com.rodrigomoro.estoque.dto.PageResponse;
import com.rodrigomoro.estoque.dto.ProdutoHistoricoResponse;
import com.rodrigomoro.estoque.dto.ProdutoRequest;
import com.rodrigomoro.estoque.dto.ProdutoResponse;
import com.rodrigomoro.estoque.entity.Produto;
import com.rodrigomoro.estoque.exception.RecursoNaoEncontradoException;
import com.rodrigomoro.estoque.messaging.ProdutoEventPublisher;
import com.rodrigomoro.estoque.service.ProdutoHistoricoService;
import com.rodrigomoro.estoque.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoHistoricoService historicoService;
    private final ProdutoEventPublisher eventPublisher;

    public ProdutoService(ProdutoRepository produtoRepository,
                          ProdutoHistoricoService historicoService,
                          ProdutoEventPublisher eventPublisher) {
        this.produtoRepository = produtoRepository;
        this.historicoService = historicoService;
        this.eventPublisher = eventPublisher;
    }

    public PageResponse<ProdutoResponse> listar(String nome, int page, int size) {
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        Page<Produto> produtos = nome == null || nome.isBlank()
                ? produtoRepository.findAll(pageable)
                : produtoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        return PageResponse.from(produtos.map(ProdutoResponse::fromEntity));
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        return ProdutoResponse.fromEntity(obterProduto(id));
    }

    public ProdutoResponse criar(ProdutoRequest request) {
        Produto produto = new Produto(request.nome(), request.descricao(), request.preco(), request.quantidade());
        Produto salvo = produtoRepository.save(produto);
        historicoService.registrarCriacao(salvo);
        eventPublisher.publicarCriacao(salvo, UUID.randomUUID().toString());
        return ProdutoResponse.fromEntity(salvo);
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = obterProduto(id);
        Produto anterior = copiar(produto);

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setQuantidade(request.quantidade());

        Produto salvo = produtoRepository.save(produto);
        historicoService.registrarAtualizacao(anterior, salvo);
        eventPublisher.publicarAtualizacao(salvo, UUID.randomUUID().toString());
        return ProdutoResponse.fromEntity(salvo);
    }

    public void excluir(Long id) {
        Produto produto = obterProduto(id);
        historicoService.registrarExclusao(produto);
        produtoRepository.delete(produto);
        eventPublisher.publicarExclusao(id, UUID.randomUUID().toString());
    }

    @Transactional(readOnly = true)
    public List<ProdutoHistoricoResponse> listarHistorico(Long produtoId) {
        return historicoService.listarPorProduto(produtoId).stream()
                .map(ProdutoHistoricoResponse::fromEntity)
                .toList();
    }

    private Produto obterProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontrado: " + id));
    }

    private Produto copiar(Produto origem) {
        Produto copia = new Produto(origem.getNome(), origem.getDescricao(), origem.getPreco(), origem.getQuantidade());
        copia.setId(origem.getId());
        copia.setVersao(origem.getVersao());
        return copia;
    }
}

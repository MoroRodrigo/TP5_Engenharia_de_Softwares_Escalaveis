package com.rodrigomoro.estoque.estoque;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueQueryService {
    private final EstoqueSaldoProjectionRepository saldoRepository;
    private final EstoqueMovimentacaoProjectionRepository movimentacaoRepository;

    public EstoqueQueryService(EstoqueSaldoProjectionRepository saldoRepository,
                               EstoqueMovimentacaoProjectionRepository movimentacaoRepository) {
        this.saldoRepository = saldoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional(readOnly = true)
    public SaldoEstoqueViewResponse consultarSaldo(Long produtoId) {
        return new SaldoEstoqueViewResponse(
                produtoId,
                saldoRepository.findByProdutoId(produtoId)
                        .map(EstoqueSaldoProjection::getSaldoAtual)
                        .orElse(0)
        );
    }

    @Transactional(readOnly = true)
    public List<EstoqueViewResponse> listarMovimentacoes(Long produtoId) {
        return movimentacaoRepository.findByProdutoIdOrderByCriadaEmDescIdDesc(produtoId)
                .stream()
                .map(EstoqueViewResponse::from)
                .toList();
    }
}

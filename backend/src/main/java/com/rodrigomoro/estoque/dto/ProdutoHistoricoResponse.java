package com.rodrigomoro.estoque.dto;

import com.rodrigomoro.estoque.entity.ProdutoHistorico;
import com.rodrigomoro.estoque.entity.TipoOperacaoHistorico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoHistoricoResponse(
        Long id,
        Long produtoId,
        TipoOperacaoHistorico operacao,
        String nomeAnterior,
        String descricaoAnterior,
        BigDecimal precoAnterior,
        Integer quantidadeAnterior,
        String nomeNovo,
        String descricaoNovo,
        BigDecimal precoNovo,
        Integer quantidadeNova,
        LocalDateTime dataHora) {

    public static ProdutoHistoricoResponse fromEntity(ProdutoHistorico historico) {
        return new ProdutoHistoricoResponse(
                historico.getId(), historico.getProdutoId(), historico.getOperacao(),
                historico.getNomeAnterior(), historico.getDescricaoAnterior(), historico.getPrecoAnterior(),
                historico.getQuantidadeAnterior(), historico.getNomeNovo(), historico.getDescricaoNovo(),
                historico.getPrecoNovo(), historico.getQuantidadeNova(), historico.getDataHora());
    }
}

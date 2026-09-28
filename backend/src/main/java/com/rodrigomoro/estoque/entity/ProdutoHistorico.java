package com.rodrigomoro.estoque.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "produto_historico",
        indexes = {
                @Index(name = "idx_historico_produto_data", columnList = "produto_id,data_hora")
        }
)
public class ProdutoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoOperacaoHistorico operacao;

    @Column(name = "nome_anterior", length = 120)
    private String nomeAnterior;

    @Column(name = "descricao_anterior", length = 500)
    private String descricaoAnterior;

    @Column(name = "preco_anterior", precision = 15, scale = 2)
    private BigDecimal precoAnterior;

    @Column(name = "quantidade_anterior")
    private Integer quantidadeAnterior;

    @Column(name = "nome_novo", length = 120)
    private String nomeNovo;

    @Column(name = "descricao_novo", length = 500)
    private String descricaoNovo;

    @Column(name = "preco_novo", precision = 15, scale = 2)
    private BigDecimal precoNovo;

    @Column(name = "quantidade_nova")
    private Integer quantidadeNova;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public ProdutoHistorico() {
    }

    public ProdutoHistorico(
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
        this.produtoId = produtoId;
        this.operacao = operacao;
        this.nomeAnterior = nomeAnterior;
        this.descricaoAnterior = descricaoAnterior;
        this.precoAnterior = precoAnterior;
        this.quantidadeAnterior = quantidadeAnterior;
        this.nomeNovo = nomeNovo;
        this.descricaoNovo = descricaoNovo;
        this.precoNovo = precoNovo;
        this.quantidadeNova = quantidadeNova;
        this.dataHora = dataHora;
    }

    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public TipoOperacaoHistorico getOperacao() { return operacao; }
    public String getNomeAnterior() { return nomeAnterior; }
    public String getDescricaoAnterior() { return descricaoAnterior; }
    public BigDecimal getPrecoAnterior() { return precoAnterior; }
    public Integer getQuantidadeAnterior() { return quantidadeAnterior; }
    public String getNomeNovo() { return nomeNovo; }
    public String getDescricaoNovo() { return descricaoNovo; }
    public BigDecimal getPrecoNovo() { return precoNovo; }
    public Integer getQuantidadeNova() { return quantidadeNova; }
    public LocalDateTime getDataHora() { return dataHora; }
}

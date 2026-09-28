package com.rodrigomoro.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes_estoque", indexes = {
        @Index(name = "idx_mov_produto_data", columnList = "produto_id,data_hora")
})
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 255)
    private String motivo;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    protected MovimentacaoEstoque() {
    }

    public MovimentacaoEstoque(Long produtoId, TipoMovimentacao tipo, Integer quantidade, String motivo) {
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.dataHora = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProdutoId() { return produtoId; }
    public TipoMovimentacao getTipo() { return tipo; }
    public Integer getQuantidade() { return quantidade; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getDataHora() { return dataHora; }
}

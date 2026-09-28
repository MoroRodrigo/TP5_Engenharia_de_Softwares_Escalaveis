package com.rodrigomoro.estoque.estoque;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "estoque_movimentacoes_view", indexes = {
        @Index(name = "idx_estoque_view_produto_data", columnList = "produto_id,criada_em")
})
public class EstoqueMovimentacaoProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evento_id", nullable = false, unique = true, length = 36)
    private String eventoId;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 255)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMovimentacao status;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    @Column(name = "processada_em")
    private LocalDateTime processadaEm;

    @Column(name = "erro", length = 500)
    private String erro;

    protected EstoqueMovimentacaoProjection() {}

    public EstoqueMovimentacaoProjection(String eventoId, Long produtoId, String tipo, Integer quantidade, String motivo) {
        this.eventoId = eventoId;
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.status = StatusMovimentacao.PROCESSANDO;
        this.criadaEm = LocalDateTime.now();
    }

    public void registrarSucesso() {
        status = StatusMovimentacao.REGISTRADA;
        processadaEm = LocalDateTime.now();
        erro = null;
    }

    public void registrarErro(String mensagem) {
        status = StatusMovimentacao.REJEITADA;
        processadaEm = LocalDateTime.now();
        erro = mensagem;
    }

    public Long getId() { return id; }
    public String getEventoId() { return eventoId; }
    public Long getProdutoId() { return produtoId; }
    public String getTipo() { return tipo; }
    public Integer getQuantidade() { return quantidade; }
    public String getMotivo() { return motivo; }
    public StatusMovimentacao getStatus() { return status; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public LocalDateTime getProcessadaEm() { return processadaEm; }
    public String getErro() { return erro; }
}

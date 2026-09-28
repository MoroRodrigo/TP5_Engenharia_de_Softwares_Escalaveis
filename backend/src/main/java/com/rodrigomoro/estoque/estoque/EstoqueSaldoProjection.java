package com.rodrigomoro.estoque.estoque;

import jakarta.persistence.*;

@Entity
@Table(name = "estoque_saldo_view", indexes = {
        @Index(name = "idx_estoque_saldo_produto", columnList = "produto_id")
})
public class EstoqueSaldoProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false, unique = true)
    private Long produtoId;

    @Column(name = "saldo_atual", nullable = false)
    private Integer saldoAtual;

    protected EstoqueSaldoProjection() {}

    public EstoqueSaldoProjection(Long produtoId, Integer saldoAtual) {
        this.produtoId = produtoId;
        this.saldoAtual = saldoAtual;
    }

    public void atualizarSaldo(Integer saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public Integer getSaldoAtual() { return saldoAtual; }
}

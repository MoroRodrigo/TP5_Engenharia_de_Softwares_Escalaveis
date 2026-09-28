package com.rodrigomoro.estoque.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(
        name = "produtos",
        indexes = {
                @Index(name = "idx_produto_nome", columnList = "nome"),
                @Index(name = "idx_produto_estoque", columnList = "quantidade")
        }
)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String nome;

    @Size(max = 500)
    @Column(length = 500)
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer quantidade;

    @Version
    @Column(nullable = false)
    private Long versao;

    public Produto() {
    }

    public Produto(String nome, String descricao, BigDecimal preco, Integer quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public Integer getQuantidade() { return quantidade; }
    public Long getVersao() { return versao; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public void setVersao(Long versao) { this.versao = versao; }
}

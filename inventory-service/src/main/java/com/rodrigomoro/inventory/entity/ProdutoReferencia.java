package com.rodrigomoro.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="produtos_referencia")
public class ProdutoReferencia {
    @Id
    private Long id;
    @Column(nullable=false,length=120) private String nome;
    @Column(precision=15,scale=2) private BigDecimal preco;
    @Column(nullable=false) private boolean ativo;
    @Column(name="atualizado_em",nullable=false) private LocalDateTime atualizadoEm;
    protected ProdutoReferencia(){}
    public ProdutoReferencia(Long id,String nome,BigDecimal preco,boolean ativo){this.id=id;this.nome=nome;this.preco=preco;this.ativo=ativo;this.atualizadoEm=LocalDateTime.now();}
    public void atualizar(String nome,BigDecimal preco){this.nome=nome;this.preco=preco;this.ativo=true;this.atualizadoEm=LocalDateTime.now();}
    public void desativar(){this.ativo=false;this.atualizadoEm=LocalDateTime.now();}
    public Long getId(){return id;} public String getNome(){return nome;} public BigDecimal getPreco(){return preco;} public boolean isAtivo(){return ativo;} public LocalDateTime getAtualizadoEm(){return atualizadoEm;}
}

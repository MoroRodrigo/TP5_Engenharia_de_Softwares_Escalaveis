package com.rodrigomoro.inventory.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(Long produtoId, Integer saldoAtual, Integer solicitado) {
        super("Estoque insuficiente para o produto " + produtoId + ". Saldo atual: " + saldoAtual + ", solicitado: " + solicitado);
    }
}

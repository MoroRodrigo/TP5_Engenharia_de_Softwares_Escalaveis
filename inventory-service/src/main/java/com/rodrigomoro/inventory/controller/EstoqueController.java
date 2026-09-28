package com.rodrigomoro.inventory.controller;

import com.rodrigomoro.inventory.dto.MovimentacaoResponse;
import com.rodrigomoro.inventory.dto.SaldoResponse;
import com.rodrigomoro.inventory.service.MovimentacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {
    private final MovimentacaoService service;

    public EstoqueController(MovimentacaoService service) {
        this.service = service;
    }

    @GetMapping("/produtos/{produtoId}/saldo")
    public ResponseEntity<SaldoResponse> saldo(@PathVariable Long produtoId) {
        return ResponseEntity.ok(service.consultarSaldo(produtoId));
    }

    @GetMapping("/produtos/{produtoId}/movimentacoes")
    public ResponseEntity<List<MovimentacaoResponse>> movimentacoes(@PathVariable Long produtoId) {
        return ResponseEntity.ok(service.listarMovimentacoes(produtoId));
    }
}

package com.rodrigomoro.estoque.controller;

import com.rodrigomoro.estoque.dto.PageResponse;
import com.rodrigomoro.estoque.dto.ProdutoHistoricoResponse;
import com.rodrigomoro.estoque.dto.ProdutoRequest;
import com.rodrigomoro.estoque.dto.ProdutoResponse;
import com.rodrigomoro.estoque.estoque.*;
import com.rodrigomoro.estoque.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final EstoqueCommandService estoqueCommandService;
    private final EstoqueQueryService estoqueQueryService;

    public ProdutoController(ProdutoService service,
                             EstoqueCommandService estoqueCommandService,
                             EstoqueQueryService estoqueQueryService) {
        this.service = service;
        this.estoqueCommandService = estoqueCommandService;
        this.estoqueQueryService = estoqueQueryService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProdutoResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.listar(nome, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(
            @Valid @RequestBody ProdutoRequest request,
            UriComponentsBuilder uriBuilder) {
        ProdutoResponse response = service.criar(request);
        URI location = uriBuilder.path("/api/produtos/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<ProdutoHistoricoResponse>> historico(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarHistorico(id));
    }

    @GetMapping("/{id}/estoque")
    public ResponseEntity<SaldoEstoqueViewResponse> estoque(@PathVariable Long id) {
        service.buscarPorId(id);
        return ResponseEntity.ok(estoqueQueryService.consultarSaldo(id));
    }

    @GetMapping("/{id}/movimentacoes")
    public ResponseEntity<List<EstoqueViewResponse>> movimentacoes(@PathVariable Long id) {
        service.buscarPorId(id);
        return ResponseEntity.ok(estoqueQueryService.listarMovimentacoes(id));
    }

    @PostMapping("/{id}/movimentacoes")
    public ResponseEntity<EstoqueCommandResponse> movimentar(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoCommandRequest request) {
        service.buscarPorId(id);
        return ResponseEntity.accepted().body(estoqueCommandService.solicitar(id, request));
    }
}

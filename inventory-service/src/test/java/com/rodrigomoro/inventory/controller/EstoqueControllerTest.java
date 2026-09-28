package com.rodrigomoro.inventory.controller;

import com.rodrigomoro.inventory.dto.MovimentacaoResponse;
import com.rodrigomoro.inventory.dto.SaldoResponse;
import com.rodrigomoro.inventory.service.MovimentacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstoqueController.class)
class EstoqueControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private MovimentacaoService service;

    @Test
    void deveExporSaldoViaRest() throws Exception {
        when(service.consultarSaldo(5L)).thenReturn(new SaldoResponse(5L, 14));
        mockMvc.perform(get("/api/estoque/produtos/5/saldo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(5))
                .andExpect(jsonPath("$.saldoAtual").value(14));
    }

    @Test
    void deveExporConsultasDeMovimentacoesViaRest() throws Exception {
        when(service.listarMovimentacoes(5L)).thenReturn(List.of(
                new MovimentacaoResponse(1L, 5L, "ENTRADA", 5, "Compra", LocalDateTime.now())
        ));

        mockMvc.perform(get("/api/estoque/produtos/5/movimentacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].produtoId").value(5))
                .andExpect(jsonPath("$[0].tipo").value("ENTRADA"))
                .andExpect(jsonPath("$[0].quantidade").value(5));
    }
}

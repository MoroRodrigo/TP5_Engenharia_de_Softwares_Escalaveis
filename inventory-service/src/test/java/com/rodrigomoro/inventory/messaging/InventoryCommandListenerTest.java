package com.rodrigomoro.inventory.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoro.inventory.service.MovimentacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryCommandListenerTest {
    @Mock private MovimentacaoService service;
    @Mock private ProcessedMessageRepository processedRepository;

    @Test
    void deveEncaminharComandoParaServicoERegistrarProcessamento() throws Exception {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        when(processedRepository.existsByEventId("cmd-1")).thenReturn(false);

        String json = mapper.writeValueAsString(new EventEnvelope(
                "cmd-1", "EstoqueMovimentacaoSolicitadaV1", "Produto", "9",
                Instant.now(), "estoque-monolith", 1, "corr-9",
                mapper.readTree("{\"produtoId\":9,\"tipo\":\"ENTRADA\",\"quantidade\":5,\"motivo\":\"Reposicao\"}")
        ));

        new InventoryCommandListener(mapper, service, processedRepository).receber(json);

        verify(service).processarComando(eq("cmd-1"), eq("corr-9"), eq(9L), eq("ENTRADA"), eq(5), eq("Reposicao"));
        verify(processedRepository).save(any(ProcessedMessage.class));
    }
}

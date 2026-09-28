package com.rodrigomoro.inventory.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoro.inventory.entity.ProdutoReferencia;
import com.rodrigomoro.inventory.repository.ProdutoReferenciaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class ProdutoEventListener {
    private final ObjectMapper objectMapper;
    private final ProdutoReferenciaRepository repository;
    private final ProcessedMessageRepository processedRepository;

    public ProdutoEventListener(ObjectMapper objectMapper,ProdutoReferenciaRepository repository,ProcessedMessageRepository processedRepository){
        this.objectMapper=objectMapper;this.repository=repository;this.processedRepository=processedRepository;
    }

    @RabbitListener(queues = RabbitMqConfig.INVENTORY_PRODUCT_QUEUE)
    @Transactional
    public void receber(String json){
        try{
            EventEnvelope envelope=objectMapper.readValue(json,EventEnvelope.class);
            if(processedRepository.existsByEventId(envelope.eventId())) return;
            JsonNode p=envelope.payload();
            Long produtoId=p.get("produtoId").asLong();
            ProdutoReferencia referencia=repository.findById(produtoId).orElse(null);
            switch(envelope.eventType()){
                case "ProdutoCriadoV1" -> {
                    BigDecimal preco=p.get("preco").decimalValue();
                    repository.save(referencia==null?new ProdutoReferencia(produtoId,p.get("nome").asText(),preco,true):referencia);
                }
                case "ProdutoAtualizadoV1" -> {
                    if(referencia==null) referencia=new ProdutoReferencia(produtoId,p.get("nome").asText(),p.get("preco").decimalValue(),true);
                    else referencia.atualizar(p.get("nome").asText(),p.get("preco").decimalValue());
                    repository.save(referencia);
                }
                case "ProdutoExcluidoV1" -> {
                    if(referencia!=null){referencia.desativar();repository.save(referencia);}
                }
                default -> { }
            }
            processedRepository.save(new ProcessedMessage(envelope.eventId()));
        }catch(Exception ex){throw new IllegalStateException("Falha ao sincronizar evento de produto",ex);}
    }
}

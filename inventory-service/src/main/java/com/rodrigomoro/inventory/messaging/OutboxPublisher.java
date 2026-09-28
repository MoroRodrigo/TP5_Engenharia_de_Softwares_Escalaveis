package com.rodrigomoro.inventory.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxPublisher {
    private static final Logger log=LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxRepository repository; private final RabbitTemplate rabbitTemplate;
    public OutboxPublisher(OutboxRepository repository,RabbitTemplate rabbitTemplate){this.repository=repository;this.rabbitTemplate=rabbitTemplate;}
    @Scheduled(fixedDelay=1000)
    @Transactional
    public void publicarPendentes(){
        List<OutboxEvent> pendentes=repository.findTop50ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);
        for(OutboxEvent evento:pendentes){
            try{
                rabbitTemplate.convertAndSend(RabbitMqConfig.ESTOQUE_EVENTS_EXCHANGE,evento.getRoutingKey(),evento.getPayload());
                evento.marcarPublicado();
                log.info("Evento de estoque publicado eventId={} type={}",evento.getEventId(),evento.getEventType());
            }catch(Exception ex){evento.registrarFalha(ex.getMessage());log.warn("Falha ao publicar eventId={}: {}",evento.getEventId(),ex.getMessage());}
        }
    }
}

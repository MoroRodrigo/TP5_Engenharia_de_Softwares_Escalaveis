package com.rodrigomoro.inventory.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String PRODUTO_EVENTS_EXCHANGE = "produtos.events";
    public static final String ESTOQUE_COMMANDS_EXCHANGE = "estoque.commands";
    public static final String ESTOQUE_EVENTS_EXCHANGE = "estoque.events";
    public static final String DEAD_LETTER_EXCHANGE = "dead-letter.exchange";

    public static final String INVENTORY_COMMAND_QUEUE = "inventory.commands.movimentacao";
    public static final String INVENTORY_PRODUCT_QUEUE = "inventory.products.sync";
    public static final String INVENTORY_COMMAND_DLQ = "inventory.commands.dlq";
    public static final String INVENTORY_PRODUCT_DLQ = "inventory.products.dlq";

    @Bean TopicExchange produtosEventsExchange() { return new TopicExchange(PRODUTO_EVENTS_EXCHANGE, true, false); }
    @Bean DirectExchange estoqueCommandsExchange() { return new DirectExchange(ESTOQUE_COMMANDS_EXCHANGE, true, false); }
    @Bean TopicExchange estoqueEventsExchange() { return new TopicExchange(ESTOQUE_EVENTS_EXCHANGE, true, false); }
    @Bean DirectExchange deadLetterExchange() { return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false); }

    @Bean Queue inventoryCommandQueue() {
        return QueueBuilder.durable(INVENTORY_COMMAND_QUEUE)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(INVENTORY_COMMAND_DLQ)
                .build();
    }

    @Bean Queue inventoryProductQueue() {
        return QueueBuilder.durable(INVENTORY_PRODUCT_QUEUE)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(INVENTORY_PRODUCT_DLQ)
                .build();
    }

    @Bean Queue inventoryCommandDlq() { return QueueBuilder.durable(INVENTORY_COMMAND_DLQ).build(); }
    @Bean Queue inventoryProductDlq() { return QueueBuilder.durable(INVENTORY_PRODUCT_DLQ).build(); }

    @Bean Binding inventoryCommandBinding() {
        return BindingBuilder.bind(inventoryCommandQueue())
                .to(estoqueCommandsExchange())
                .with("estoque.movimentacao.solicitada.v1");
    }

    @Bean Binding inventoryProductBinding() {
        return BindingBuilder.bind(inventoryProductQueue())
                .to(produtosEventsExchange())
                .with("produto.*.v1");
    }

    @Bean Binding inventoryCommandDlqBinding() {
        return BindingBuilder.bind(inventoryCommandDlq()).to(deadLetterExchange()).with(INVENTORY_COMMAND_DLQ);
    }

    @Bean Binding inventoryProductDlqBinding() {
        return BindingBuilder.bind(inventoryProductDlq()).to(deadLetterExchange()).with(INVENTORY_PRODUCT_DLQ);
    }
}

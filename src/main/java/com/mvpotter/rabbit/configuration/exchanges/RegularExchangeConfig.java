package com.mvpotter.rabbit.configuration.exchanges;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegularExchangeConfig {

    public static final String EXCHANGE = "exchange";

    public static final String QUEUE = EXCHANGE + ".queue";

    @Bean
    public Declarables regularDeclarables() {
        return new Declarables(
            new TopicExchange(EXCHANGE),
            QueueBuilder.durable(QUEUE)
                        .deadLetterExchange(DeadLetterExchangeConfig.EXCHANGE)
                        .deadLetterRoutingKey(QUEUE)
                        .build(),
            new Binding(QUEUE, Binding.DestinationType.QUEUE, EXCHANGE, "#", null)
        );

    }

}

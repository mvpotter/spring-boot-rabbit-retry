package com.mvpotter.rabbit.configuration.exchanges;

import com.mvpotter.rabbit.configuration.RabbitMqRetryProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadLetterExchangeConfig {

    public static final String EXCHANGE = "DLX";

    public static final String QUEUE = EXCHANGE + "." + RegularExchangeConfig.QUEUE + ".dlq";

    @Bean
    public Declarables deadLetterDeclarables(RabbitMqRetryProperties retryProperties) {
        return new Declarables(
            new DirectExchange(EXCHANGE),
            QueueBuilder.durable(QUEUE)
                        .ttl((int) retryProperties.getDelay().toMillis())
                        .deadLetterExchange("")
                        .build(),
            new Binding(QUEUE, Binding.DestinationType.QUEUE, EXCHANGE, RegularExchangeConfig.QUEUE, null)
        );

    }

}

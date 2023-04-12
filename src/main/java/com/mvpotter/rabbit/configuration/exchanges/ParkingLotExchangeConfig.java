package com.mvpotter.rabbit.configuration.exchanges;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParkingLotExchangeConfig {

    public static final String EXCHANGE = "parking-lot";

    public static final String QUEUE = EXCHANGE + "." + RegularExchangeConfig.QUEUE;

    @Bean
    public Declarables parkingLotDeclarables() {
        return new Declarables(
            new TopicExchange(EXCHANGE),
            new Queue(QUEUE),
            new Binding(QUEUE, Binding.DestinationType.QUEUE, EXCHANGE, "#", null)
        );
    }

}

package com.mvpotter.rabbit.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.DirectRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMqConfiguration {

    @Bean
    public RabbitTemplate rabbitTemplate(final CachingConnectionFactory cachingConnectionFactory,
                                         final ObjectMapper objectMapper) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public DirectRabbitListenerContainerFactory rabbitListenerContainerFactory(
            final CachingConnectionFactory cachingConnectionFactory,
            final DirectRabbitListenerContainerFactoryConfigurer configurer
    ) {
        final DirectRabbitListenerContainerFactory factory = new DirectRabbitListenerContainerFactory();
        configurer.configure(factory, cachingConnectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

}

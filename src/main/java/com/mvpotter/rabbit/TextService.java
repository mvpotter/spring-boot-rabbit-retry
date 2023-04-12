package com.mvpotter.rabbit;

import com.mvpotter.rabbit.configuration.exchanges.RegularExchangeConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class TextService {

    private final RabbitTemplate rabbitTemplate;

    private String response;

    public TextService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String text) {
        rabbitTemplate.convertAndSend(RegularExchangeConfig.EXCHANGE, "", new Event(text));
    }

    public void handleResponse(String text) {
        this.response = text;
    }

    public String getResponse() {
        return this.response;
    }

}

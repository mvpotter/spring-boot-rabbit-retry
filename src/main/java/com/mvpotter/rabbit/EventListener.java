package com.mvpotter.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvpotter.rabbit.configuration.RabbitMqRetryProperties;
import com.mvpotter.rabbit.configuration.exchanges.ParkingLotExchangeConfig;
import com.mvpotter.rabbit.configuration.exchanges.RegularExchangeConfig;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    private final ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;

    private final TextService textService;

    private final RabbitMqRetryProperties retryProperties;

    public EventListener(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate, TextService textService,
                         RabbitMqRetryProperties retryProperties) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.textService = textService;
        this.retryProperties = retryProperties;
    }

    @RabbitListener(queues = RegularExchangeConfig.QUEUE)
    public void onEvent(Message message) {
        long attempt = getAttempt(message);
        try {
            Event event = objectMapper.readValue(message.getBody(), Event.class);
            String text = event.getText();
            LOGGER.info("Received event with text = {}, attempt = {}", text, attempt);
            if ("do fail".equalsIgnoreCase(text)) {
                throw new IllegalStateException("Do fail!");
            }
            textService.handleResponse(text);
        } catch (Exception e) {
            handleError(message, e, attempt);
        }
    }

    private void handleError(Message message, Exception e, long attempt) {
        if (attempt >= retryProperties.getMaxAttempts()) {
            LOGGER.info("Move event to parking lot");
            rabbitTemplate.send(ParkingLotExchangeConfig.EXCHANGE, RegularExchangeConfig.QUEUE, message);
        } else {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private long getAttempt(Message message) {
        List<Map<String, Object>> deathHeaders
            = (List<Map<String, Object>>) message.getMessageProperties().getHeaders().get("x-death");
        if (deathHeaders == null) {
            return 0;
        } else {
            return deathHeaders.stream()
                               .map(entry -> (Long) entry.get("count"))
                               .filter(Objects::nonNull)
                               .reduce(0L, Math::max);
        }
    }

}

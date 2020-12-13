package it.pipitone.matteo.spike.rabbitmq.messages;

import it.pipitone.matteo.spike.rabbitmq.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class SimpleConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleConsumer.class);

    @RabbitListener(queues = "messages.queue")
    public void handleMessage(String message){
        LOG.info("Message received {}", message);
        throw new BusinessException();
    }
}

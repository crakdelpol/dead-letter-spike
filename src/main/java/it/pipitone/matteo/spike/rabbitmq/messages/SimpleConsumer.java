package it.pipitone.matteo.spike.rabbitmq.messages;

import it.pipitone.matteo.spike.rabbitmq.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;


public class SimpleConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleConsumer.class);

    @RabbitListener(bindings = @QueueBinding(
            key = "simple.message",
            value =
                @Queue(
                        value = "messages.queue",
                        durable = "false",
                        arguments = {
                                @Argument(name = "x-dead-letter-exchange", value = "#{dlqExchange}")
                        }),
            exchange = @Exchange(value = "#{messageExchange}", type = "topic"))
    )
    public void handleMessage(String message){
        LOG.info("Message received {}", message);
        throw new BusinessException();
    }
}

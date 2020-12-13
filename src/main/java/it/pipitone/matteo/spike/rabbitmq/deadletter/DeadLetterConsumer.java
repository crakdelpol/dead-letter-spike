package it.pipitone.matteo.spike.rabbitmq.deadletter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class DeadLetterConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DeadLetterConsumer.class);
    public final AmqpTemplate amqpTemplate;

    public DeadLetterConsumer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @RabbitListener(queues = "dead-letter.queue")
    public void resendMessage(Message failedMessage){
        logger.info("DLQ receive message {}", failedMessage);
        amqpTemplate.convertAndSend(MessagingConfiguration.MESSAGE_EXCHANGE, failedMessage.getMessageProperties().getReceivedRoutingKey(), failedMessage);
    }

}

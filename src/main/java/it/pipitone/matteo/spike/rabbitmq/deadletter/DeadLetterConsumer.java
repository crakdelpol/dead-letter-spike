package it.pipitone.matteo.spike.rabbitmq.deadletter;

import it.pipitone.matteo.spike.rabbitmq.configuration.MessagingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static it.pipitone.matteo.spike.rabbitmq.configuration.MessagingConfiguration.*;

public class DeadLetterConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DeadLetterConsumer.class);

    public final AmqpTemplate amqpTemplate;

    public DeadLetterConsumer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @RabbitListener(queues = "dead-letter.queue")
    public void resendMessage(Message failedMessage){
        Integer retriesCnt = (Integer) failedMessage.getMessageProperties().getHeaders().get(HEADER_X_RETRIES_COUNT);
        logger.info("Number of retries count {}", retriesCnt);
        if (retriesCnt == null)
            retriesCnt = 1;

        if (retriesCnt > MAX_RETRIES_COUNT) {
            logger.info("Sending message to the parking lot queue");
            amqpTemplate.convertAndSend(EXCHANGE_PARKING_LOT, failedMessage.getMessageProperties().getReceivedRoutingKey(), failedMessage);
            return;
        }
        logger.info("DLQ receive message {}", failedMessage);
        failedMessage.getMessageProperties().getHeaders().put(HEADER_X_RETRIES_COUNT, ++retriesCnt);
        amqpTemplate.convertAndSend(MessagingConfiguration.MESSAGE_EXCHANGE, failedMessage.getMessageProperties().getReceivedRoutingKey(), failedMessage);
    }

}

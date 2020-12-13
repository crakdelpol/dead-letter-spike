package it.pipitone.matteo.spike.rabbitmq.deadletter;

import org.springframework.amqp.core.AmqpTemplate;

import static it.pipitone.matteo.spike.rabbitmq.deadletter.MessagingConfiguration.MESSAGE_EXCHANGE;

public class SimpleProducer {

    private final AmqpTemplate amqpTemplate;

    public SimpleProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendGenericMessage(){
        amqpTemplate.convertAndSend(MESSAGE_EXCHANGE, "simple.message","messageTest");
    }
}

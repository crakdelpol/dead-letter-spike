package it.pipitone.matteo.spike.rabbitmq.deadletter;

import org.springframework.amqp.core.AmqpTemplate;

public class SimpleProducer {

    private final AmqpTemplate amqpTemplate;

    public SimpleProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendGenericMessage(){
        amqpTemplate.convertAndSend("exchange.direct", "simple.message","messageTest");
    }
}
package it.pipitone.matteo.spike.rabbitmq.deadletter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleConsumer.class);

    public void handleMessage(String message){

        LOG.info("Message received {}", message);
        throw new BusinessException();
    }
}
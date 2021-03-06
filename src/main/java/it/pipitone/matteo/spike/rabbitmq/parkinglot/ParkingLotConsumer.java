package it.pipitone.matteo.spike.rabbitmq.parkinglot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class ParkingLotConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ParkingLotConsumer.class);

    @RabbitListener(queues = "parking-lot.queue")
    public void handleMessage(Message failedMessage){
        logger.info("Received message in parking lot queue {}", failedMessage.toString());
    }
}

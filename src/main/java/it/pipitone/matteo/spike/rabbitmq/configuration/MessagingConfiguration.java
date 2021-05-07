package it.pipitone.matteo.spike.rabbitmq.configuration;

import it.pipitone.matteo.spike.rabbitmq.deadletter.DeadLetterConsumer;
import it.pipitone.matteo.spike.rabbitmq.messages.SimpleConsumer;
import it.pipitone.matteo.spike.rabbitmq.parkinglot.ParkingLotConsumer;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class MessagingConfiguration {

    public static String DLQ_EXCHANGE = "exchange.fanout.deal-letter";

    public static String PARKING_LOT_EXCHANGE = "exchange.fanout.parking-lot";
    public static String MESSAGE_EXCHANGE = "exchange.topic.message";
    public static String EXCHANGE_PARKING_LOT = "exchange.fanout.parking-lot";
    public static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
    public static final int MAX_RETRIES_COUNT = 2;

    @Bean
    @Qualifier("dlqExchange")
    public String dlqExchange(){
        return DLQ_EXCHANGE;
    }
    @Bean
    @Qualifier("messageExchange")
    public String messageExchange(){
        return MESSAGE_EXCHANGE;
    }
    @Bean
    Queue deadLetterQueue(){
        return QueueBuilder.nonDurable("dead-letter.queue").build();
    }

    @Bean
    Queue parkingLotQueue(){
        return QueueBuilder.nonDurable("parking-lot.queue").build();
    }

    @Bean
    TopicExchange directExchange(){
        return new TopicExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    AbstractExchange deadLetterExchange(){
        return new FanoutExchange(DLQ_EXCHANGE);
    }

     @Bean
    FanoutExchange parkingLotExchange(){
        return new FanoutExchange(PARKING_LOT_EXCHANGE);
    }

    @Bean
    Binding bindDLQ(){
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("").and(new HashMap<>());
    }

    @Bean
    Binding bindParkingLot(){
        return BindingBuilder.bind(parkingLotQueue()).to(parkingLotExchange());
    }

    @Bean
    SimpleConsumer simpleConsumer(){
        return new SimpleConsumer();
    }

    @Bean
    DeadLetterConsumer deadLetterConsumer(AmqpTemplate amqpTemplate){
        return new DeadLetterConsumer(amqpTemplate);
    }

    @Bean
    ParkingLotConsumer parkingLotConsumer(){
        return new ParkingLotConsumer();
    }
}

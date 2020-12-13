package it.pipitone.matteo.spike.rabbitmq.deadletter;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    public static String DLQ_EXCHANGE = "exchange.fanout.deal-letter";
    public static String PARKING_LOT_EXCHANGE = "exchange.fanout.parking-lot";
    public static String MESSAGE_EXCHANGE = "exchange.direct.message";
    public static String EXCHANGE_PARKING_LOT = "exchange.fanout.parking-lot";
    public static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
    public static final int MAX_RETRIES_COUNT = 2;

    @Bean
    Queue messagesQueue(){
        return QueueBuilder.nonDurable("messages.queue").withArgument("x-dead-letter-exchange", DLQ_EXCHANGE).build();
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
    DirectExchange directExchange(){
        return new DirectExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    FanoutExchange deadLetterExchange(){
        return new FanoutExchange(DLQ_EXCHANGE);
    }

     @Bean
    FanoutExchange parkingLotExchange(){
        return new FanoutExchange(PARKING_LOT_EXCHANGE);
    }

    @Bean
    Binding bindMessage(){
        return BindingBuilder.bind(messagesQueue()).to(directExchange()).with("simple.message");
    }

    @Bean
    Binding bindDLQ(){
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange());
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

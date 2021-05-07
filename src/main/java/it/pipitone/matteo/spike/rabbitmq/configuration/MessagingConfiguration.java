package it.pipitone.matteo.spike.rabbitmq.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pipitone.matteo.spike.rabbitmq.deadletter.DeadLetterConsumer;
import it.pipitone.matteo.spike.rabbitmq.parkinglot.ParkingLotConsumer;
import it.pipitone.matteo.spike.rabbitmq.messages.SimpleConsumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;

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
    Binding bindMessage(){
        return BindingBuilder.bind(messagesQueue()).to(directExchange()).with("simple.message");
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


    @Bean
    DirectMessageListenerContainer insufficientFundsMessageListenerContainer(
            ConnectionFactory connectionFactory,
            Queue messagesQueue,
            @Qualifier("simpleMessageListener") MessageListener simpleMessageListener,
            StatefulRetryOperationsInterceptor interceptor
    )
    {
        return new DirectMessageListenerContainer(connectionFactory){{
            setQueues(messagesQueue);
            setMessageListener(simpleMessageListener);
//            setDefaultRequeueRejected(false);
            setAdviceChain(interceptor);
        }};

    }

    @Bean
    MessageListenerAdapter simpleMessageListener(SimpleConsumer simpleConsumer, MessageConverter jsonMessageConverter){
        return new MessageListenerAdapter(simpleConsumer, jsonMessageConverter);
    }

    @Bean
    MessageConverter jsonMessageConverter(
            ObjectMapper jacksonObjectMapper
    ) {
        return new Jackson2JsonMessageConverter(jacksonObjectMapper);
    }

    @Bean
    public StatefulRetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder.stateful()
                .maxAttempts(5)
                .backOffOptions(1000, 2.0, 10000) // initialInterval, multiplier, maxInterval
                .build();
    }
}

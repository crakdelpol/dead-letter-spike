package it.pipitone.matteo.spike.rabbitmq.deadletter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    String DLQ_EXCHANGE = "fanout.exchange";
    public static String MESSAGE_EXCHANGE = "exchange.direct";

    @Bean
    Queue messagesQueue(){

        return QueueBuilder.nonDurable("messages.queue").withArgument("x-dead-letter-exchange", DLQ_EXCHANGE).build();
    }

    @Bean
    Queue deadLetterQueue(){
        return QueueBuilder.nonDurable("dead-letter.queue")
                .build();
    }

    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange(DLQ_EXCHANGE);
    }

    @Bean
    Binding bindMessage(){
        return BindingBuilder.bind(messagesQueue()).to(directExchange()).with("simple.message");
    }

    @Bean
    Binding bindDLQ(){
        return BindingBuilder.bind(deadLetterQueue()).to(fanoutExchange());
    }

    @Bean
    MessageConverter jsonMessageConverter(
            ObjectMapper jacksonObjectMapper
    ) {
        return new Jackson2JsonMessageConverter(jacksonObjectMapper);
    }

    @Bean
    SimpleConsumer simpleConsumer(){
        return new SimpleConsumer();
    }

    @Bean
    DeadLetterConsumer deadLetterConsumer(AmqpTemplate amqpTemplate){
        return new DeadLetterConsumer(amqpTemplate);
    }
}

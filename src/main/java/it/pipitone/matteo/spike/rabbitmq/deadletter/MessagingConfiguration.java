package it.pipitone.matteo.spike.rabbitmq.deadletter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    @Bean
    Queue messagesQueue(){

        return QueueBuilder.durable("messages.queue")
                .build();
    }


    @Bean
    DirectExchange fanoutExchange(){
        return new DirectExchange("exchange.direct");
    }

    @Bean
    Binding bindMessage(){
        return BindingBuilder.bind(messagesQueue()).to(fanoutExchange()).with("simple.message");
    }

    @Bean
    DirectMessageListenerContainer directMessageListenerContainerDLQ(
            ConnectionFactory connectionFactory,
            Queue messagesQueue,
            MessageListener messageListenerAdapter)
    {
        return new DirectMessageListenerContainer(connectionFactory){{
            setQueues(messagesQueue);
            setMessageListener(messageListenerAdapter);
        }};

    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(SimpleConsumer simpleConsumer, MessageConverter jsonMessageConverter){

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(simpleConsumer, jsonMessageConverter);
        messageListenerAdapter.setDefaultRequeueRejected(false);
        return messageListenerAdapter;
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


}

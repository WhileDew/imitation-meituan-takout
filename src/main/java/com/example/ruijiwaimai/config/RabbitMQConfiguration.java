package com.example.ruijiwaimai.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.ruijiwaimai.constans.RabbitMQConstants.*;

@Configuration
public class RabbitMQConfiguration {
    //创建交换机
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(DISH_EXCHANGE_KEY);
    }

    //创建新增队列
    @Bean
    public Queue dishInsertQueue(){
        return new Queue(DISH_INSERT_QUEUE);
    }
    @Bean
    public Queue dishDeleteQueue(){
        return new Queue(DISH_DELETE_QUEUE);
    }

    @Bean
    public Binding bindingInsertQueue(Queue dishInsertQueue, TopicExchange topicExchange){
        return BindingBuilder.bind(dishInsertQueue).to(topicExchange).with(DISH_DELETE_KEY);
    }
}

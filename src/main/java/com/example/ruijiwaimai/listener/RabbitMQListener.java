package com.example.ruijiwaimai.listener;

import com.example.ruijiwaimai.constans.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Set;

import static com.example.ruijiwaimai.constans.RabbitMQConstants.*;

/**
 * 监听菜品/套餐信息变化，同步缓存
 */
@Component
@Slf4j
public class RabbitMQListener {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 新增菜品队列
     * @param id 分类id
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = DISH_INSERT_QUEUE),
                    exchange = @Exchange(name = DISH_EXCHANGE_KEY, type = ExchangeTypes.TOPIC),
                    key = {DISH_INSERT_KEY})
    )
    public void listenDishInsertQueue(Long id){
        // 更新缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_DISHDTO_KEY + id);
        log.error("mqmqmq");
    }

    /**
     * 删除菜品队列
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = DISH_DELETE_QUEUE),
                    exchange = @Exchange(name = DISH_EXCHANGE_KEY, type = ExchangeTypes.TOPIC),
                    key = {DISH_DELETE_KEY})
    )
    public void listenDishDeleteQueue(){
        // 更新缓存
        Set<String> keys = stringRedisTemplate.keys(RedisConstants.CACHE_DISHDTO_KEY + "*");
        assert keys != null;
        stringRedisTemplate.delete(keys);
    }


}

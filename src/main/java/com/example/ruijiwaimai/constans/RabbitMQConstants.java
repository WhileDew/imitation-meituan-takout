package com.example.ruijiwaimai.constans;

public class RabbitMQConstants {
    /**
     * 交换机key
     */
    public static final String DISH_EXCHANGE_KEY = "dish.topic";
    public static final String SETMEAL_EXCHANGE_KEY = "setmeal.topic";
    /**
     * 新增队列key
     */
    public static final String DISH_INSERT_QUEUE = "dish.insert.queue";
    public static final String SETMEAL_INSERT_QUEUE = "setmeal.insert.queue";
    /**
     * 删除队列key
     */
    public static final String DISH_DELETE_QUEUE = "dish.delete.queue";
    public static final String SETMEAL_DELETE_QUEUE = "setmeal.delete.queue";
    /**
     * 新增routing key
     */
    public static final String DISH_INSERT_KEY = "dish.insert";
    public static final String SETMEAL_INSERT_KEY = "setmeal.insert";
    /**
     * 删除routing key
     */
    public static final String DISH_DELETE_KEY = "dish.delete";
    public static final String SETMEAL_DELETE_KEY = "setmeal.delete";
}

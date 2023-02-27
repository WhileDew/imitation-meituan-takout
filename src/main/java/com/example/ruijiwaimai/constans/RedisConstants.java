package com.example.ruijiwaimai.constans;

import java.util.concurrent.TimeUnit;

public class RedisConstants {
    /**
     * 验证码key
     */
    public static final String PHONE_CODE_KEY = "phone_code_";
    public static final Long PHONE_CODE_TTL = 3L;
    public static final TimeUnit PHONE_CODE_TIMEUNIT = TimeUnit.MINUTES;

    /**
     * 菜品key
     */
    public static final String CACHE_DISHDTO_KEY = "cache_dishdto_key_";
    public static final Long CACHE_KEY_TTL = 5L;
    public static final TimeUnit CACHE_KEY_TIMEUNIT = TimeUnit.MINUTES;
}

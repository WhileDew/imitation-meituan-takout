package com.example.ruijiwaimai.constans;

import java.util.concurrent.TimeUnit;

public class RedisConstants {
    public static final String PHONE_CODE_KEY = "phone_code_";
    public static final Long PHONE_CODE_TTL = 3L;
    public static final TimeUnit PHONE_CODE_TIMEUNIT = TimeUnit.MINUTES;
}

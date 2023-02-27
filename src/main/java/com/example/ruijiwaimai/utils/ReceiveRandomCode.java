package com.example.ruijiwaimai.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public class ReceiveRandomCode {
    public static String getPhoneCode(){
        // 返回六位数的随机码
        return RandomUtil.randomNumbers(6);
    }
}

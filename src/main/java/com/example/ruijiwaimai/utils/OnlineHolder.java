package com.example.ruijiwaimai.utils;

import com.example.ruijiwaimai.entity.Employee;

/**
 * employee-id线程
 */
public class OnlineHolder {
    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    public static void saveId(Long id){
        tl.set(id);
    }

    public static Long getId(){
        return tl.get();
    }

    public static void removeId(){
        tl.remove();
    }
}

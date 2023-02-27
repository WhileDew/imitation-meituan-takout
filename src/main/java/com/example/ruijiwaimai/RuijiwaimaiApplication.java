package com.example.ruijiwaimai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@MapperScan("com.example.ruijiwaimai.mapper")
@ServletComponentScan
public class RuijiwaimaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuijiwaimaiApplication.class, args);
    }

}

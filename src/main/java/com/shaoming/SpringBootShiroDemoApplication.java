package com.shaoming;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shaoming.*.mapper")
public class SpringBootShiroDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShiroDemoApplication.class, args);
    }

}

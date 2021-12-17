package com.hack2win.dynamicrabbits;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan(basePackages = {"com.hack2win.dynamicrabbits.dynamic.controller", "com.hack2win.dynamicrabbits.dynamic.service", "com.hack2win.dynamicrabbits.cache"})
@MapperScan(basePackages = "com.hack2win.dynamicrabbits.dynamic.mapper")
@EnableScheduling
public class DynamicRabbitsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynamicRabbitsApplication.class, args);
    }
}

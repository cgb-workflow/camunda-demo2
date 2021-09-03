package org.hgq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableProcessApplication
@SpringBootApplication
@MapperScan(basePackages = {"org.hgq.mapper.base"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }




}
package org.hgq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//@EnableProcessApplication
@SpringBootApplication
@MapperScan(basePackages = {"org.hgq.mapper.base"})
@EnableAspectJAutoProxy
public class CamundaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CamundaApplication.class, args);
    }




}
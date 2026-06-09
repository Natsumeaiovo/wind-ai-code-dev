package com.na.windaicodedev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.na.windaicodedev.mapper")
public class WindAiCodeDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(WindAiCodeDevApplication.class, args);
    }

}

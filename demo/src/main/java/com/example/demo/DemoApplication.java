package com.example.demo;


import com.example.demo.service.JsonService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@MapperScan("com.example.demo.mapper")
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    JsonService jsonService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws IOException {
       jsonService.write();
    }
}


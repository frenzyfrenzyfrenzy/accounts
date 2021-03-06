package com.svintsov.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CommonConfig.class, PromTransferConfig.class})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}

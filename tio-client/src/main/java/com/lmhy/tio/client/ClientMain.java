package com.lmhy.tio.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ClientMain {

    @PostConstruct
    public void init() throws Exception {
        TioClientCore.start();
        TioClientCore.send("connect server");
    }

    /**
     * 启动程序入口
     */
    public static void main(String[] args) {
        SpringApplication.run(ClientMain.class, args);
    }

}

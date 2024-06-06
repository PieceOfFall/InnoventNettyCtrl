package com.fall.nettyctrl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class NettyCtrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyCtrlApplication.class, args);
    }

}

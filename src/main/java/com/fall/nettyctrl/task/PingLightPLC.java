package com.fall.nettyctrl.task;

import com.fall.nettyctrl.netty.TcpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024/9/2 16:17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PingLightPLC {

    private final TcpClient tcpClient;

    @Value("${web-panel.light.ip}")
    private String ip;

    @Value("${web-panel.light.port}")
    private Integer port;

    @Async
    @Scheduled(fixedDelay = 10 * 1000)
    public void pingTask() {
        tcpClient.sendMsg(ip,port,"ping");
    }
}

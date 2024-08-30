package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.NettySender;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author FAll
 * @date 2024年06月06日 13:12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LightHandler implements IOperationHandler {

    @Value("${web-panel.light.ip}")
    private String ip;
    @Value("${web-panel.light.port}")
    private Integer port;

    private final NettySender nettySender;

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        String operation = webPanelMsg.getOperation();

        nettySender.sendMsgAsync(operation, ip, port);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                nettySender.sendMsgAsync(
                        NettySender.hexStringToByteBuf("poweron".equals(operation) ? "01" : "00"),
                        ip, port);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}


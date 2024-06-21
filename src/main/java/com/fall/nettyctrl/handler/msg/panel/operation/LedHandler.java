package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024年06月07日 13:51
 */
@Component
@RequiredArgsConstructor
public class LedHandler implements IOperationHandler {

    @Value("${web-panel.led.ip}")
    private String ip;
    @Value("${web-panel.led.port}")
    private Integer port;
    @Value("${web-panel.led.command}")
    private String command;

    private final TcpClient tcpClient;

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        Integer operationParam = (Integer) webPanelMsg.getOperationParam();

        String completeCommand = command
                .replace("{id}", operationParam.toString());
        tcpClient.sendMsg(ip, port, completeCommand);
    }
}

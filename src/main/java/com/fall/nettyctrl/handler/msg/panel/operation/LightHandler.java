package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024年06月06日 13:12
 */
@Component
@RequiredArgsConstructor
public class LightHandler implements IOperationHandler {

    @Value("${web-panel.light.ip}")
    private String ip;
    @Value("${web-panel.light.port}")
    private Integer port;
    @Value("${web-panel.light.command}")
    private String command;

    private final TcpClient tcpClient;

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        Object lightId = webPanelMsg.getOperationParam();
        String replaceId = lightId instanceof String ? (String) lightId : lightId.toString();

        String operation = webPanelMsg.getOperation();
        String completeCommand = command
                .replace("{id}", replaceId)
                .replace("{command}", operation);
        tcpClient.sendMsg(ip, port, completeCommand);
    }
}

package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024年06月07日 16:03
 */
@Component
@RequiredArgsConstructor
public class BigScreenHandler implements IOperationHandler {

    @Value("${web-panel.big-screen.ip}")
    private String ip;
    @Value("${web-panel.big-screen.port}")
    private Integer port;
    @Value("${web-panel.big-screen.command}")
    private String command;

    private final TcpClient tcpClient;

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        Integer screenId = (Integer)webPanelMsg.getOperationParam();
        String operation = webPanelMsg.getOperation();
        String completeCommand = command
                .replace("{id}", screenId.toString())
                .replace("{command}", operation);
        tcpClient.sendMsg(ip,port,completeCommand);
    }
}

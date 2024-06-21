package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024年06月20日 16:11
 */
@Component
@RequiredArgsConstructor
public class SequentialHandler implements IOperationHandler {

    @Value("${web-panel.sequential.ip}")
    private String ip;
    @Value("${web-panel.sequential.port}")
    private Integer port;

    @Value("${web-panel.sequential.poweron}")
    private String poweronCommand;
    @Value("${web-panel.sequential.poweroff}")
    private String poweroffCommand;

    private final TcpClient tcpClient;

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        String operation = webPanelMsg.getOperation();
        String commandToSend =
                "poweron" .equals(operation) ? poweronCommand  :
                "poweroff".equals(operation) ? poweroffCommand : null;
        if (commandToSend == null) return;
        tcpClient.sendMsg(ip, port,TcpClient.hexStringToByteBuf(commandToSend));
    }
}

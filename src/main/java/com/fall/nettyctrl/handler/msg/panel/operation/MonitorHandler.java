package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024年08月30日 14:17
 */
@Component
@RequiredArgsConstructor
public class MonitorHandler implements IOperationHandler {

    private final TcpClient tcpClient;

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        String operation = webPanelMsg.getOperation();
        tcpClient.sendMsg("127.0.0.1", 8686, operation);
    }
}

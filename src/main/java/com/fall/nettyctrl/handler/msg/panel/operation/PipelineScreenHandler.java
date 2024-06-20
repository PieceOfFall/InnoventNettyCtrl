package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024年06月20日 15:29
 */
@Component
@RequiredArgsConstructor
public class PipelineScreenHandler implements IOperationHandler {

    @Value("${web-panel.pipeline-screen.ip}")
    private String ip;
    @Value("${web-panel.pipeline-screen.port}")
    private Integer port;
    @Value("${web-panel.pipeline-screen.command}")
    private String command;

    private final TcpClient tcpClient;
    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        Object screenId = webPanelMsg.getOperationParam();
        String replaceId = screenId instanceof String ? (String) screenId : screenId.toString();

        String operation = webPanelMsg.getOperation();
        String completeCommand = command
                .replace("{id}", replaceId)
                .replace("{command}", operation);
        tcpClient.sendMsg(ip, port, completeCommand);
    }
}

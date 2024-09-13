package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.NettySender;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.CommandParam;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024/9/12 13:02
 */
@Component
@RequiredArgsConstructor
public class CommandHandler implements IOperationHandler {

    private final TcpClient tcpClient;
    private final NettySender udpClient;

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        String operation = webPanelMsg.getOperation();
        CommandParam commandParam = (CommandParam) webPanelMsg.getOperationParam();

        if("tcp".equals(operation)) {
           if(commandParam.getIsHex()) {
               tcpClient.sendMsg(
                       commandParam.getIp(),
                       commandParam.getPort(),
                       NettySender.hexStringToByteBuf(commandParam.getData()));
           } else {
               tcpClient.sendMsg(
                       commandParam.getIp(),
                       commandParam.getPort(),
                       commandParam.getData());
           }
        } else {
            if(commandParam.getIsHex()) {
                udpClient.sendMsgAsync(
                        NettySender.hexStringToByteBuf(commandParam.getData()),
                        commandParam.getIp(),
                        commandParam.getPort());
            } else {
                udpClient.sendMsgAsync(
                        commandParam.getData(),
                        commandParam.getIp(),
                        commandParam.getPort());
            }
        }
    }
}


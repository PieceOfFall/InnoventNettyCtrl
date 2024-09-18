package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.NettySender;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.CommandParam;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

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
        LinkedHashMap<String,Object> commandParamMap = (LinkedHashMap<String,Object>) webPanelMsg.getOperationParam();

        CommandParam commandParam = CommandParam.builder()
                .ip((String) commandParamMap.get("ip"))
                .port((Integer) commandParamMap.get("port"))
                .isHex((Boolean) commandParamMap.get("isHex"))
                .data((String) commandParamMap.get("data"))
                .build();
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


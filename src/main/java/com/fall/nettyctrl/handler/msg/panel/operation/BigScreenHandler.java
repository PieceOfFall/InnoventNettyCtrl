package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FAll
 * @date 2024年06月07日 16:03
 */
@Component
@ConfigurationProperties("web-panel.big-screen")
public class BigScreenHandler implements IOperationHandler {

    @Setter
    private Integer port;

    @Setter
    private List<LinkedHashMap<String, String>> list;
    private final TcpClient tcpClient;

    @Autowired
    public BigScreenHandler(TcpClient tcpClient) {this.tcpClient = tcpClient;}

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        String operation = webPanelMsg.getOperation();
        Object id = webPanelMsg.getOperationParam();
        for (LinkedHashMap<String, String> stringLinkedHashMap : list) {
            if(stringLinkedHashMap.get("id").equals(id)) {
                String ip = stringLinkedHashMap.get("ip");
                String commandStr = "poweron".equals(operation) ?
                        stringLinkedHashMap.get("poweron") :
                        stringLinkedHashMap.get("poweroff");
                ByteBuf byteBufMsg = TcpClient.hexStringToByteBuf(commandStr);
                tcpClient.sendMsg(ip,port,byteBufMsg);
                break;
            }
        }

    }
}

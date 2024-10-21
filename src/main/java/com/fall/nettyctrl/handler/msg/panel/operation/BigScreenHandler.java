package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.NettySender;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.BigScreenParam;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import io.netty.buffer.ByteBuf;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FAll
 * @date 2024年06月07日 16:03
 */
@Component
@ConfigurationProperties("web-panel.big-screen")
public class BigScreenHandler implements IOperationHandler {
    //
    @Setter
    private List<BigScreenParam> list;
    private final NettySender udpSender;
    private final TcpClient tcpClient;

    @Autowired
    public BigScreenHandler(NettySender udpSender, TcpClient tcpClient) {
        this.udpSender = udpSender;
        this.tcpClient = tcpClient;
    }

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        String operation = webPanelMsg.getOperation();
        Object id = webPanelMsg.getOperationParam();
        for (BigScreenParam bigScreenParam : list) {
            if (bigScreenParam.getId().equals(id)) {
                String ip = bigScreenParam.getIp();
                Integer port = bigScreenParam.getPort();
                List<String> commandStr = "poweron".equals(operation) ?
                        bigScreenParam.getPoweron() :
                        bigScreenParam.getPoweroff();
                for (String singleCmd : commandStr) {
                    ByteBuf byteBufMsg = TcpClient.hexStringToByteBuf(singleCmd);
                    tcpClient.sendMsg(ip, port, byteBufMsg);
                }
                break;
            }
        }

    }
}

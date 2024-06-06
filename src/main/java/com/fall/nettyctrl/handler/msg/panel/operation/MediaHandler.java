package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.NettySender;
import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @author FAll
 * @date 2024年06月06日 13:09
 */
@Component
@ConfigurationProperties(prefix = "media")
public class MediaHandler implements IOperationHandler {

    private final TcpClient tcpClient;
    private final NettySender udpSender;

    @Getter
    @Setter
    private String mode;
    @Setter
    private String normalCommand;
    @Setter
    private String mediaIp;
    @Setter
    private Integer mediaPort;
    @Setter
    private LinkedHashMap<String,String> gateway;
    @Setter
    private LinkedHashMap<Integer,String> mute;
    @Setter
    private LinkedHashMap<Integer,String> unmute;

    @Autowired
    MediaHandler(TcpClient tcpClient,
                 NettySender udpSender) {
        this.tcpClient = tcpClient;
        this.udpSender = udpSender;
    }

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        String msgToSend = null;
        switch (webPanelMsg.getOperation()) {
                case "poweron":{
                    tcpClient.sendMsg(
                            gateway.get("ip"),
                            Integer.parseInt(gateway.get("port")),
                            gateway.get("poweron"));
                    break;
                }
                case "poweroff":{
                    tcpClient.sendMsg(
                            gateway.get("ip"),
                            Integer.parseInt(gateway.get("port")),
                            gateway.get("poweroff"));
                    break;
                }
                case "normal":{
                    msgToSend = normalCommand;
                    this.mode = "normal";
                    break;
                }
                case "positioning": this.mode = "positioning";break;
                case "mute": msgToSend = mute.get((Integer) webPanelMsg.getOperationParam());break;
                case "unmute": msgToSend = unmute.get((Integer) webPanelMsg.getOperationParam());break;
            }
            if(msgToSend == null) return;
        udpSender.sendMsgAsync(
                NettySender.hexStringToByteBuf(msgToSend),
                mediaIp,
                mediaPort);
        }

    }


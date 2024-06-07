package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
import com.fall.nettyctrl.netty.NettySender;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FAll
 * @date 2024年06月06日 13:09
 */
@Component
@ConfigurationProperties(prefix = "web-panel.media")
public class MediaHandler implements IOperationHandler {

    private final NettySender udpSender;

    @Getter
    @Setter
    private String mode;
    @Setter
    private List<String> normalCommand;
    @Setter
    private String mediaIp;
    @Setter
    private Integer mediaPort;
    @Setter
    private LinkedHashMap<Integer,String> mute;
    @Setter
    private LinkedHashMap<Integer,String> unmute;

    @Autowired
    MediaHandler(NettySender udpSender) {
        this.udpSender = udpSender;
    }

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        String msgToSend = null;
        switch (webPanelMsg.getOperation()) {
            case "normal":{
                    for (String command : normalCommand) {
                        udpSender.sendMsgAsync(
                                NettySender.hexStringToByteBuf(command),
                                mediaIp,
                                mediaPort);
                    }
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


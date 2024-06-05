package com.fall.nettyctrl.handler.msg;

import com.fall.nettyctrl.netty.NettySender;
import com.fall.nettyctrl.vo.MediaCommand;
import com.fall.nettyctrl.vo.PositioningMsg;
import com.fall.nettyctrl.vo.WsMsg;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 定位消息处理器 (音频控制)
 *
 * @author FAll
 * @date 2024年06月03日 13:25
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "stations")
public class PositioningMsgHandler implements MsgHandler {

    @Setter
    private List<MediaCommand> commands;
    private final NettySender nettySender;
    @Value("${stations.media-ip}")
    private String mediaIp;
    @Value("${stations.media-port}")
    private Integer mediaPort;

    @Autowired
    public PositioningMsgHandler(NettySender nettySender) {
        this.nettySender = nettySender;
    }

    @Override
    public void handleMsg(ChannelHandlerContext ctx, WsMsg msg) {
        if (msg instanceof PositioningMsg positioningMsg) {
            for (Map.Entry<Integer, String> entry : positioningMsg.getPosMap().entrySet()) {
                var stationId = entry.getKey();
                var mediaCommand = commands.get(stationId);
                var commandHexStr = mediaCommand.getCommand(entry.getValue());
                nettySender.sendMsgAsync(NettySender.hexStringToByteBuf(commandHexStr), mediaIp, mediaPort);
            }
            log.info("Received positioning message: " + positioningMsg.getPosMap());
        }
    }
}
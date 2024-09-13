package com.fall.nettyctrl.handler.msg.positioning;

import com.fall.nettyctrl.handler.msg.IMsgHandler;
import com.fall.nettyctrl.handler.msg.panel.operation.MediaHandler;
import com.fall.nettyctrl.netty.NettySender;
import com.fall.nettyctrl.vo.positioning.MediaCommand;
import com.fall.nettyctrl.vo.positioning.PositioningMsg;
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
@ConfigurationProperties(prefix = "positioning")
public class PositioningMsgHandler implements IMsgHandler {

    @Setter
    private List<MediaCommand> commands;
    private final MediaHandler mediaHandler;
    private final NettySender nettySender;
    @Value("${positioning.media-ip}")
    private String mediaIp;
    @Value("${positioning.media-port}")
    private Integer mediaPort;


    @Autowired
    public PositioningMsgHandler(NettySender nettySender,MediaHandler mediaHandler) {
        this.nettySender = nettySender;
        this.mediaHandler = mediaHandler;
    }

    @Override
    public void handleMsg(ChannelHandlerContext ctx, WsMsg msg) {
        if (msg instanceof PositioningMsg positioningMsg && "positioning".equals(mediaHandler.getMode())) {
            for (Map.Entry<Integer, String> entry : positioningMsg.getPosMap().entrySet()) {
                var stationId = entry.getKey();
                var mediaCommand = commands.get(stationId);
                var commandHexStr = mediaCommand.getCommand(entry.getValue());

                for (String singleCommand : commandHexStr)
                    nettySender.sendMsgAsync(NettySender.hexStringToByteBuf(singleCommand), mediaIp, mediaPort);
            }

            log.info("Handle positioning message: " + positioningMsg.getPosMap());
        }
    }
}

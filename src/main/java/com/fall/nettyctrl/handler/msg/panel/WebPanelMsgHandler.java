package com.fall.nettyctrl.handler.msg.panel;

import com.fall.nettyctrl.handler.msg.IMsgHandler;
import com.fall.nettyctrl.vo.param.WebPanelMsg;
import com.fall.nettyctrl.vo.WsMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 控制面板消息处理器
 *
 * @author FAll
 * @date 2024年06月03日 13:26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebPanelMsgHandler implements IMsgHandler {

    private final PowerOnHandler powerOnHandler;
    private final PowerOffHandler powerOffHandler;

    @Override
    public void handleMsg(ChannelHandlerContext ctx, WsMsg msg) {
        if (msg instanceof WebPanelMsg webPanelMsg) {
            String operation = webPanelMsg.getOperation();
            if ("ping".equals(operation)) return;
            ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + webPanelMsg));

            switch (operation) {
                case "poweron": powerOnHandler.handleOperation(webPanelMsg);
                case "poweroff": powerOffHandler.handleOperation(webPanelMsg);
            }
        }
    }


}

package com.fall.nettyctrl.handler.msg.panel;

import com.fall.nettyctrl.handler.msg.IMsgHandler;
import com.fall.nettyctrl.handler.msg.panel.operation.ComputerPowerOffHandler;
import com.fall.nettyctrl.handler.msg.panel.operation.ComputerPowerOnHandler;
import com.fall.nettyctrl.handler.msg.panel.operation.LightHandler;
import com.fall.nettyctrl.handler.msg.panel.operation.MediaHandler;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
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

    private final ComputerPowerOnHandler computerPowerOnHandler;
    private final ComputerPowerOffHandler computerPowerOffHandler;
    private final MediaHandler mediaHandler;
    private final LightHandler lightHandler;

    @Override
    public void handleMsg(ChannelHandlerContext ctx, WsMsg msg) {
        if (msg instanceof WebPanelMsg webPanelMsg) {

            String target = webPanelMsg.getTarget();
            String operation = webPanelMsg.getOperation();

            if ("ping".equals(operation)) return;
            ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + webPanelMsg));

            IOperationHandler operationHandler = switch (target) {
                case "computer" -> "poweron".equals(operation) ? computerPowerOnHandler : computerPowerOffHandler;
                case "media" -> mediaHandler;
                case "light" -> lightHandler;
                default -> throw new IllegalStateException("Unexpected target: " + target);
            };
            operationHandler.handleOperation(webPanelMsg);
        }
    }

}

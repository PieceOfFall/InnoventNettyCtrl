package com.fall.nettyctrl.handler.msg.panel;

import com.fall.nettyctrl.handler.msg.IMsgHandler;
import com.fall.nettyctrl.handler.msg.panel.operation.*;
import com.fall.nettyctrl.util.ResponseUtil;
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
    private final LedHandler ledHandler;
    private final SoundBoxHandler soundBoxHandler;
    private final BigScreenHandler bigScreenHandler;
    private final LeaderScreenHandler leaderScreenHandler;
    private final PipelineScreenHandler pipelineScreenHandler;
    private final ScreenHandler screenHandler;
    private final RelayHandler relayHandler;
    private final SequentialHandler sequentialHandler;


    private final ResponseUtil responseUtil;

    @Override
    public void handleMsg(ChannelHandlerContext ctx, WsMsg msg) {
        if (msg instanceof WebPanelMsg webPanelMsg) {

            String target = webPanelMsg.getTarget();
            String operation = webPanelMsg.getOperation();

            if ("ping".equals(operation)) return;
            ctx.channel().writeAndFlush(new TextWebSocketFrame(responseUtil.notAlert("后端接收成功:" + webPanelMsg)));

            IOperationHandler operationHandler = switch (target) {
                case "computer" -> "poweron".equals(operation) ? computerPowerOnHandler : computerPowerOffHandler;
                case "media" -> mediaHandler;
                case "relay" -> relayHandler;
                case "light" -> lightHandler;
                case "leader-screen" -> leaderScreenHandler;
                case "pipeline-screen" -> pipelineScreenHandler;
                case "led" -> ledHandler;
                case "soundbox" -> soundBoxHandler;
                case "big-screen" -> bigScreenHandler;
                case "screen" -> screenHandler;
                case "sequential" -> sequentialHandler;
                default -> throw new IllegalStateException("Unexpected target: " + target);
            };
            operationHandler.handleOperation(webPanelMsg);
        }
    }

}

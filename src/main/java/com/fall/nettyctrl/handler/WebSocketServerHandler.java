package com.fall.nettyctrl.handler;

import com.fall.nettyctrl.handler.msg.positioning.PositioningMsgHandler;
import com.fall.nettyctrl.handler.msg.panel.WebPanelMsgHandler;
import com.fall.nettyctrl.vo.positioning.PositioningMsg;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import com.fall.nettyctrl.vo.WsMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WebSocket消息处理器
 *
 * @author FAll
 * @date 2024年05月31日 14:27
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    private final ObjectMapper objectMapper;
    private final PositioningMsgHandler positioningMsgHandler;
    private final WebPanelMsgHandler webPanelMsgHandler;
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws JsonProcessingException {
        WsMsg msg = objectMapper.readValue(textWebSocketFrame.text(), WsMsg.class);
        switch (msg) {
            case PositioningMsg positioningMsg -> positioningMsgHandler.handleMsg(ctx, positioningMsg);
            case WebPanelMsg webPanelMsg -> webPanelMsgHandler.handleMsg(ctx, webPanelMsg);
            default -> throw new IllegalStateException("Unexpected msg value: " + msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        var incoming = ctx.channel();
        // 添加连接
        log.info("Client connected: " + incoming);
        channels.add(incoming);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 断开连接
        log.info("Client disconnected: " + ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        log.error(cause.getMessage());
        ctx.close();
    }

    public static void broadcastMessage(String message) {
        TextWebSocketFrame frame = new TextWebSocketFrame(message);
        channels.writeAndFlush(frame);
    }
}

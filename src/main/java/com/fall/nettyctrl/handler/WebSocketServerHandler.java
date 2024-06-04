package com.fall.nettyctrl.handler;

import com.fall.nettyctrl.handler.msg.MsgHandler;
import com.fall.nettyctrl.handler.msg.PositioningMsgHandler;
import com.fall.nettyctrl.handler.msg.WebPanelMsgHandler;
import com.fall.nettyctrl.vo.PositioningMsg;
import com.fall.nettyctrl.vo.WebPanelMsg;
import com.fall.nettyctrl.vo.WsMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws JsonProcessingException {
        WsMsg msg = objectMapper.readValue(textWebSocketFrame.text(), WsMsg.class);
        switch (msg) {
            case PositioningMsg positioningMsg -> positioningMsgHandler.handleMsg(ctx, positioningMsg);
            case WebPanelMsg webPanelMsg -> webPanelMsgHandler.handleMsg(ctx, webPanelMsg);
            default -> throw new IllegalStateException("Unexpected value: " + msg);
        }

        ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + textWebSocketFrame.text()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 添加连接
        log.info("Client connected: " + ctx.channel());
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
}

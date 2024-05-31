package com.fall.nettyctrl.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WebSocket消息处理器
 * @author FAll
 * @date 2024年05月31日 14:27
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) {
        System.out.println("Received message: " + textWebSocketFrame.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + textWebSocketFrame.text()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 添加连接
        System.out.println("Client connected: " + ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 断开连接
        System.out.println("Client disconnected: " + ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 异常处理
        log.error(cause.getMessage());
        ctx.close();
    }
}

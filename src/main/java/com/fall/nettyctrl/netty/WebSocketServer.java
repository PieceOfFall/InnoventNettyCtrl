package com.fall.nettyctrl.netty;

import com.fall.nettyctrl.handler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * WebSocket服务器
 * @author FAll
 * @date 2024年05月31日 14:17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketServer {

    @Value("${netty.websocket-server.port}")
    private Integer websocketServerPort;
    @Value("${netty.websocket-server.prefix}")
    private String prefix;

    private final WebSocketServerHandler webSocketServerHandler;

    @PostConstruct
    public void initServer() throws InterruptedException{

        log.info("websocket server init ...");

        var boss = new NioEventLoopGroup();
        var worker = new NioEventLoopGroup();

        var handlerGroup = new DefaultEventLoopGroup();

        var serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(65536))
                                .addLast(new WebSocketServerProtocolHandler(prefix))
                                .addLast(handlerGroup, webSocketServerHandler);
                    }
                });
        // 绑定端口
        var channelFuture = serverBootstrap.bind(websocketServerPort).sync();
        // 添加关闭事件处理，优雅退出服务
        var closeFuture = channelFuture.channel().closeFuture();
        closeFuture.addListener(future -> {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            handlerGroup.shutdownGracefully();
        });

    }
}

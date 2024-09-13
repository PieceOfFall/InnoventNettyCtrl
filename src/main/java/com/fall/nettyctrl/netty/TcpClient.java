package com.fall.nettyctrl.netty;

import com.fall.nettyctrl.handler.WebSocketServerHandler;
import com.fall.nettyctrl.util.ResponseUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;

/**
 * @author FAll
 * @date 2024年06月05日 13:22
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "ip-name")
public class TcpClient {

    @Value("${netty.tcp-client.timeout}")
    private Integer timeout;

    @Setter
    private LinkedHashMap<String, String> map;

    private final ResponseUtil responseUtil;

    @Autowired
    public TcpClient(ResponseUtil responseUtil) {
        this.responseUtil = responseUtil;
    }

    @Async
    public void sendMsg(String ip, int port, String message) {
        if(!"ping".equals(message)) {
            log.info("[tcp] {}: {}", ip, message);
        }

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = initTcpClientBootStrap(group);
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
            Channel channel = future.channel();
            ByteBuf buffer = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
            channel.writeAndFlush(buffer).sync();
            channel.closeFuture();
        } catch (Exception e) {
            WebSocketServerHandler.broadcastMessage(responseUtil.info("后端发送[{" + map.get(ip) + "}]指令异常"));
            log.error("Error while sending TCP message: ", e);
            Thread.currentThread().interrupt();
        } finally {
            group.shutdownGracefully();
        }
    }

    @Async
    public void sendMsg(String ip, int port, ByteBuf message) {
        log.info("[tcp] {}: {}", ip, ByteBufUtil.hexDump(message));
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = initTcpClientBootStrap(group);
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
            Channel channel = future.channel();

            channel.writeAndFlush(message).sync();
            channel.closeFuture();
        } catch (Exception e) {
            log.error("Error while sending TCP message: ", e);
            Thread.currentThread().interrupt();
        } finally {
            group.shutdownGracefully();
        }
    }

    private Bootstrap initTcpClientBootStrap(NioEventLoopGroup group) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);
        return bootstrap;
    }


    public static ByteBuf hexStringToByteBuf(String hexString) {
        byte[] byteArray = NettySender.hexStringToByteArray(hexString);
        return Unpooled.wrappedBuffer(byteArray);
    }
}

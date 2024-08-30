package com.fall.nettyctrl.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.net.InetSocketAddress;

/**
 * Udp客户端
 *
 * @author FAll
 * @date 2024年06月03日 16:53
 */
@Slf4j
@Configuration
public class UdpClient {

    @Bean
    public Bootstrap initUdpClient() {
        log.info("udp client init ...");

        var group = new NioEventLoopGroup();
        var bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).handler(new ChannelInitializer<DatagramChannel>() {
            @Override
            protected void initChannel(DatagramChannel ch) {
                ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramChannel>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramChannel msg) {
                        // 接收到的udp数据包
                    }
                });
            }

        });

        return bootstrap;
    }


    @Bean
    @Qualifier("udpClient")
    public Channel udpClientChannel(Bootstrap udpBootStrap) throws InterruptedException {
        return udpBootStrap.bind(0).sync().channel();
    }

    @Bean
    @Qualifier("udpSender")
    public NettySender sendUdpMsg(@Qualifier("udpClient") Channel udpClientChannel) {
        return new NettySender() {
            @Async
            @Override
            public void sendMsgAsync(ByteBuf buffer, String ip, Integer port) {
                log.info("[udp] {}: {}",ip, ByteBufUtil.hexDump(buffer));
                udpClientChannel.writeAndFlush(new DatagramPacket(buffer,
                        new InetSocketAddress(ip, port)));
            }

            @Async
            @Override
            public void sendMsgAsync(String message, String ip, Integer port) {
                log.info("[udp] {}: {}",ip,message);
                udpClientChannel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8),
                        new InetSocketAddress(ip, port)));
            }
        };
    }

}

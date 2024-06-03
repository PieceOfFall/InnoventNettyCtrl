package com.fall.nettyctrl.handler;

import com.fall.nettyctrl.vo.WsMsg;
import io.netty.channel.ChannelHandlerContext;

public interface MsgHandler {
    void handleMsg(ChannelHandlerContext ctx, WsMsg msg);
}


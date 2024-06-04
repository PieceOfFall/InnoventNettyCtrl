package com.fall.nettyctrl.handler.msg;

import com.fall.nettyctrl.vo.WsMsg;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 控制面板消息处理器
 *
 * @author FAll
 * @date 2024年06月03日 13:26
 */
@Component
public class WebPanelMsgHandler implements MsgHandler {
    @Override
    public void handleMsg(ChannelHandlerContext ctx, WsMsg msg) {

    }
}

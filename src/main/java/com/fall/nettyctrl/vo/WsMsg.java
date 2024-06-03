package com.fall.nettyctrl.vo;

import lombok.Data;

/**
 * WebSocket消息
 * @author FAll
 * @date 2024年06月03日 13:45
 */
@Data
public abstract class WsMsg {
    public static final String POSITIONING = "positioning";
    public static final String WEB_PANEL = "web-panel";

    private String clientName;
    private Integer clientId;
    private Long timestamp;
}

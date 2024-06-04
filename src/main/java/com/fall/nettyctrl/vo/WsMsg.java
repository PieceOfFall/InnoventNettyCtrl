package com.fall.nettyctrl.vo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * WebSocket消息
 * @author FAll
 * @date 2024年06月03日 13:45
 */
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "clientName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PositioningMsg.class, name = "positioning"),
        @JsonSubTypes.Type(value = WebPanelMsg.class, name = "web-panel")
})
public class WsMsg {
    public static final String POSITIONING = "positioning";
    public static final String WEB_PANEL = "web-panel";

    private String clientName;
    private Integer clientId;
    private Long timestamp;
}

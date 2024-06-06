package com.fall.nettyctrl.vo;

import com.fall.nettyctrl.vo.param.WebPanelMsg;
import com.fall.nettyctrl.vo.positioning.PositioningMsg;
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
        property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PositioningMsg.class, name = "positioning"),
        @JsonSubTypes.Type(value = WebPanelMsg.class, name = "web-panel")
})
public class WsMsg {
    public static final String POSITIONING = "positioning";
    public static final String WEB_PANEL = "web-panel";

    private String clientName;
    private String clientAlias;
    private Integer clientId;
    private Long timestamp;
}

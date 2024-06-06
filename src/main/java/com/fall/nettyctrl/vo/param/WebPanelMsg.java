package com.fall.nettyctrl.vo.param;

import com.fall.nettyctrl.vo.WsMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 控制面板消息
 * @author FAll
 * @date 2024年06月03日 14:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebPanelMsg extends WsMsg {
    private String operation;
    private String target;
    private Object operationParam;
}

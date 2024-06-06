package com.fall.nettyctrl.vo.positioning;

import com.fall.nettyctrl.vo.WsMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 定位消息
 * @author FAll
 * @date 2024年06月03日 14:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PositioningMsg extends WsMsg {
    private Map<Integer,String> posMap;
}

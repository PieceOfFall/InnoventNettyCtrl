package com.fall.nettyctrl.vo.panel;

import lombok.Data;

/**
 * @author FAll
 * @date 2024/9/13 9:51
 */
@Data
public class CommandParam {
    private String ip;
    private Integer port;
    private Boolean isHex;
    private String data;
}

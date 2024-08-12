package com.fall.nettyctrl.vo.panel;

import lombok.Data;

import java.util.List;

/**
 * @author FAll
 * @date 2024/7/31 18:53
 */
@Data
public class BigScreenParam {
    private String id;
    private String ip;
    private Integer port;
    private List<String> poweron;
    private List<String> poweroff;
}

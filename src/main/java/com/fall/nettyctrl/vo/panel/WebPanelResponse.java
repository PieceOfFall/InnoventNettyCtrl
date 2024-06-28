package com.fall.nettyctrl.vo.panel;

import lombok.Data;

/**
 * @author FAll
 * @date 2024年06月24日 9:30
 */
@Data
@SuppressWarnings("unused")
public class WebPanelResponse {
    public static final int ERROR_CODE = 0;
    public static final int SUCCESS_CODE = 1;
    public static final int INFO_CODE = 2;
    public static  final int NOT_ALERT = 3;

    private Integer code;
    private String msg;

    private WebPanelResponse(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static WebPanelResponse success(String msg) {
        return new WebPanelResponse(SUCCESS_CODE,msg);
    }

    public static WebPanelResponse error(String msg) {
        return new WebPanelResponse(ERROR_CODE,msg);
    }

    public static WebPanelResponse info(String msg) {
        return new WebPanelResponse(INFO_CODE,msg);
    }

    public static WebPanelResponse notAlert(String msg) {
        return new WebPanelResponse(NOT_ALERT,msg);
    }

}

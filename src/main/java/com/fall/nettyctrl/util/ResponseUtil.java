package com.fall.nettyctrl.util;

import com.fall.nettyctrl.vo.panel.WebPanelResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author FAll
 * @date 2024年06月24日 9:40
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ResponseUtil {

    private final ObjectMapper mapper;

    public String success(String msg) {
        return toJsonString(WebPanelResponse.success(msg));
    }

    public String error(String msg) {
        return toJsonString(WebPanelResponse.error(msg));
    }

    public String info(String msg) {
        return toJsonString(WebPanelResponse.info(msg));
    }

    public String notAlert(String msg) {
        return toJsonString(WebPanelResponse.notAlert(msg));
    }

    private String toJsonString(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Jackson序列化错误[{}]", e.getMessage());
            throw new RuntimeException(e);
        }
    }


}

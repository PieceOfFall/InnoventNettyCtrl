package com.fall.nettyctrl.handler.msg.panel;

import com.fall.nettyctrl.netty.TcpClient;
import com.fall.nettyctrl.vo.panel.ComputerParam;
import com.fall.nettyctrl.vo.panel.WebPanelMsg;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 关机消息处理器
 *
 * @author FAll
 * @date 2024年06月06日 11:04
 */
@Component
@SuppressWarnings("unchecked")
@ConfigurationProperties(prefix = "web-panel.computer.poweroff")
public class PowerOffHandler implements IOperationHandler {

    @Setter
    private List<LinkedHashMap<String, String>> list;
    private final TcpClient tcpClient;

    @Autowired
    PowerOffHandler(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        var operationParam = (Map<String, String>) webPanelMsg.getOperationParam();
        var computerParam = new ComputerParam(operationParam.get("type"), operationParam.get("name"));

        for (LinkedHashMap<String, String> typeMap : list) {
            if (typeMap.get("type").equals(computerParam.getType())) {
                String targetIp = typeMap.get(computerParam.getName());
                tcpClient.sendMsg(
                        targetIp,
                        Integer.parseInt(typeMap.get("port")),
                        "shutdown");
            }
        }

    }
}

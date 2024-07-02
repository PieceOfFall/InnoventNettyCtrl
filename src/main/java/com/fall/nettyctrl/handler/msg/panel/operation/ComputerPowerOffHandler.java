package com.fall.nettyctrl.handler.msg.panel.operation;

import com.fall.nettyctrl.handler.msg.panel.IOperationHandler;
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
import java.util.function.Predicate;

/**
 * 关机消息处理器
 *
 * @author FAll
 * @date 2024年06月06日 11:04
 */
@Component
@SuppressWarnings("unchecked")
@ConfigurationProperties(prefix = "web-panel.computer.poweroff")
public class ComputerPowerOffHandler implements IOperationHandler {

    @Setter
    private List<LinkedHashMap<String, String>> list;
    private final TcpClient tcpClient;

    @Autowired
    ComputerPowerOffHandler(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void handleOperation(WebPanelMsg webPanelMsg) {
        var operationParam = (Map<String, String>) webPanelMsg.getOperationParam();
        var computerParam = new ComputerParam(operationParam.get("type"), operationParam.get("name"));
        var type = computerParam.getType();

        if("hosts".equals(type)) {
            // 过滤得到所有host相关的map的list
            var hostList = getFilteredMapListExclude("host", "leaderHost");

            loopToShutdown(hostList);
        } else if("integrated".equals(type)) {
            // 过滤得到所有integrated相关的map的list
            var integratedList = getFilteredMapListExclude("global", "medicine");

            loopToShutdown(integratedList);
        }

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

    private List<LinkedHashMap<String, String>>  getFilteredMapListExclude(String typeName1,String typeName2) {
        Predicate<LinkedHashMap<String, String>> predicate = map-> map.get("type").equals(typeName1)
                || map.get("type").equals(typeName2);
        return list
                .stream()
                .filter(predicate)
                .toList();
    }


    private void loopToShutdown(List<LinkedHashMap<String, String>> machineList) {
        for (LinkedHashMap<String, String> machines : machineList) {
            // map获取每个map下的所有machine的ip
            var machineIps = machines.entrySet()
                    .stream()
                    .filter(set-> !set.getKey().equals("port")
                            && !set.getKey().equals("type"))
                    .map(Map.Entry::getValue)
                    .toList();
            // 遍历关机所有machine
            var targetPort = machines.get("port");
            for (String machineIp : machineIps)
                tcpClient.sendMsg(machineIp, Integer.parseInt(targetPort),"shutdown");
        }
   }

}

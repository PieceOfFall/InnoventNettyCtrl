package com.fall.nettyctrl.vo.positioning;

import lombok.Data;

import java.util.List;

/**
 * @author FAll
 * @date 2024年06月04日 10:09
 */
@Data
public class MediaCommand {
    private Integer id;
    private List<String> high;
    private List<String> medium;
    private List<String> low;
    private List<String> off;

    public List<String> getCommand(String strength) {
        return switch (strength) {
            case "off" -> off;
            case "low" -> low;
            case "medium" -> medium;
            case "high" -> high;
            default -> throw new IllegalStateException("Unexpected value: " + strength);
        };
    }
}

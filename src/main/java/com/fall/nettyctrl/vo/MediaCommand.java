package com.fall.nettyctrl.vo;

import lombok.Data;

/**
 * @author FAll
 * @date 2024年06月04日 10:09
 */
@Data
public class MediaCommand {
    private Integer id;
    private String high;
    private String medium;
    private String low;
    private String off;

    public String getCommand(String strength) {
        return switch (strength) {
            case "off" -> off;
            case "low" -> low;
            case "medium" -> medium;
            case "high" -> high;
            default -> throw new IllegalStateException("Unexpected value: " + strength);
        };
    }
}

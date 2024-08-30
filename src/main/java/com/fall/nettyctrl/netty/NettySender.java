package com.fall.nettyctrl.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public interface NettySender {
    void sendMsgAsync(ByteBuf buffer, String ip, Integer port);

    void sendMsgAsync(String message, String ip, Integer port);

    static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    static ByteBuf hexStringToByteBuf(String hexString) {
        byte[] byteArray = hexStringToByteArray(hexString);
        return Unpooled.wrappedBuffer(byteArray);
    }
}

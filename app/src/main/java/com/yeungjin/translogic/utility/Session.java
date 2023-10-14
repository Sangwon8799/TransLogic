package com.yeungjin.translogic.utility;

import com.yeungjin.translogic.object.CHAT;
import com.yeungjin.translogic.object.EMPLOYEE;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Session {
    public static EMPLOYEE USER;
    public static CHAT CHAT;

    public static int width;
    public static int height;

    public static Socket socket;

    static {
        try {
            socket = IO.socket("http://152.70.237.174:5000");
        } catch (Exception error) {
            error.printStackTrace();
        }
        socket.connect();
    }
}

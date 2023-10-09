package com.yeungjin.translogic.utility;

import com.yeungjin.translogic.object.CHAT;
import com.yeungjin.translogic.object.EMPLOYEE;

import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Session {
    private static final Session instance = new Session();

    public static EMPLOYEE user;
    public static CHAT entered_chat = new CHAT();
    public static ArrayList<Long> joined_chat = new ArrayList<>();

    public static Socket socket;

    public static int width;
    public static int height;

    private Session() {
        try {
            socket = IO.socket("http://152.70.237.174:5000");
        } catch (Exception error) {
            error.printStackTrace();
        }
        socket.connect();
    }
}

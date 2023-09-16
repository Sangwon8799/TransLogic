package com.yeungjin.translogic.object.chat;

public class MessageInfo {
    public String sender;
    public String message;
    public String time;
    public MessageType type;

    public MessageInfo(String sender, String message, String time, MessageType type) {
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.type = type;
    }
}

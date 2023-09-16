package com.yeungjin.translogic.object.chat;

public class MessageData {
    public String type;
    public String from;
    public String to;
    public String content;
    public long time;

    public MessageData(String type, String from, String to, String content, long time) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.content = content;
        this.time = time;
    }
}

package com.yeungjin.translogic.object.chat;

import java.util.Date;

public class Message {
    public int type;
    public int number;
    public String name;
    public String content;
    public Date time;

    public Message(int type, int number, String name, String content, Date time) {
        this.type = type;
        this.number = number;
        this.name = name;
        this.content = content;
        this.time = time;
    }
}

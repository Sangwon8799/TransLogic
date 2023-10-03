package com.yeungjin.translogic.object.chat;

import com.yeungjin.translogic.adapter.chat.MessageAdapter;

import java.util.Date;

public class Message {
    public int type;
    public long number;
    public String name;
    public String content;
    public Date time;

    public Message(int type, long number, String name, String content) {
        this.type = type;
        this.number = number;
        this.name = name;
        this.content = content;
        this.time = new Date(System.currentTimeMillis());
    }

    public Message(long number, String name, String content) {
        this(MessageAdapter.MYSELF, number, name, content);
    }
}

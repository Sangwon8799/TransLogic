package com.yeungjin.translogic.layout.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.MessageAdapter;
import com.yeungjin.translogic.object.chat.MessageData;
import com.yeungjin.translogic.object.chat.MessageInfo;
import com.yeungjin.translogic.object.chat.MessageType;
import com.yeungjin.translogic.object.chat.RoomData;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RoomLayout extends AppCompatActivity {
    private MessageAdapter messageAdapter;

    private RecyclerView messageList;
    private EditText message;
    private Button send;

    private Gson gson;
    private Socket socket;

    private String username;
    private String roomNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_room);
        init();

        messageList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        messageList.setAdapter(messageAdapter);

        connect();
    }

    private void init() {
        messageList = (RecyclerView) findViewById(R.id.layout_chat_room__message_list);
        message = (EditText) findViewById(R.id.layout_chat_room__message);
        send = (Button) findViewById(R.id.layout_chat_room__send);

        messageAdapter = new MessageAdapter();

        gson = new Gson();
    }

    private void connect() {
        try {
            socket = IO.socket("http://152.70.237.174:5000");
        } catch (Exception error) {
            error.printStackTrace();
        }

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        roomNumber = intent.getStringExtra("roomNumber");

        socket.on("update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
                System.out.println(args[0].toString());
                addChat(data);
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("enter", gson.toJson(new RoomData(username, roomNumber)));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        socket.connect();
    }

    private void sendMessage() {
        MessageData data = new MessageData("MESSAGE", username, roomNumber, message.getText().toString(), System.currentTimeMillis());
        socket.emit("newMessage", gson.toJson(data));
        messageAdapter.addItem(new MessageInfo(data.from, data.content, toDate(data.time), MessageType.MYSELF));
        message.setText("");
    }

    private void addChat(MessageData data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data.type.equals("ENTER") || data.type.equals("LEFT")) {
                    messageAdapter.addItem(new MessageInfo(data.from, data.content, toDate(data.time), MessageType.NOTICE));
                } else {
                    messageAdapter.addItem(new MessageInfo(data.from, data.content, toDate(data.time), MessageType.OPPONENT));
                }
                messageList.scrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }

    private String toDate(long currentMillis) {
        return new SimpleDateFormat("a hh:mm").format(new Date(currentMillis));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("left", gson.toJson(new RoomData(username, roomNumber)));
        socket.disconnect();
    }
}

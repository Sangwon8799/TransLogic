package com.yeungjin.translogic.layout.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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

    private DrawerLayout layout;
    private RecyclerView messageList;
    private ImageButton previous;
    private TextView title;
    private ImageButton menu;
    private ImageButton upload;
    private EditText message;
    private ImageButton send;

    private Gson gson;
    private Socket socket;

    private String name;
    private String roomNumber;
    private String roomTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_room);
        init();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText(roomTitle);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!layout.isDrawerOpen(GravityCompat.END)) {
                    layout.openDrawer(GravityCompat.END);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 코드 추가
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().isEmpty()) {
                    sendMessage();
                }
            }
        });

        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("left", gson.toJson(new RoomData(name, roomNumber, roomTitle)));
        socket.disconnect();
    }

    private void init() {
        layout = (DrawerLayout) findViewById(R.id.layout_chat_room__layout);
        messageList = (RecyclerView) findViewById(R.id.layout_chat_room__message_list);
        previous = (ImageButton) findViewById(R.id.layout_chat_room__previous);
        title = (TextView) findViewById(R.id.layout_chat_room__title);
        menu = (ImageButton) findViewById(R.id.layout_chat_room__menu);
        upload = (ImageButton) findViewById(R.id.layout_chat_room__upload);
        message = (EditText) findViewById(R.id.layout_chat_room__message);
        send = (ImageButton) findViewById(R.id.layout_chat_room__send);

        messageAdapter = new MessageAdapter();
        messageList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        messageList.setAdapter(messageAdapter);

        gson = new Gson();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        roomNumber = intent.getStringExtra("number");
        roomTitle = intent.getStringExtra("title");
    }

    private void connect() {
        try {
            socket = IO.socket("http://152.70.237.174:5000");
        } catch (Exception error) {
            error.printStackTrace();
        }

        socket.connect();

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("enter", gson.toJson(new RoomData(name, roomNumber, roomTitle)));
            }
        });

        socket.on("update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
                addChat(data);
            }
        });
    }

    private void sendMessage() {
        MessageData data = new MessageData("MESSAGE", name, roomNumber, message.getText().toString(), System.currentTimeMillis());
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
}

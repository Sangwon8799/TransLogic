package com.yeungjin.translogic.layout.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.MessageAdapter;
import com.yeungjin.translogic.layout.CommonActivity;
import com.yeungjin.translogic.object.chat.Message;
import com.yeungjin.translogic.object.chat.Room;

import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RoomLayout extends CommonActivity {
    private DrawerLayout side;        // 메뉴창
    private RecyclerView messageList; // 메시지 목록
    private ImageButton previous;     // 뒤로가기
    private TextView title;           // 채팅방 제목
    private ImageButton menu;         // 메뉴 버튼
    private ImageButton upload;       // 업로드 버튼
    private EditText message;         // 메시지 입력창
    private ImageButton send;         // 전송 버튼

    private MessageAdapter messageAdapter; // 메시지 목록 어댑터

    private Gson gson;     // JSON 변환용 필드
    private Socket socket; // 소켓 통신용 필드

    private String name;      // 사용자 이름
    private int roomNumber;   // 채팅방 번호
    private String roomTitle; // 채팅방 제목

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_room);
        init();

        gson = new Gson();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        roomNumber = intent.getIntExtra("number", 0);
        roomTitle = intent.getStringExtra("title");

        title.setText(roomTitle);

        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.emit("LEAVE", gson.toJson(new Room(name, roomNumber, roomTitle)));
        socket.disconnect();
    }

    @Override
    protected void setId() {
        side = findViewById(R.id.layout_chat_room__layout);
        messageList = findViewById(R.id.layout_chat_room__message_list);
        previous = findViewById(R.id.layout_chat_room__previous);
        title = findViewById(R.id.layout_chat_room__title);
        menu = findViewById(R.id.layout_chat_room__menu);
        upload = findViewById(R.id.layout_chat_room__upload);
        message = findViewById(R.id.layout_chat_room__message);
        send = findViewById(R.id.layout_chat_room__send);
    }

    @Override
    protected void setAdapter() {
        messageAdapter = new MessageAdapter();

        messageList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        messageList.setAdapter(messageAdapter);
    }

    @Override
    protected void setListener() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!side.isDrawerOpen(GravityCompat.END)) {
                    side.openDrawer(GravityCompat.END);
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
                    Message data = new Message(MessageAdapter.MYSELF, roomNumber, name, message.getText().toString(), new Date(System.currentTimeMillis()));
                    socket.emit("MESSAGE", gson.toJson(data));

                    messageAdapter.addItem(data);
                    message.setText("");
                }
            }
        });
    }

    private void connect() {
        try {
            socket = IO.socket("http://152.70.237.174:5000");
        } catch (Exception error) {
            error.printStackTrace();
        }

        socket.connect();
        // 채팅방 진입시 해당 채팅방에 미리 들어가있는 사람들에게 출력될 문구
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("ENTER", gson.toJson(new Room(name, roomNumber, roomTitle)));
            }
        });

        // 상대방이 채팅을 보내면 해당 채팅을 불러오는 기능
        socket.on("UPDATE", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Message data = gson.fromJson(args[0].toString(), Message.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.addItem(data);
                        messageList.scrollToPosition(messageAdapter.getItemCount() - 1);
                    }
                });
            }
        });
    }
}

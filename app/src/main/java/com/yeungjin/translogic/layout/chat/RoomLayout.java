package com.yeungjin.translogic.layout.chat;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.yeungjin.translogic.object.database.CHAT;
import com.yeungjin.translogic.utility.BitmapTranslator;
import com.yeungjin.translogic.utility.Session;

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
    private EditText content;         // 메시지 입력창
    private ImageButton send;         // 전송 버튼

    private MessageAdapter messageAdapter; // 메시지 목록 어댑터

    private final Gson gson = new Gson(); // JSON 변환용 필드
    private Socket socket;                // 소켓 통신용 필드

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia; // 이미지 불러오기 라이브러리
    private static final int MAX_SIZE = 512;                          // 업로드할 이미지의 가장 긴 부분의 길이

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_room);
        init();

        title.setText(Session.chat.CHAT_TITLE);

        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.emit("LEAVE", gson.toJson(new Room(Session.user.EMPLOYEE_NAME, Session.chat.CHAT_NUMBER, Session.chat.CHAT_TITLE)));
        socket.disconnect();

        Session.chat = new CHAT();
    }

    @Override
    protected void setId() {
        side = findViewById(R.id.layout_chat_room__layout);
        messageList = findViewById(R.id.layout_chat_room__message_list);
        previous = findViewById(R.id.layout_chat_room__previous);
        title = findViewById(R.id.layout_chat_room__title);
        menu = findViewById(R.id.layout_chat_room__menu);
        upload = findViewById(R.id.layout_chat_room__upload);
        content = findViewById(R.id.layout_chat_room__content);
        send = findViewById(R.id.layout_chat_room__send);
    }

    @Override
    protected void setAdapter() {
        messageAdapter = new MessageAdapter(getApplicationContext());

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
                pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!content.getText().toString().isEmpty()) {
                    Message message = new Message(Session.chat.CHAT_NUMBER, Session.user.EMPLOYEE_NAME, content.getText().toString());
                    sendMessage(message);

                    content.setText("");
                }
            }
        });
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    Bitmap image = null;

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            image = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
                        } else {
                            image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        }
                    } catch (Exception error) {
                        error.printStackTrace();
                    }

                    Ratio ratio = Ratio.getRatio(image);
                    Bitmap resized = Bitmap.createScaledBitmap(image, ratio.width, ratio.height, true);

                    Message message = new Message(Session.chat.CHAT_NUMBER, Session.user.EMPLOYEE_NAME, BitmapTranslator.toBase64(resized));
                    sendMessage(message);
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

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("ENTER", gson.toJson(new Room(Session.user.EMPLOYEE_NAME, Session.chat.CHAT_NUMBER, Session.chat.CHAT_TITLE)));
            }
        });

        socket.on("UPDATE", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Message message = gson.fromJson(args[0].toString(), Message.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.addMessage(message);
                        messageList.scrollToPosition(messageAdapter.getItemCount() - 1);
                    }
                });
            }
        });
    }

    private void sendMessage(Message message) {
        socket.emit("MESSAGE", gson.toJson(message));

        messageAdapter.addMessage(message);
        messageList.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private static class Ratio {
        public int width;
        public int height;

        private Ratio() { }

        public static Ratio getRatio(Bitmap image) {
            Ratio ratio = new Ratio();

            int result = Math.min(image.getWidth(), image.getHeight()) * MAX_SIZE / Math.max(image.getWidth(), image.getHeight());

            if (image.getWidth() >= image.getHeight()) {
                ratio.width = MAX_SIZE;
                ratio.height = result;
            } else {
                ratio.width = result;
                ratio.height = MAX_SIZE;
            }

            return ratio;
        }
    }
}

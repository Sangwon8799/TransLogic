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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.RoomAdapter;
import com.yeungjin.translogic.layout.CommonActivity;
import com.yeungjin.translogic.object.view.MESSAGE_INFO;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.utility.Image;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;
import java.util.Objects;

import io.socket.emitter.Emitter;

public class RoomLayout extends CommonActivity {
    private DrawerLayout menu;
    private RecyclerView menu_list;
    private RecyclerView message_list;
    private ImageButton previous;
    private TextView title;
    private ImageButton open_menu;
    private ImageButton upload;
    private EditText content;
    private ImageButton send;

    private RoomAdapter.MenuAdapter menu_adapter;
    private RoomAdapter message_adapter;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_room);
        init();

        title.setText(Session.CHAT.CHAT_TITLE);
        message_list.scrollToPosition(message_adapter.getItemCount() - 1);

        Session.socket.on("get_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MESSAGE_INFO message = Json.from(args[0], MESSAGE_INFO.class);

                if (message.MESSAGE_EMPLOYEE_NUMBER != Session.USER.EMPLOYEE_NUMBER) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message_adapter.addMessage(message);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        new DBThread("RefreshLastAccess", new HashMap<String, Object>() {{
            put("chat_number", Session.CHAT.CHAT_NUMBER);
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
        }});
        Session.CHAT = null;
    }

    @Override
    protected void setId() {
        menu = findViewById(R.id.layout_chat_room__menu);
        menu_list = findViewById(R.id.layout_chat_room_menu__list);
        message_list = findViewById(R.id.layout_chat_room__message_list);
        previous = findViewById(R.id.layout_chat_room__previous);
        title = findViewById(R.id.layout_chat_room__title);
        open_menu = findViewById(R.id.layout_chat_room__open_menu);
        upload = findViewById(R.id.layout_chat_room__upload);
        content = findViewById(R.id.layout_chat_room__content);
        send = findViewById(R.id.layout_chat_room__send);
    }

    @Override
    protected void setAdapter() {
        menu_adapter = new RoomAdapter.MenuAdapter(getApplicationContext());
        message_adapter = new RoomAdapter(getApplicationContext());

        menu_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        menu_list.setAdapter(menu_adapter);

        message_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        message_list.setAdapter(message_adapter);
    }

    @Override
    protected void setListener() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        open_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!menu.isDrawerOpen(GravityCompat.END)) {
                    menu.openDrawer(GravityCompat.END);
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
                content.clearFocus();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _content = content.getText().toString();

                if (!_content.isEmpty()) {
                    MESSAGE_INFO message = new MESSAGE_INFO(Session.CHAT.CHAT_NUMBER, Session.USER.EMPLOYEE_NUMBER, Session.USER.EMPLOYEE_NAME, _content);
                    sendMessage(message);

                    new DBVolley(getApplicationContext(), "CreateMessage", new HashMap<String, Object>() {{
                        put("chat_number", message.MESSAGE_CHAT_NUMBER);
                        put("employee_number", message.MESSAGE_EMPLOYEE_NUMBER);
                        put("content", message.MESSAGE_CONTENT);
                    }});

                    content.setText(null);
                }
            }
        });
        message_adapter.listener = new RoomAdapter.Listener() {
            @Override
            public void scroll(int position) {
                message_list.scrollToPosition(position);
            }
        };
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

                    Image.Ratio ratio = Image.Ratio.getRatio(Objects.requireNonNull(image));
                    Bitmap resized = Bitmap.createScaledBitmap(image, ratio.width, ratio.height, true);

                    MESSAGE_INFO message = new MESSAGE_INFO(Session.CHAT.CHAT_NUMBER, Session.USER.EMPLOYEE_NUMBER, Session.USER.EMPLOYEE_NAME, Image.toBase64(resized));
                    sendMessage(message);

                    new DBVolley(getApplicationContext(), "CreateMessage", new HashMap<String, Object>() {{
                        put("chat_number", message.MESSAGE_CHAT_NUMBER);
                        put("employee_number", message.MESSAGE_EMPLOYEE_NUMBER);
                        put("base64", message.MESSAGE_CONTENT);
                    }});
                }
            }
        });
    }

    private void sendMessage(@NonNull MESSAGE_INFO message) {
        Session.socket.emit("send_message", Json.to(message));
        message_adapter.addMessage(message);
    }
}

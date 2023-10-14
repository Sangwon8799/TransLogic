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
import com.yeungjin.translogic.object.MESSAGE;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.utility.Image;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;
import java.util.Objects;

import io.socket.emitter.Emitter;

public class RoomLayout extends CommonActivity {
    private DrawerLayout side;
    private RecyclerView message_list;
    private ImageButton previous;
    private TextView title;
    private ImageButton menu;
    private ImageButton upload;
    private EditText content;
    private ImageButton send;

    private RoomAdapter adapter;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_room);
        init();

        title.setText(Session.CHAT.CHAT_TITLE);
        message_list.scrollToPosition(adapter.getItemCount() - 1);

        Session.socket.on("get_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MESSAGE message = Json.from(args[0], MESSAGE.class);

                if (message.MESSAGE_EMPLOYEE_NUMBER != Session.USER.EMPLOYEE_NUMBER) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addMessage(message);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        new DBVolley(getApplicationContext(), "RefreshLastAccess", new HashMap<String, Object>() {{
            put("chat_number", Session.CHAT.CHAT_NUMBER);
            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
        }});

        Session.CHAT = null;
    }

    @Override
    protected void setId() {
        side = findViewById(R.id.layout_chat_room__layout);
        message_list = findViewById(R.id.layout_chat_room__message_list);
        previous = findViewById(R.id.layout_chat_room__previous);
        title = findViewById(R.id.layout_chat_room__title);
        menu = findViewById(R.id.layout_chat_room__menu);
        upload = findViewById(R.id.layout_chat_room__upload);
        content = findViewById(R.id.layout_chat_room__content);
        send = findViewById(R.id.layout_chat_room__send);
    }

    @Override
    protected void setAdapter() {
        adapter = new RoomAdapter(getApplicationContext());

        message_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        message_list.setAdapter(adapter);
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
                content.clearFocus();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _content = content.getText().toString();

                if (!_content.isEmpty()) {
                    MESSAGE message = new MESSAGE(Session.CHAT.CHAT_NUMBER, Session.USER.EMPLOYEE_NUMBER, Session.USER.EMPLOYEE_NAME, _content);
                    sendMessage(message);

                    new DBVolley(getApplicationContext(), "CreateMessage", new HashMap<String, Object>() {{
                        put("chat_number", message.MESSAGE_CHAT_NUMBER);
                        put("employee_number", message.MESSAGE_EMPLOYEE_NUMBER);
                        put("employee_name", message.MESSAGE_EMPLOYEE_NAME);
                        put("content", message.MESSAGE_CONTENT);
                    }});

                    content.setText(null);
                }
            }
        });
        adapter.listener = new RoomAdapter.Listener() {
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

                    Ratio ratio = Ratio.getRatio(Objects.requireNonNull(image));
                    Bitmap resized = Bitmap.createScaledBitmap(image, ratio.width, ratio.height, true);

                    MESSAGE message = new MESSAGE(Session.CHAT.CHAT_NUMBER, Session.USER.EMPLOYEE_NUMBER, Session.USER.EMPLOYEE_NAME, Image.toBase64(resized));
                    sendMessage(message);

                    new DBVolley(getApplicationContext(), "CreateMessage", new HashMap<String, Object>() {{
                        put("chat_number", message.MESSAGE_CHAT_NUMBER);
                        put("employee_number", message.MESSAGE_EMPLOYEE_NUMBER);
                        put("employee_name", message.MESSAGE_EMPLOYEE_NAME);
                        put("base64", message.MESSAGE_CONTENT);
                    }});
                }
            }
        });
    }

    private void sendMessage(@NonNull MESSAGE message) {
        Session.socket.emit("send_message", Json.to(message));
        adapter.addMessage(message);
    }

    private static class Ratio {
        private static final int MAX_SIZE = 512;

        public int width;
        public int height;

        private Ratio() { }

        @NonNull
        public static Ratio getRatio(@NonNull Bitmap image) {
            Ratio ratio = new Ratio();

            int width = image.getWidth();
            int height = image.getHeight();

            int max_size = Math.min(Math.max(width, height), width > height ? MAX_SIZE : MAX_SIZE * 2);
            int result = Math.min(width, height) * max_size / Math.max(width, height);

            if (width >= height) {
                ratio.width = max_size;
                ratio.height = result;
            } else {
                ratio.width = result;
                ratio.height = max_size;
            }

            return ratio;
        }
    }
}

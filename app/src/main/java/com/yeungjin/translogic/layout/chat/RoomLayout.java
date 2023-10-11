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
import com.yeungjin.translogic.object.CHAT;
import com.yeungjin.translogic.object.MESSAGE;
import com.yeungjin.translogic.utility.DBVolley;
import com.yeungjin.translogic.utility.Image;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;

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

        title.setText(Session.entered_chat.CHAT_TITLE);
        message_list.scrollToPosition(adapter.getItemCount() - 1);

        Session.socket.on("GET_MESSAGE", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MESSAGE message = Json.from(args[0], MESSAGE.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addMessage(message);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        new DBVolley(getApplicationContext(), "RefreshLastAccess", new HashMap<String, Object>() {{
            put("chat_number", Session.entered_chat.CHAT_NUMBER);
            put("employee_number", Session.user.EMPLOYEE_NUMBER);
        }});

        Session.entered_chat = new CHAT();
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
                    MESSAGE message = new MESSAGE(Session.entered_chat.CHAT_NUMBER, Session.user.EMPLOYEE_NUMBER, Session.user.EMPLOYEE_NAME, _content);
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

                    assert image != null;
                    Ratio ratio = Ratio.getRatio(image);
                    Bitmap resized = Bitmap.createScaledBitmap(image, ratio.width, ratio.height, true);

                    MESSAGE message = new MESSAGE(Session.entered_chat.CHAT_NUMBER, Session.user.EMPLOYEE_NUMBER, Session.user.EMPLOYEE_NAME, Image.toBase64(resized));
                    sendMessage(message);

                    new DBVolley(getApplicationContext(), "InsertChatImage", new HashMap<String, Object>() {{
                        put("chat_number", Session.entered_chat.CHAT_NUMBER);
                        put("employee_number", Session.user.EMPLOYEE_NUMBER);
                        put("employee_name", Session.user.EMPLOYEE_NAME);
                        put("base64", Image.toBase64(resized));
                    }});
                }
            }
        });
    }

    private void sendMessage(@NonNull MESSAGE message) {
        Session.socket.emit("SEND_MESSAGE", Json.to(message));
        adapter.addMessage(message);
    }

    private static class Ratio {
        public int width;
        public int height;

        private Ratio() { }

        @NonNull
        public static Ratio getRatio(@NonNull Bitmap image) {
            Ratio ratio = new Ratio();

            final int MAX_SIZE = Math.min(Math.max(image.getWidth(), image.getHeight()), image.getWidth() > image.getHeight() ? 512 : 1024);
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

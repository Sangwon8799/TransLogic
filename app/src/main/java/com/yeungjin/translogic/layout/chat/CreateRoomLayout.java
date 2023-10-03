package com.yeungjin.translogic.layout.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.ChatAdapter;
import com.yeungjin.translogic.layout.CommonDialog;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.chat.CreateChatRequest;

public class CreateRoomLayout extends CommonDialog {
    private TextView create;
    private EditText title;

    private ChatAdapter chatAdapter;

    public CreateRoomLayout(@NonNull Context context, ChatAdapter chatAdapter, int width) {
        super(context);
        setContentView(R.layout.layout_chat_create_room);
        this.chatAdapter = chatAdapter;
        init();

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width * 8 / 10;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void setId() {
        create = findViewById(R.id.layout_chat_create_room__create);
        title = findViewById(R.id.layout_chat_create_room__title);
    }
    
    @Override
    protected void setListener() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty()) {
                    CreateChatRequest request = new CreateChatRequest(title.getText().toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("true")) {
                                chatAdapter.reload();
                                Toast.makeText(getContext(), "채팅방이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                Toast.makeText(getContext(), "생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Request.sendRequest(context, request);
                } else {
                    Toast.makeText(getContext(), "이름을 입력하고 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    title.requestFocus();
                }
            }
        });
    }
}

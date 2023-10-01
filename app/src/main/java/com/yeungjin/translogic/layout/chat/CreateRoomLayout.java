package com.yeungjin.translogic.layout.chat;

import android.app.Dialog;
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
import com.android.volley.toolbox.Volley;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.ChatAdapter;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.chat.CreateChatRequest;

public class CreateRoomLayout extends Dialog {
    private TextView create;
    private EditText title;

    public CreateRoomLayout(@NonNull Context context, ChatAdapter chatAdapter, int width) {
        super(context);
        setContentView(R.layout.layout_chat_create_room);
        init(width);

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
                    Request.queue = Volley.newRequestQueue(getContext());
                    Request.queue.add(request);
                } else {
                    Toast.makeText(getContext(), "이름을 입력하고 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    title.requestFocus();
                }
            }
        });
    }

    private void init(int width) {
        create = (TextView) findViewById(R.id.layout_chat_create_room__create);
        title = (EditText) findViewById(R.id.layout_chat_create_room__title);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width * 8 / 10;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}

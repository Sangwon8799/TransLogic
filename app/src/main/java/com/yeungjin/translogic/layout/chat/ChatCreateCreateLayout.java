package com.yeungjin.translogic.layout.chat;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.layout.CommonDialog;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.chat.CreateChatRequest;
import com.yeungjin.translogic.utility.Session;

public class ChatCreateCreateLayout extends CommonDialog {
    private OnCreateListener listener;

    private TextView create;
    private EditText title;

    public ChatCreateCreateLayout(Context context) {
        super(context);
        setContentView(R.layout.layout_chat_chat_create_create);
        init(8, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void setId() {
        create = findViewById(R.id.layout_chat_chat_create_create__create);
        title = findViewById(R.id.layout_chat_chat_create_create__title);
    }

    @Override
    protected void setListener() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _title = title.getText().toString();

                if (!_title.isEmpty()) {
                    Request request = new CreateChatRequest(_title, Session.user.EMPLOYEE_NUMBER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.contains("null")) {
                                if (listener != null) {
                                    listener.create(Long.parseLong(response.trim()));
                                }
                            } else {
                                Toast.makeText(view.getContext(), "채팅 만들기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Request.sendRequest(context, request);
                }
            }
        });
    }

    public void setOnCreateListener(OnCreateListener listener) {
        this.listener = listener;
    }

    public interface OnCreateListener {
        void create(long chat_number);
    }
}

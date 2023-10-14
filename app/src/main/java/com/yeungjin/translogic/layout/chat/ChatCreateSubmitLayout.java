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
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;
import java.util.Objects;

public class ChatCreateSubmitLayout extends CommonDialog {
    private TextView create;
    private EditText title;

    public Listener listener;

    public ChatCreateSubmitLayout(Context context) {
        super(context);
        setContentView(R.layout.layout_chat_chat_create_submit);
        init(8, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void setId() {
        create = findViewById(R.id.layout_chat_chat_create_submit__create);
        title = findViewById(R.id.layout_chat_chat_create_submit__title);
    }

    @Override
    protected void setListener() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _title = title.getText().toString();

                if (!_title.isEmpty()) {
                    new DBVolley(context, "CreateChat", new HashMap<String, Object>() {{
                        put("title", _title);
                        put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                    }}, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.contains("null")) {
                                Objects.requireNonNull(listener).create(Long.parseLong(response.trim()));
                            } else {
                                Toast.makeText(view.getContext(), "채팅 만들기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public interface Listener {
        void create(long chat_number);
    }
}

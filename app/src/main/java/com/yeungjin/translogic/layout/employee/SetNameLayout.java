package com.yeungjin.translogic.layout.employee;

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
import com.yeungjin.translogic.request.employee.CreateGroupRequest;
import com.yeungjin.translogic.request.employee.IsGroupNameUniqueRequest;
import com.yeungjin.translogic.utility.Session;

public class SetNameLayout extends CommonDialog {
    private OnCreateListener listener;

    private TextView create;
    private EditText name;

    public SetNameLayout(Context context) {
        super(context);
        setContentView(R.layout.layout_employee_set_name);
        init(9, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void setId() {
        create = findViewById(R.id.layout_employee_set_name__create);
        name = findViewById(R.id.layout_employee_set_name__name);
    }

    @Override
    protected void setListener() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _name = name.getText().toString();

                if (!_name.isEmpty()) {
                    Request request = new IsGroupNameUniqueRequest(_name, Session.user.EMPLOYEE_NUMBER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("true")) {
                                Request request = new CreateGroupRequest(_name, Session.user.EMPLOYEE_NUMBER, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (!response.contains("null")) {
                                            if (listener != null) {
                                                listener.create(Long.parseLong(response.trim()));
                                            }
                                        } else {
                                            Toast.makeText(view.getContext(), "그룹 만들기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                Request.sendRequest(context, request);
                            } else {
                                Toast.makeText(view.getContext(), "이미 존재하는 그룹명입니다. 다른 이름으로 변경해주세요.", Toast.LENGTH_SHORT).show();
                                name.requestFocus();
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
        void create(long group_number);
    }
}

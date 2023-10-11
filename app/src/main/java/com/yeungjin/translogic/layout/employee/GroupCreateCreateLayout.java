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
import com.yeungjin.translogic.utility.DBVolley;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;

public class GroupCreateCreateLayout extends CommonDialog {
    private TextView create;
    private EditText name;

    public Listener listener;

    public GroupCreateCreateLayout(Context context) {
        super(context);
        setContentView(R.layout.layout_employee_group_create_create);
        init(8, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void setId() {
        create = findViewById(R.id.layout_employee_group_create_create__create);
        name = findViewById(R.id.layout_employee_group_create_create__name);
    }

    @Override
    protected void setListener() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _name = name.getText().toString();

                if (!_name.isEmpty()) {
                    new DBVolley(context, "IsGroupNameUnique", new HashMap<String, Object>() {{
                        put("name", _name);
                        put("employee_number", Session.user.EMPLOYEE_NUMBER);
                    }}, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("true")) {
                                new DBVolley(context, "CreateGroup", new HashMap<String, Object>() {{
                                    put("name", _name);
                                    put("employee_number", Session.user.EMPLOYEE_NUMBER);
                                }}, new Response.Listener<String>() {
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
                            } else {
                                Toast.makeText(view.getContext(), "이미 존재하는 그룹명입니다. 다른 이름으로 변경해주세요.", Toast.LENGTH_SHORT).show();
                                name.requestFocus();
                            }
                        }
                    });
                }
            }
        });
    }

    public interface Listener {
        void create(long group_number);
    }
}

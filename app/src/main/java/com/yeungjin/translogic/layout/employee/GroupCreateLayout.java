package com.yeungjin.translogic.layout.employee;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.employee.GroupCreateAdapter;
import com.yeungjin.translogic.layout.CommonBottomSheetDialogFragment;
import com.yeungjin.translogic.layout.CommonDialog;
import com.yeungjin.translogic.object.view.EMPLOYEE_INFO;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;
import java.util.Objects;

public class GroupCreateLayout extends CommonBottomSheetDialogFragment {
    private TextView create;
    private EditText search;
    private ImageButton clear;
    private RecyclerView selected_list;
    private RecyclerView unselected_list;

    private GroupCreateAdapter.SelectedAdapter selected_adapter;
    private GroupCreateAdapter.UnselectedAdapter unselected_adapter;

    public Listener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_employee_group_create, container, false);
        init();

        return view;
    }

    @Override
    protected void setId() {
        create = view.findViewById(R.id.layout_employee_group_create__create);
        search = view.findViewById(R.id.layout_employee_group_create__search);
        clear = view.findViewById(R.id.layout_employee_group_create__clear);
        selected_list = view.findViewById(R.id.layout_employee_group_create__selected);
        unselected_list = view.findViewById(R.id.layout_employee_group_create__unselected);
    }

    @Override
    protected void setAdapter() {
        selected_adapter = new GroupCreateAdapter.SelectedAdapter(requireContext());
        unselected_adapter = new GroupCreateAdapter.UnselectedAdapter(requireContext());

        selected_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        selected_list.setAdapter(selected_adapter);

        unselected_list.setLayoutManager(new LinearLayoutManager(getContext()));
        unselected_list.setAdapter(unselected_adapter);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void setListener() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_adapter.getItemCount() != 0) {
                    SubmitLayout dialog = new SubmitLayout(view.getContext());
                    dialog.listener = new SubmitLayout.Listener() {
                        @Override
                        public void create(long group_number) {
                            try {
                                for (long employee_number : selected_adapter.getNumber()) {
                                    new DBVolley(view.getContext(), "InsertGroupMember", new HashMap<String, Object>() {{
                                        put("group_number", group_number);
                                        put("employee_number", employee_number);
                                    }});
                                }

                                Toast.makeText(view.getContext(), "그룹이 생성되었습니다!", Toast.LENGTH_SHORT).show();
                                Objects.requireNonNull(listener).load();

                                dismiss();
                                dialog.dismiss();
                            } catch (Exception error) {
                                error.printStackTrace();
                            }
                        }
                    };
                    dialog.show();
                } else {
                    Toast.makeText(view.getContext(), "그룹으로 추가할 사람을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence content, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence content, int start, int before, int count) {
                if (!content.toString().isEmpty()) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
                unselected_adapter.reload(content);
            }

            @Override
            public void afterTextChanged(Editable content) { }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText(null);
            }
        });
        selected_adapter.listener = new GroupCreateAdapter.SelectedAdapter.Listener() {
            @Override
            public void remove(long employee_number) {
                if (selected_adapter.getItemCount() == 0) {
                    selected_list.setVisibility(View.GONE);
                }
                unselected_adapter.remove(employee_number);
            }

            @Override
            public void scroll() {
                selected_list.scrollToPosition(0);
            }
        };
        unselected_adapter.listener = new GroupCreateAdapter.UnselectedAdapter.Listener() {
            @Override
            public void check(EMPLOYEE_INFO employee) {
                if (selected_list.getVisibility() == View.GONE) {
                    selected_list.setVisibility(View.VISIBLE);
                }
                selected_adapter.check(employee);
            }

            @Override
            public void uncheck(long number) {
                if (selected_adapter.getItemCount() == 1) {
                    selected_list.setVisibility(View.GONE);
                }
                selected_adapter.uncheck(number);
            }
        };
        unselected_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                view.onTouchEvent(event);
                return true;
            }
        });
    }

    public interface Listener {
        void load();
    }

    private static class SubmitLayout extends CommonDialog {
        private TextView create;
        private EditText name;

        public Listener listener;

        public SubmitLayout(@NonNull Context context) {
            super(context);
            setContentView(R.layout.layout_employee_group_create_submit);
            init(7, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        @Override
        protected void setId() {
            create = findViewById(R.id.layout_employee_group_create_submit__create);
            name = findViewById(R.id.layout_employee_group_create_submit__name);
        }

        @Override
        protected void setListener() {
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!name.getText().toString().isEmpty()) {
                        new DBVolley(context, "IsGroupNameUnique", new HashMap<String, Object>() {{
                            put("name", name.getText().toString());
                            put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                        }}, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("true")) {
                                    new DBVolley(context, "CreateGroup", new HashMap<String, Object>() {{
                                        put("name", name.getText().toString());
                                        put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                                    }}, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (!response.contains("null")) {
                                                Objects.requireNonNull(listener).create(Long.parseLong(response.trim()));
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
                    } else {
                        Toast.makeText(view.getContext(), "채팅방 이름을 설정해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public interface Listener {
            void create(long group_number);
        }
    }
}

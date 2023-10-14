package com.yeungjin.translogic.layout.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.ChatCreateAdapter;
import com.yeungjin.translogic.layout.CommonBottomSheetDialogFragment;
import com.yeungjin.translogic.object.EMPLOYEE;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;
import java.util.Objects;

public class ChatCreateLayout extends CommonBottomSheetDialogFragment {
    private TextView create;
    private EditText search;
    private ImageButton clear;
    private RecyclerView selected_list;
    private RecyclerView unselected_list;

    private ChatCreateAdapter.SelectedAdapter selected_adapter;
    private ChatCreateAdapter.UnselectedAdapter unselected_adapter;

    public Listener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_chat_chat_create, container, false);
        init();

        return view;
    }

    @Override
    protected void setId() {
        create = view.findViewById(R.id.layout_chat_chat_create__create);
        search = view.findViewById(R.id.layout_chat_chat_create__search);
        clear = view.findViewById(R.id.layout_chat_chat_create__clear);
        selected_list = view.findViewById(R.id.layout_chat_chat_create__selected);
        unselected_list = view.findViewById(R.id.layout_chat_chat_create__unselected);
    }

    @Override
    protected void setAdapter() {
        selected_adapter = new ChatCreateAdapter.SelectedAdapter(requireContext());
        unselected_adapter = new ChatCreateAdapter.UnselectedAdapter(requireContext());

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
                    ChatCreateSubmitLayout dialog = new ChatCreateSubmitLayout(view.getContext());
                    dialog.listener = new ChatCreateSubmitLayout.Listener() {
                        @Override
                        public void create(long chat_number) {
                            for (long employee_number : selected_adapter.getNumber()) {
                                new DBVolley(view.getContext(), "InsertChatMember", new HashMap<String, Object>() {{
                                    put("chat_number", chat_number);
                                    put("employee_number", employee_number);
                                }});
                                Session.socket.emit("create_chat", chat_number, employee_number);
                            }

                            Toast.makeText(view.getContext(), "채팅방이 생성되었습니다!", Toast.LENGTH_SHORT).show();
                            Objects.requireNonNull(listener).load();

                            dismiss();
                            dialog.dismiss();
                        }
                    };
                    dialog.show();
                } else {
                    Toast.makeText(view.getContext(), "채팅방에 추가할 인원을 선택해주세요.", Toast.LENGTH_SHORT).show();
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
        selected_adapter.listener = new ChatCreateAdapter.SelectedAdapter.Listener() {
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
        unselected_adapter.listener = new ChatCreateAdapter.UnselectedAdapter.Listener() {
            @Override
            public void check(EMPLOYEE employee) {
                if (selected_list.getVisibility() == View.GONE) {
                    selected_list.setVisibility(View.VISIBLE);
                }
                selected_adapter.check(employee);
            }

            @Override
            public void uncheck(long employee_number) {
                if (selected_adapter.getItemCount() == 1) {
                    selected_list.setVisibility(View.GONE);
                }
                selected_adapter.uncheck(employee_number);
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
}

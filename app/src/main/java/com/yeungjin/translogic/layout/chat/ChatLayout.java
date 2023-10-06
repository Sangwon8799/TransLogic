package com.yeungjin.translogic.layout.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.ChatAdapter;
import com.yeungjin.translogic.layout.CommonFragment;

public class ChatLayout extends CommonFragment {
    private EditText search;            // 검색창
    private ImageButton clear;          // 검색어 지우기
    private ImageButton createRoom;     // 채팅 생성
    private RecyclerView chatList;      // 채팅 목록
    private SwipeRefreshLayout refresh; // 갱신

    private ChatAdapter chatAdapter; // 채팅 목록 어댑터

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_chat_chat, container, false);
        init();

        return view;
    }

    @Override
    protected void setId() {
        search = view.findViewById(R.id.layout_chat_chat__search);
        clear = view.findViewById(R.id.layout_chat_chat__clear);
        createRoom = view.findViewById(R.id.layout_chat_chat__create_room);
        chatList = view.findViewById(R.id.layout_chat_chat__chat_list);
        refresh = view.findViewById(R.id.layout_chat_chat__refresh);
    }

    @Override
    protected void setAdapter() {
        chatAdapter = new ChatAdapter(getActivity().getApplicationContext());

        chatList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        chatList.setAdapter(chatAdapter);
    }

    @Override
    protected void setListener() {
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
                chatAdapter.reload(content.toString());
            }

            @Override
            public void afterTextChanged(Editable content) { }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRoomLayout dialog = new CreateRoomLayout(v.getContext(), chatAdapter);
                dialog.show();
            }
        });
        chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void click(Intent intent) {
                startActivity(intent);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chatAdapter.reload();
                refresh.setRefreshing(false);
            }
        });
    }
}

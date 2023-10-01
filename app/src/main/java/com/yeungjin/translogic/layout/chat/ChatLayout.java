package com.yeungjin.translogic.layout.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.ChatAdapter;

public class ChatLayout extends Fragment {
    private ChatAdapter chatAdapter;

    private ImageButton createRoom;
    private EditText search;
    private ImageButton clear;
    private RecyclerView chatList;
    private SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_chat_chat, container, false);
        init(view);

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
                DisplayMetrics metrics = getResources().getDisplayMetrics();

                CreateRoomLayout dialog = new CreateRoomLayout(v.getContext(), chatAdapter, metrics.widthPixels);
                dialog.show();
            }
        });

        chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void execute(Intent intent) {
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

        return view;
    }

    private void init(View view) {
        createRoom = (ImageButton) view.findViewById(R.id.layout_chat_chat__create_room);
        search = (EditText) view.findViewById(R.id.layout_chat_chat__search);
        clear = (ImageButton) view.findViewById(R.id.layout_chat_chat__clear);
        chatList = (RecyclerView) view.findViewById(R.id.layout_chat_chat__list);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.layout_chat_chat__refresh);

        chatAdapter = new ChatAdapter(getActivity().getApplicationContext());

        chatList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        chatList.setAdapter(chatAdapter);

        chatAdapter.load();
    }
}

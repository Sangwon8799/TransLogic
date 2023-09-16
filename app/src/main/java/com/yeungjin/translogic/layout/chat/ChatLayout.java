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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.ChatAdapter;

public class ChatLayout extends Fragment {
    private ChatAdapter chatAdapter;

    private EditText search;
    private ImageButton clear;
    private RecyclerView chatList;

    private int index = 0;

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
                chatAdapter.getSearchedChat(content.toString(), index = 0);
            }

            @Override
            public void afterTextChanged(Editable content) { }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                chatAdapter.getChat(index = 0);
            }
        });

        chatList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        chatList.setAdapter(chatAdapter);

        chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void execute(Intent intent) {
                startActivity(intent);
            }
        });
        chatAdapter.getChat(index++);

        return view;
    }

    private void init(View view) {
        clear = (ImageButton) view.findViewById(R.id.layout_chat_chat__clear);
        search = (EditText) view.findViewById(R.id.layout_chat_chat__search);
        chatList = (RecyclerView) view.findViewById(R.id.layout_chat_chat__list);

        chatAdapter = new ChatAdapter(getActivity().getApplicationContext());
    }
}

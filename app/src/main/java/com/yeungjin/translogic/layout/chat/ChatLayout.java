package com.yeungjin.translogic.layout.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.chat.ChatAdapter;
import com.yeungjin.translogic.layout.CommonFragment;

public class ChatLayout extends CommonFragment {
    private EditText search;
    private ImageButton clear;
    private ImageButton chat_management;
    private RecyclerView chat_list;
    private SwipeRefreshLayout refresh;

    private ChatAdapter chat_adapter;

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
        chat_management = view.findViewById(R.id.layout_chat_chat__create_room);
        chat_list = view.findViewById(R.id.layout_chat_chat__chat_list);
        refresh = view.findViewById(R.id.layout_chat_chat__refresh);
    }

    @Override
    protected void setAdapter() {
        chat_adapter = new ChatAdapter(requireActivity().getApplicationContext());

        chat_list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        chat_list.setAdapter(chat_adapter);
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
                chat_adapter.reload(content.toString());
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
        chat_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                requireActivity().getMenuInflater().inflate(R.menu.layout_chat_chat__menu_icon, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.layout_chat_chat__menu_icon__create) {
                            ChatCreateLayout layout = new ChatCreateLayout();
                            layout.setOnLoadListener(new ChatCreateLayout.OnLoadListener() {
                                @Override
                                public void load() {
                                    chat_adapter.reload();
                                }
                            });
                            layout.show(getParentFragmentManager(), layout.getTag());
                        } else if (item.getItemId() == R.id.layout_chat_chat__menu_icon__edit) {
                            Toast.makeText(view.getContext(), "개발중", Toast.LENGTH_SHORT).show();
                        } else {
                            return false;
                        }

                        return true;
                    }
                });
                menu.show();
            }
        });
        chat_adapter.setOnClickListener(new ChatAdapter.OnClickListener() {
            @Override
            public void click(Intent intent) {
                startActivity(intent);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chat_adapter.reload();
                refresh.setRefreshing(false);
            }
        });
    }
}

package com.yeungjin.translogic.layout.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class Chat extends Fragment {
   private com.yeungjin.translogic.adapter.chatting.Chatting chat;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.layout_chat_chat, container, false);

      EditText search = (EditText) view.findViewById(R.id.layout_chat_chat__search);
      search.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence content, int start, int count, int after) { }

         @Override
         public void onTextChanged(CharSequence content, int start, int before, int count) {
            // 내용 추가 필요
         }

         @Override
         public void afterTextChanged(Editable content) { }
      });

      chat = new com.yeungjin.translogic.adapter.chatting.Chatting();
      RecyclerView chatList = (RecyclerView) view.findViewById(R.id.layout_chat_chat__list);
      chatList.setLayoutManager(new LinearLayoutManager(view.getContext()));
      chatList.setAdapter(chat);

      return view;
   }
}

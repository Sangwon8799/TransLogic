package com.yeungjin.translogic.display.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class Layout extends Fragment {
   private com.yeungjin.translogic.adapter.chat.Chat chat;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.display_chat_layout, container, false);

      EditText search = (EditText) view.findViewById(R.id.display_chat_layout__search);
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

      chat = new com.yeungjin.translogic.adapter.chat.Chat();
      RecyclerView chatList = (RecyclerView) view.findViewById(R.id.display_chat_layout__list);
      chatList.setLayoutManager(new LinearLayoutManager(view.getContext()));
      chatList.setAdapter(chat);

      return view;
   }
}

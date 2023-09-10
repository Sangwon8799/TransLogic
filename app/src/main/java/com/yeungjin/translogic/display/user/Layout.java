package com.yeungjin.translogic.display.user;

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
   private com.yeungjin.translogic.adapter.user.User user;
   private com.yeungjin.translogic.adapter.user.Group group;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.display_user_layout, container, false);

      EditText search = (EditText) view.findViewById(R.id.display_user_layout__search);
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

      user = new com.yeungjin.translogic.adapter.user.User();
      RecyclerView userList = (RecyclerView) view.findViewById(R.id.display_user_layout__list);
      userList.setLayoutManager(new LinearLayoutManager(view.getContext()));
      userList.setAdapter(user);

      RecyclerView groupList = (RecyclerView) view.findViewById(R.id.display_user_layout__group);
      groupList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
      group = new com.yeungjin.translogic.adapter.user.Group(); // 람다 함수 적용해야 함
      groupList.setAdapter(group);

      return view;
   }

   public interface Connector {
      void execute(String groupName);
   }
}

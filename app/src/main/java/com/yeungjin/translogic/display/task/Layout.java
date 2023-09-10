package com.yeungjin.translogic.display.task;

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
   private com.yeungjin.translogic.adapter.task.User user;
   private com.yeungjin.translogic.adapter.task.Filter filter;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.display_task_layout, container, false);

      EditText search = (EditText) view.findViewById(R.id.display_task_layout__search);
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

      user = new com.yeungjin.translogic.adapter.task.User();
      RecyclerView userList = (RecyclerView) view.findViewById(R.id.display_task_layout__list);
      userList.setLayoutManager(new LinearLayoutManager(view.getContext()));
      userList.setAdapter(user);

      RecyclerView filterList = (RecyclerView) view.findViewById(R.id.display_task_layout__filter);
      filterList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
      filter = new com.yeungjin.translogic.adapter.task.Filter(); // 람다 함수 적용해야 함
      filterList.setAdapter(filter);

      return view;
   }
}

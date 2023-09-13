package com.yeungjin.translogic.layout.task;

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
import com.yeungjin.translogic.adapter.task.Employee;

public class Task extends Fragment {
   private Employee user;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.layout_task_task, container, false);

      EditText search = (EditText) view.findViewById(R.id.layout_task_task__search);
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

      user = new Employee();
      RecyclerView userList = (RecyclerView) view.findViewById(R.id.layout_task_task__list);
      userList.setLayoutManager(new LinearLayoutManager(view.getContext()));
      userList.setAdapter(user);

      return view;
   }
}

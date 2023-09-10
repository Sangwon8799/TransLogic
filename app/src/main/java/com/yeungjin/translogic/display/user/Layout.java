package com.yeungjin.translogic.display.user;

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
import com.yeungjin.translogic.adapter.user.GroupAdapter;
import com.yeungjin.translogic.adapter.user.ListAdapter;

public class Layout extends Fragment {
   private ListAdapter listAdapter;
   private GroupAdapter groupAdapter;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.display_user_layout, container, false);

      EditText search = (EditText) view.findViewById(R.id.display_user_user__search);
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

      listAdapter = new ListAdapter();
      RecyclerView list = (RecyclerView) view.findViewById(R.id.display_user_user__list);
      list.setLayoutManager(new LinearLayoutManager(view.getContext()));
      list.setAdapter(listAdapter);

      RecyclerView group = (RecyclerView) view.findViewById(R.id.display_user_user__group);
      group.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
      groupAdapter = new GroupAdapter(); // 람다 함수 적용해야 함
      group.setAdapter(groupAdapter);

      return view;
   }

   public interface Connector {
      void execute(String groupName);
   }
}

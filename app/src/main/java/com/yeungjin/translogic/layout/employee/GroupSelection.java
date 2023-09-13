package com.yeungjin.translogic.layout.employee;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.employee.GroupSelected;
import com.yeungjin.translogic.adapter.employee.GroupUnselected;

public class GroupSelection extends BottomSheetDialogFragment {
   private com.yeungjin.translogic.adapter.employee.GroupSelected selected;
   private com.yeungjin.translogic.adapter.employee.GroupUnselected unselected;

   @Override
   @SuppressLint("ClickableViewAccessibility")
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.layout_employee_group_selection, container, false);

      TextView setName = (TextView) view.findViewById(R.id.layout_employee_group_selection__set_name);
      setName.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // 추후 내용 추가
         }
      });

      EditText search = (EditText) view.findViewById(R.id.layout_employee_group_selection__search);
      search.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 추후 내용 추가
         }

         @Override
         public void afterTextChanged(Editable s) { }
      });

      unselected = new GroupUnselected();
      RecyclerView unselectedList = (RecyclerView) view.findViewById(R.id.layout_employee_group_selection__unselected);
      unselectedList.setLayoutManager(new LinearLayoutManager(view.getContext()));
      unselectedList.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
               case MotionEvent.ACTION_DOWN:
                  v.getParent().requestDisallowInterceptTouchEvent(true);
                  break;
               case MotionEvent.ACTION_UP:
                  v.getParent().requestDisallowInterceptTouchEvent(false);
                  break;
            }
            v.onTouchEvent(event);
            return true;
         }
      });
//      unselectedList.setAdapter(unselected);

      selected = new GroupSelected();
      RecyclerView selectedList = (RecyclerView) view.findViewById(R.id.layout_employee_group_selection__selected);
      selectedList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
//      selectedList.setAdapter(selected);

      return view;
   }

   private interface Linker {
      void add();
      void remove();
   }
}

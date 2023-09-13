package com.yeungjin.translogic.layout.employee;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class Employee extends Fragment {
   private com.yeungjin.translogic.adapter.employee.Employee employee;
   private com.yeungjin.translogic.adapter.employee.Group group;
   private com.yeungjin.translogic.layout.employee.GroupSelection groupSelection;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.layout_employee_employee, container, false);

      EditText search = (EditText) view.findViewById(R.id.layout_employee_employee__search);
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

      TextView createGroup = (TextView) view.findViewById(R.id.layout_employee_employee__create_group);
      createGroup.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            groupSelection.show(getParentFragmentManager(), groupSelection.getTag());
         }
      });

      employee = new com.yeungjin.translogic.adapter.employee.Employee();
      RecyclerView employeeList = (RecyclerView) view.findViewById(R.id.layout_employee_employee__list);
      employeeList.setLayoutManager(new LinearLayoutManager(view.getContext()));
      employeeList.setAdapter(employee);

      RecyclerView groupList = (RecyclerView) view.findViewById(R.id.layout_employee_employee__group);
      groupList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
      group = new com.yeungjin.translogic.adapter.employee.Group(); // 람다 함수 적용해야 함
      groupList.setAdapter(group);

      groupSelection = new GroupSelection();

      return view;
   }

   public interface Connector {
      void execute(String groupName);
   }
}

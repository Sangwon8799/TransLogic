package com.yeungjin.translogic.layout.task;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.task.EmployeeAdapter;
import com.yeungjin.translogic.layout.CommonFragment;

public class TaskLayout extends CommonFragment {
    private EditText search;           // 검색창
    private ImageButton clear;         // 검색어 지우기
    private ImageButton setFilter;     // 필터창
    private RecyclerView employeeList; // 직원 목록

    private EmployeeAdapter employeeAdapter; // 직원 목록 어댑터

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_task_task, container, false);
        init();

        return view;
    }

    @Override
    protected void setId() {
        setFilter = view.findViewById(R.id.layout_task_task__set_filter);
        search = view.findViewById(R.id.layout_task_task__search);
        clear = view.findViewById(R.id.layout_task_task__clear);
        employeeList = view.findViewById(R.id.layout_task_task__employee_list);
    }

    @Override
    protected void setAdapter() {
        employeeAdapter = new EmployeeAdapter(getActivity().getApplicationContext());

        employeeList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        employeeList.setAdapter(employeeAdapter);
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
            }

            @Override
            public void afterTextChanged(Editable content) { }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
        setFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterLayout dialog = new FilterLayout(v.getContext());
                dialog.show();
            }
        });
    }
}

package com.yeungjin.translogic.layout.employee;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.employee.EmployeeAdapter;
import com.yeungjin.translogic.adapter.employee.GroupAdapter;
import com.yeungjin.translogic.layout.CommonFragment;

public class EmployeeLayout extends CommonFragment {
    private EditText search;            // 검색창
    private ImageButton clear;          // 검색어 지우기
    private ImageButton createGroup;    // 그룹 생성
    private RecyclerView employeeList;  // 직원 목록
    private RecyclerView groupList;     // 그룹 목록
    private SwipeRefreshLayout refresh; // 갱신

    private EmployeeAdapter employeeAdapter; // 직원 목록 어댑터
    private GroupAdapter groupAdapter;       // 그룹 목록 어댑터

    private GroupSelectionLayout groupSelectionLayout; // 그룹 생성 레이아웃

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_employee_employee, container, false);
        init();

        groupSelectionLayout = new GroupSelectionLayout();

        return view;
    }

    @Override
    protected void setId() {
        search = view.findViewById(R.id.layout_employee_employee__search);
        clear = view.findViewById(R.id.layout_employee_employee__clear);
        createGroup = view.findViewById(R.id.layout_employee_employee__create_group);
        employeeList = view.findViewById(R.id.layout_employee_employee__employee_list);
        groupList = view.findViewById(R.id.layout_employee_employee__group_list);
        refresh = view.findViewById(R.id.layout_employee_employee__refresh);
    }

    @Override
    protected void setAdapter() {
        employeeAdapter = new EmployeeAdapter(getActivity().getApplicationContext());
        groupAdapter = new GroupAdapter();

        employeeList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        employeeList.setAdapter(employeeAdapter);

        groupList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        groupList.setAdapter(groupAdapter);
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
                employeeAdapter.reload(content.toString());
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
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupSelectionLayout.show(getParentFragmentManager(), groupSelectionLayout.getTag());
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                employeeAdapter.reload();
                refresh.setRefreshing(false);
            }
        });
    }
}

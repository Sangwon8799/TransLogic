package com.yeungjin.translogic.layout.employee;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.employee.EmployeeAdapter;
import com.yeungjin.translogic.adapter.employee.GroupAdapter;

public class EmployeeLayout extends Fragment {
    private EmployeeAdapter employeeAdapter;
    private GroupAdapter groupAdapter;

    private EditText search;
    private ImageButton clear;
    private ImageButton createGroup;
    private RecyclerView employeeList;
    private RecyclerView groupList;
    private SwipeRefreshLayout refresh;

    private GroupSelectionLayout groupSelectionLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_employee_employee, container, false);
        init(view);

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

        return view;
    }

    private void init(View view) {
        search = (EditText) view.findViewById(R.id.layout_employee_employee__search);
        clear = (ImageButton) view.findViewById(R.id.layout_employee_employee__clear);
        createGroup = (ImageButton) view.findViewById(R.id.layout_employee_employee__create_group);
        employeeList = (RecyclerView) view.findViewById(R.id.layout_employee_employee__list);
        groupList = (RecyclerView) view.findViewById(R.id.layout_employee_employee__group);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.layout_employee_employee__refresh);

        employeeAdapter = new EmployeeAdapter(getActivity().getApplicationContext());
        groupAdapter = new GroupAdapter();

        groupSelectionLayout = new GroupSelectionLayout();

        employeeList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        employeeList.setAdapter(employeeAdapter);

        groupList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        groupList.setAdapter(groupAdapter);

        employeeAdapter.load();
    }
}

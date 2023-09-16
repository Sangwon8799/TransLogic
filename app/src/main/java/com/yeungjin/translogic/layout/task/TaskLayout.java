package com.yeungjin.translogic.layout.task;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.task.EmployeeAdapter;

public class TaskLayout extends Fragment {
    private EmployeeAdapter user;

    private EditText search;
    private ImageButton clear;
    private TextView setFilter;
    private RecyclerView userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_task_task, container, false);
        init(view);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence content, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence content, int start, int before, int count) {
                if (!content.toString().isEmpty()) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable content) {
            }
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
                DisplayMetrics metrics = getResources().getDisplayMetrics();

                FilterLayout filterLayout = new FilterLayout(v.getContext(), metrics.widthPixels, metrics.heightPixels);
                filterLayout.show();
            }
        });

        userList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userList.setAdapter(user);

        return view;
    }

    private void init(View view) {
        search = (EditText) view.findViewById(R.id.layout_task_task__search);
        clear = (ImageButton) view.findViewById(R.id.layout_task_task__clear);
        setFilter = (TextView) view.findViewById(R.id.layout_task_task__set_filter);
        userList = (RecyclerView) view.findViewById(R.id.layout_task_task__list);

        user = new EmployeeAdapter();
    }
}

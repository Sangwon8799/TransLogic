package com.yeungjin.translogic.layout.employee;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.employee.GroupEditAdapter;
import com.yeungjin.translogic.layout.CommonDialog;

public class GroupEditLayout extends CommonDialog {
    private RecyclerView list;

    private GroupEditAdapter adapter;

    public GroupEditLayout(Context context) {
        super(context);
        setContentView(R.layout.layout_employee_group_edit);
        init(9, 8);
    }

    @Override
    protected void setId() {
        list = findViewById(R.id.layout_employee_group_edit__list);
    }

    @Override
    protected void setAdapter() {
        adapter = new GroupEditAdapter(context);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
    }

    @Override
    protected void setListener() {

    }
}

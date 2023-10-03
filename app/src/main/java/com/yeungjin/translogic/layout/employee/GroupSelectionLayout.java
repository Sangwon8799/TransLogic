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
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.employee.GroupSelectedAdapter;
import com.yeungjin.translogic.adapter.employee.GroupUnselectedAdapter;
import com.yeungjin.translogic.layout.CommonBottomSheetDialogFragment;

public class GroupSelectionLayout extends CommonBottomSheetDialogFragment {
    private GroupSelectedAdapter selected;
    private GroupUnselectedAdapter unselected;

    private TextView setName;
    private EditText search;
    private ImageButton clear;
    private RecyclerView unselectedList;
    private RecyclerView selectedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_employee_group_selection, container, false);
        init();

        return view;
    }

    @Override
    protected void setId() {
        setName = view.findViewById(R.id.layout_employee_group_selection__set_name);
        search = view.findViewById(R.id.layout_employee_group_selection__search);
        clear = view.findViewById(R.id.layout_employee_group_selection__clear);
        unselectedList = view.findViewById(R.id.layout_employee_group_selection__unselected);
        selectedList = view.findViewById(R.id.layout_employee_group_selection__selected);
    }

    @Override
    protected void setAdapter() {
        unselected = new GroupUnselectedAdapter();
        selected = new GroupSelectedAdapter();

        unselectedList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        unselectedList.setAdapter(unselected);

        selectedList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        selectedList.setAdapter(selected);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void setListener() {
        setName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추후 내용 추가
            }
        });
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
    }
}

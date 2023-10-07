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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.employee.GroupCreateSelectedAdapter;
import com.yeungjin.translogic.adapter.employee.GroupCreateUnselectedAdapter;
import com.yeungjin.translogic.layout.CommonBottomSheetDialogFragment;
import com.yeungjin.translogic.object.database.EMPLOYEE;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.employee.InsertGroupMemberRequest;

public class GroupCreateLayout extends CommonBottomSheetDialogFragment {
    private GroupCreateSelectedAdapter selected;
    private GroupCreateUnselectedAdapter unselected;

    private TextView set_name;
    private EditText search;
    private ImageButton clear;
    private RecyclerView selected_list;
    private RecyclerView unselected_list;

    private OnLoadListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_employee_group_create, container, false);
        init();

        return view;
    }

    @Override
    protected void setId() {
        set_name = view.findViewById(R.id.layout_employee_group_create__set_name);
        search = view.findViewById(R.id.layout_employee_group_create__search);
        clear = view.findViewById(R.id.layout_employee_group_create__clear);
        selected_list = view.findViewById(R.id.layout_employee_group_create__selected);
        unselected_list = view.findViewById(R.id.layout_employee_group_create__unselected);
    }

    @Override
    protected void setAdapter() {
        selected = new GroupCreateSelectedAdapter(requireActivity().getApplicationContext());
        unselected = new GroupCreateUnselectedAdapter(requireActivity().getApplicationContext());

        selected_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        selected_list.setAdapter(selected);

        unselected_list.setLayoutManager(new LinearLayoutManager(getContext()));
        unselected_list.setAdapter(unselected);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void setListener() {
        set_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected.getItemCount() != 0) {
                    GroupCreateSetNameLayout dialog = new GroupCreateSetNameLayout(view.getContext());
                    dialog.setOnCreateListener(new GroupCreateSetNameLayout.OnCreateListener() {
                        @Override
                        public void create(long group_number) {
                            try {
                                for (long employee_number : selected.getNumbers()) {
                                    Request request = new InsertGroupMemberRequest(group_number, employee_number);
                                    Request.sendRequest(view.getContext(), request);
                                }

                                Toast.makeText(view.getContext(), "그룹이 생성되었습니다!", Toast.LENGTH_SHORT).show();
                                if (listener != null) {
                                    listener.load();
                                }

                                dismiss();
                                dialog.dismiss();
                            } catch (Exception error) {
                                error.printStackTrace();
                            }
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(view.getContext(), "그룹으로 추가할 사람을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
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
                unselected.reload(content);
            }

            @Override
            public void afterTextChanged(Editable content) { }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText(null);
            }
        });
        selected.setOnRemoveListener(new GroupCreateSelectedAdapter.OnRemoveListener() {
            @Override
            public void remove(long number) {
                if (selected.getItemCount() == 0) {
                    selected_list.setVisibility(View.GONE);
                }
                unselected.remove(number);
            }
        });
        selected.setOnScrollListener(new GroupCreateSelectedAdapter.OnScrollListener() {
            @Override
            public void scroll() {
                selected_list.scrollToPosition(0);
            }
        });
        unselected.setOnCheckListener(new GroupCreateUnselectedAdapter.OnCheckListener() {
            @Override
            public void check(EMPLOYEE employee) {
                if (selected_list.getVisibility() == View.GONE) {
                    selected_list.setVisibility(View.VISIBLE);
                }
                selected.check(employee);
            }

            @Override
            public void uncheck(long number) {
                if (selected.getItemCount() == 1) {
                    selected_list.setVisibility(View.GONE);
                }
                selected.uncheck(number);
            }
        });
        unselected_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                view.onTouchEvent(event);
                return true;
            }
        });
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.listener = listener;
    }

    public interface OnLoadListener {
        void load();
    }
}

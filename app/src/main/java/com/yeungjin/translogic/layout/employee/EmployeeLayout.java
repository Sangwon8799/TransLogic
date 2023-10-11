package com.yeungjin.translogic.layout.employee;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.employee.EmployeeAdapter;
import com.yeungjin.translogic.adapter.employee.GroupAdapter;
import com.yeungjin.translogic.layout.CommonFragment;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.utility.Session;

import java.util.HashMap;

public class EmployeeLayout extends CommonFragment {
    private EditText search;
    private ImageButton clear;
    private ImageButton group_management;
    private RecyclerView employee_list;
    private RecyclerView group_list;
    private SwipeRefreshLayout refresh;

    private EmployeeAdapter employee_adapter;
    private GroupAdapter group_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_employee_employee, container, false);
        init();

        if (group_adapter.getItemCount() != 0) {
            group_list.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    protected void setId() {
        search = view.findViewById(R.id.layout_employee_employee__search);
        clear = view.findViewById(R.id.layout_employee_employee__clear);
        group_management = view.findViewById(R.id.layout_employee_employee__group_management);
        employee_list = view.findViewById(R.id.layout_employee_employee__employee_list);
        group_list = view.findViewById(R.id.layout_employee_employee__group_list);
        refresh = view.findViewById(R.id.layout_employee_employee__refresh);
    }

    @Override
    protected void setAdapter() {
        employee_adapter = new EmployeeAdapter(requireContext());
        group_adapter = new GroupAdapter(requireContext());

        employee_list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        employee_list.setAdapter(employee_adapter);

        group_list.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        group_list.setAdapter(group_adapter);
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

                if (group_adapter.isSelected()) {
                    employee_adapter.reload(group_adapter.getNumber(), content.toString());
                } else {
                    employee_adapter.reload(content.toString());
                }
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
        group_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                requireActivity().getMenuInflater().inflate(R.menu.layout_employee_employee__menu_icon, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.layout_employee_employee__menu_icon__create) {
                            new DBVolley(getContext(), "IsGroupMax", new HashMap<String, Object>() {{
                                put("employee_number", Session.user.EMPLOYEE_NUMBER);
                            }}, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("true")) {
                                        GroupCreateLayout layout = new GroupCreateLayout();
                                        layout.listener = new GroupCreateLayout.Listener() {
                                            @Override
                                            public void load() {
                                                group_adapter.reload();
                                                if (group_list.getVisibility() == View.GONE) {
                                                    group_list.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        };
                                        layout.show(getParentFragmentManager(), layout.getTag());
                                    } else {
                                        Toast.makeText(getContext(), "생성 가능한 그룹 최대 개수(10개)에 도달하여 그룹을 생성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else if (item.getItemId() == R.id.layout_employee_employee__menu_icon__edit) {
                            GroupEditLayout dialog = new GroupEditLayout(getContext());
                            dialog.show();
                        } else {
                            return false;
                        }

                        return true;
                    }
                });
                menu.show();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                employee_adapter.reload();
                refresh.setRefreshing(false);
            }
        });
        group_adapter.listener = new GroupAdapter.Listener() {
            @Override
            public void select(long group_number) {
                if (employee_list.getScrollState() != RecyclerView.SCROLL_STATE_DRAGGING) {
                    String _search = search.getText().toString();

                    if (!_search.isEmpty()) {
                        employee_adapter.reload(group_number, _search);
                    } else {
                        employee_adapter.reload(group_number);
                    }
                }
            }

            @Override
            public void unselect() {
                if (employee_list.getScrollState() != RecyclerView.SCROLL_STATE_DRAGGING) {
                    String _search = search.getText().toString();

                    if (!_search.isEmpty()) {
                        employee_adapter.reload(_search);
                    } else {
                        employee_adapter.reload();
                    }
                }
            }
        };
    }
}

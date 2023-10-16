package com.yeungjin.translogic.adapter.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonAdapter;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.view.EMPLOYEE_INFO;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.Server;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.utility.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GroupCreateAdapter {
    private GroupCreateAdapter() { }

    public static class SelectedAdapter extends CommonAdapter<EMPLOYEE_INFO, SelectedAdapter.ViewHolder> {
        public Listener listener;

        public SelectedAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_create_selected, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EMPLOYEE_INFO employee = data.get(position);

            Glide.with(holder.image.getContext()).load(Server.IMAGE_URL + employee.EMPLOYEE_IMAGE).into(holder.image);
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(employee);
                    Objects.requireNonNull(listener).remove(employee.EMPLOYEE_NUMBER);
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
            holder.name.setText(employee.EMPLOYEE_NAME);
        }

        public ArrayList<Long> getNumber() {
            ArrayList<Long> numbers = new ArrayList<>();

            for (EMPLOYEE_INFO employee : data) {
                numbers.add(employee.EMPLOYEE_NUMBER);
            }

            return numbers;
        }

        public void check(EMPLOYEE_INFO employee) {
            data.add(0, employee);

            notifyItemInserted(data.size() - 1);
            for (int index = data.size() - 2; index >= 0; index--) {
                notifyItemMoved(index, index + 1);
            }

            Objects.requireNonNull(listener).scroll();
        }

        public void uncheck(long employee_number) {
            for (int index = 0; index < data.size(); index++) {
                if (data.get(index).EMPLOYEE_NUMBER == employee_number) {
                    data.remove(index);

                    notifyItemRemoved(index);
                    break;
                }
            }
        }

        public static class ViewHolder extends CommonViewHolder {
            public ImageView image;
            public ImageButton remove;
            public TextView name;

            public ViewHolder(@NonNull View view) {
                super(view);

                image.setClipToOutline(true);
            }

            @Override
            protected void setId() {
                image = view.findViewById(R.id.adapter_employee_group_create_selected__image);
                remove = view.findViewById(R.id.adapter_employee_group_create_selected__remove);
                name = view.findViewById(R.id.adapter_employee_group_create_selected__name);
            }
        }

        public interface Listener {
            void remove(long employee_number);
            void scroll();
        }
    }

    public static class UnselectedAdapter extends CommonListAdapter<EMPLOYEE_INFO, UnselectedAdapter.ViewHolder> {
        private final Set<Long> checked = new HashSet<>();

        public Listener listener;

        public UnselectedAdapter(@NonNull Context context) {
            super(context, new DBThread("GetEmployee", new HashMap<String, Object>() {{
                put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                put("index", 0);
            }}));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_create_unselected, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EMPLOYEE_INFO employee = data.get(position);

            Glide.with(holder.image.getContext()).load(Server.IMAGE_URL + employee.EMPLOYEE_IMAGE).into(holder.image);
            holder.checkbox.setChecked(checked.contains(employee.EMPLOYEE_NUMBER));
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CheckBox) view).isChecked()) {
                        checked.add(employee.EMPLOYEE_NUMBER);
                        Objects.requireNonNull(listener).check(employee);
                    } else {
                        checked.remove(employee.EMPLOYEE_NUMBER);
                        Objects.requireNonNull(listener).uncheck(employee.EMPLOYEE_NUMBER);
                    }
                }
            });
            holder.name.setText(employee.EMPLOYEE_NAME);
            holder.company.setText(employee.EMPLOYEE_COMPANY_NAME);
            holder.contact_number.setText(ContactNumber.parse(employee.EMPLOYEE_CONTACT_NUMBER));
        }

        @Override
        public void reload() {
            new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
                put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                put("index", 0);
            }}, new ReloadListener());
        }

        @Override
        public void load() {
            new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
                put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                put("index", data.size());
            }}, new LoadListener());
        }

        public void reload(CharSequence search) {
            new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
                put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                put("index", 0);
                put("search", search);
            }}, new ReloadListener());
        }

        public void load(CharSequence search) {
            new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
                put("employee_number", Session.USER.EMPLOYEE_NUMBER);
                put("index", 0);
                put("search", search);
            }}, new LoadListener());
        }

        public void remove(long employee_number) {
            checked.remove(employee_number);

            for (int index = 0; index < data.size(); index++) {
                if (data.get(index).EMPLOYEE_NUMBER == employee_number) {
                    notifyItemChanged(index);
                    break;
                }
            }
        }

        public static class ViewHolder extends CommonViewHolder {
            public CheckBox checkbox;
            public ImageView image;
            public TextView name;
            public TextView company;
            public TextView contact_number;

            public ViewHolder(@NonNull View view) {
                super(view);

                image.setClipToOutline(true);
            }

            @Override
            protected void setId() {
                checkbox = view.findViewById(R.id.adapter_employee_group_create_unselected__checkbox);
                image = view.findViewById(R.id.adapter_employee_group_create_unselected__image);
                name = view.findViewById(R.id.adapter_employee_group_create_unselected__name);
                company = view.findViewById(R.id.adapter_employee_group_create_unselected__company);
                contact_number = view.findViewById(R.id.adapter_employee_group_create_unselected__contact_number);
            }
        }

        public interface Listener {
            void check(EMPLOYEE_INFO employee);
            void uncheck(long employee_number);
        }
    }
}

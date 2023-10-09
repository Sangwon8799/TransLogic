package com.yeungjin.translogic.adapter.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonListAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.EMPLOYEE;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.employee.GetEmployeeRequest;
import com.yeungjin.translogic.request.employee.GetEmployeeThread;
import com.yeungjin.translogic.request.employee.GetSearchedEmployeeRequest;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.utility.Json;
import com.yeungjin.translogic.utility.Server;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class GroupCreateUnselectedAdapter extends CommonListAdapter<EMPLOYEE, GroupCreateUnselectedAdapter.ViewHolder> {
    private final Set<Long> checked = new HashSet<>();

    public Listener listener;

    public GroupCreateUnselectedAdapter(@NonNull Context context) {
        super(context, new GetEmployeeThread());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_create_unselected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EMPLOYEE employee = DATA.get(position);

        Glide.with(holder.image.getContext()).load(Server.IMAGE_URL + employee.EMPLOYEE_IMAGE).into(holder.image);
        holder.checkbox.setChecked(checked.contains(employee.EMPLOYEE_NUMBER));
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    checked.add(employee.EMPLOYEE_NUMBER);

                    if (listener != null) {
                        listener.check(employee);
                    }
                } else {
                    checked.remove(employee.EMPLOYEE_NUMBER);

                    if (listener != null) {
                        listener.uncheck(employee.EMPLOYEE_NUMBER);
                    }
                }
            }
        });
        holder.name.setText(employee.EMPLOYEE_NAME);
        holder.contact_number.setText(ContactNumber.parse(employee.EMPLOYEE_CONTACT_NUMBER));
    }

    @Override
    protected int getResponse(@NonNull String response) throws Exception {
        array = new JSONObject(response).getJSONArray("employee");
        for (int index = 0; index < array.length(); index++) {
            object = array.getJSONObject(index);
            DATA.add(Json.from(object, EMPLOYEE.class));
        }

        return array.length();
    }

    @Override
    public void reload() {
        Request request = new GetEmployeeRequest(0, new ReloadListener());
        Request.sendRequest(CONTEXT, request);
    }

    @Override
    public void load() {
        Request request = new GetEmployeeRequest(DATA.size(), new LoadListener());
        Request.sendRequest(CONTEXT, request);
    }

    public void reload(CharSequence search) {
        Request request = new GetSearchedEmployeeRequest(0, search.toString(), new ReloadListener());
        Request.sendRequest(CONTEXT, request);
    }

    public void load(CharSequence search) {
        Request request = new GetSearchedEmployeeRequest(DATA.size(), search.toString(), new LoadListener());
        Request.sendRequest(CONTEXT, request);
    }

    public void remove(long employee_number) {
        checked.remove(employee_number);

        for (int index = 0; index < DATA.size(); index++) {
            if (DATA.get(index).EMPLOYEE_NUMBER == employee_number) {
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
            checkbox = VIEW.findViewById(R.id.adapter_employee_group_create_unselected__checkbox);
            image = VIEW.findViewById(R.id.adapter_employee_group_create_unselected__image);
            name = VIEW.findViewById(R.id.adapter_employee_group_create_unselected__name);
            company = VIEW.findViewById(R.id.adapter_employee_group_create_unselected__company);
            contact_number = VIEW.findViewById(R.id.adapter_employee_group_create_unselected__contact_number);
        }
    }

    public interface Listener {
        void check(EMPLOYEE employee);
        void uncheck(long number);
    }
}

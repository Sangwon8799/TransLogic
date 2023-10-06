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
import com.yeungjin.translogic.object.database.EMPLOYEE;
import com.yeungjin.translogic.request.Request;
import com.yeungjin.translogic.request.Server;
import com.yeungjin.translogic.request.employee.GetEmployeeRequest;
import com.yeungjin.translogic.request.employee.GetSearchedEmployeeRequest;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.utility.DateFormat;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class GroupCreateUnselectedAdapter extends CommonListAdapter<EMPLOYEE, GroupCreateUnselectedAdapter.ViewHolder> {
    private final Set<Long> checked = new HashSet<>();
    private OnCheckListener listener;

    public GroupCreateUnselectedAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_create_unselected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EMPLOYEE employee = data.get(position);

        Glide.with(holder.image.getContext()).load(Server.ImageURL + employee.EMPLOYEE_IMAGE).into(holder.image);
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
        holder.contactNumber.setText(ContactNumber.parse(employee.EMPLOYEE_CONTACT_NUMBER));
    }

    @Override
    protected int getResponse(String response) throws Exception {
        JSONObject http = new JSONObject(response);

        array = http.getJSONArray("employee");
        for (int step = 0; step < array.length(); step++) {
            object = array.getJSONObject(step);

            EMPLOYEE employee = new EMPLOYEE();
            employee.EMPLOYEE_NUMBER = object.getLong("EMPLOYEE_NUMBER");
            employee.EMPLOYEE_NAME = object.getString("EMPLOYEE_NAME");
            employee.EMPLOYEE_USERNAME = object.getString("EMPLOYEE_USERNAME");
            employee.EMPLOYEE_PASSWORD = object.getString("EMPLOYEE_PASSWORD");
            employee.EMPLOYEE_CONTACT_NUMBER = object.getString("EMPLOYEE_CONTACT_NUMBER");
            employee.EMPLOYEE_EMAIL = object.getString("EMPLOYEE_EMAIL");
            employee.EMPLOYEE_COMPANY_NUMBER = object.getLong("EMPLOYEE_COMPANY_NUMBER");
            employee.EMPLOYEE_DEGREE = object.getString("EMPLOYEE_DEGREE");
            employee.EMPLOYEE_IMAGE = object.getString("EMPLOYEE_IMAGE");
            employee.EMPLOYEE_REGISTER_DATE = DateFormat.DATE.parse(object.getString("EMPLOYEE_REGISTER_DATE"));

            data.add(employee);
        }

        return array.length();
    }

    @Override
    public void reload() {
        Request request = new GetEmployeeRequest(0, new ReloadListener());
        Request.sendRequest(context, request);
    }

    @Override
    public void load() {
        Request request = new GetEmployeeRequest(data.size(), new LoadListener());
        Request.sendRequest(context, request);
    }

    public void reload(CharSequence search) {
        Request request = new GetSearchedEmployeeRequest(0, search.toString(), new ReloadListener());
        Request.sendRequest(context, request);
    }

    public void load(CharSequence search) {
        Request request = new GetSearchedEmployeeRequest(data.size(), search.toString(), new LoadListener());
        Request.sendRequest(context, request);
    }

    public void remove(long number) {
        checked.remove(number);

        for (int index = 0; index < data.size(); index++) {
            if (data.get(index).EMPLOYEE_NUMBER == number) {
                notifyItemChanged(index);
                break;
            }
        }
    }

    public void setOnCheckListener(OnCheckListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends CommonViewHolder {
        public CheckBox checkbox;
        public ImageView image;
        public TextView name;
        public TextView company;
        public TextView contactNumber;

        public ViewHolder(View view) {
            super(view);
            init();

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            checkbox = view.findViewById(R.id.adapter_employee_group_create_unselected__checkbox);
            image = view.findViewById(R.id.adapter_employee_group_create_unselected__image);
            name = view.findViewById(R.id.adapter_employee_group_create_unselected__name);
            company = view.findViewById(R.id.adapter_employee_group_create_unselected__company);
            contactNumber = view.findViewById(R.id.adapter_employee_group_create_unselected__contact_number);
        }
    }

    public interface OnCheckListener {
        void check(EMPLOYEE employee);
        void uncheck(long number);
    }
}

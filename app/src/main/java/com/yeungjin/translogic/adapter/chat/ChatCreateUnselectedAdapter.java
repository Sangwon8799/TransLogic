package com.yeungjin.translogic.adapter.chat;

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
import com.yeungjin.translogic.server.DBVolley;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.utility.ContactNumber;
import com.yeungjin.translogic.server.Server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChatCreateUnselectedAdapter extends CommonListAdapter<EMPLOYEE, ChatCreateUnselectedAdapter.ViewHolder> {
    private final Set<Long> checked = new HashSet<>();

    public Listener listener;

    public ChatCreateUnselectedAdapter(@NonNull Context context) {
        super(context, new DBThread("GetEmployee", new HashMap<String, Object>() {{
            put("index", 0);
        }}));
    }

    @NonNull
    @Override
    public ChatCreateUnselectedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_chat_create_unselected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EMPLOYEE employee = data.get(position);

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
    public void reload() {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("index", 0);
        }}, new ReloadListener());
    }

    @Override
    public void load() {
        new DBVolley(context, "GetEmployee", new HashMap<String, Object>() {{
            put("index", 0);
        }}, new LoadListener());
    }

    public void reload(CharSequence search) {
        new DBVolley(context, "GetSearchedEmployee", new HashMap<String, Object>() {{
            put("index", 0);
            put("search", search);
        }}, new ReloadListener());
    }

    public void load(CharSequence search) {
        new DBVolley(context, "GetSearchedEmployee",new HashMap<String, Object>() {{
            put("index", 0);
            put("search", search);
        }}, new ReloadListener());
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
            checkbox = view.findViewById(R.id.adapter_chat_chat_create_unselected__checkbox);
            image = view.findViewById(R.id.adapter_chat_chat_create_unselected__image);
            name = view.findViewById(R.id.adapter_chat_chat_create_unselected__name);
            company = view.findViewById(R.id.adapter_chat_chat_create_unselected__company);
            contact_number = view.findViewById(R.id.adapter_chat_chat_create_unselected__contact_number);
        }
    }

    public interface Listener {
        void check(EMPLOYEE employee);
        void uncheck(long employee_number);
    }
}

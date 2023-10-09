package com.yeungjin.translogic.adapter.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yeungjin.translogic.R;
import com.yeungjin.translogic.adapter.CommonAdapter;
import com.yeungjin.translogic.adapter.CommonViewHolder;
import com.yeungjin.translogic.object.EMPLOYEE;
import com.yeungjin.translogic.utility.Server;

import java.util.ArrayList;

public class GroupCreateSelectedAdapter extends CommonAdapter<EMPLOYEE, GroupCreateSelectedAdapter.ViewHolder> {
    private OnRemoveListener removeListener;
    private OnScrollListener scrollListener;

    public GroupCreateSelectedAdapter(Context context) {
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
        EMPLOYEE employee = data.get(position);

        Glide.with(holder.image.getContext()).load(Server.ImageURL + employee.EMPLOYEE_IMAGE).into(holder.image);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeListener != null) {
                    data.remove(employee);
                    removeListener.remove(employee.EMPLOYEE_NUMBER);

                    notifyItemRemoved(holder.getAdapterPosition());
                }
            }
        });
        holder.name.setText(employee.EMPLOYEE_NAME);
    }

    public ArrayList<Long> getNumbers() {
        ArrayList<Long> numbers = new ArrayList<>();

        for (EMPLOYEE employee : data) {
            numbers.add(employee.EMPLOYEE_NUMBER);
        }

        return numbers;
    }

    public void check(EMPLOYEE employee) {
        data.add(0, employee);

        notifyItemInserted(data.size() - 1);
        for (int index = data.size() - 2; index >= 0; index--) {
            notifyItemMoved(index, index + 1);
        }

        if (scrollListener != null) {
            scrollListener.scroll();
        }
    }

    public void uncheck(long number) {
        for (int index = 0; index < data.size(); index++) {
            if (data.get(index).EMPLOYEE_NUMBER == number) {
                data.remove(index);

                notifyItemRemoved(index);
                break;
            }
        }
    }

    public void setOnRemoveListener(OnRemoveListener listener) {
        this.removeListener = listener;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.scrollListener = listener;
    }

    public static class ViewHolder extends CommonViewHolder {
        public ImageView image;
        public ImageButton remove;
        public TextView name;

        public ViewHolder(View view) {
            super(view);
            init();

            image.setClipToOutline(true);
        }

        @Override
        protected void setId() {
            image = view.findViewById(R.id.adapter_employee_group_create_selected__image);
            remove = view.findViewById(R.id.adapter_employee_group_create_selected__remove);
            name = view.findViewById(R.id.adapter_employee_group_create_selected__name);
        }
    }

    public interface OnRemoveListener {
        void remove(long number);
    }

    public interface OnScrollListener {
        void scroll();
    }
}

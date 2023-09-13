package com.yeungjin.translogic.adapter.employee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class GroupSelected extends RecyclerView.Adapter<GroupSelected.ViewHolder> {
   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_selected, parent, false);
      return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

   }

   @Override
   public int getItemCount() {
      return 0;
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      public ImageView image;
      public ImageButton remove;
      public TextView name;

      public ViewHolder(View view) {
         super(view);

         image = (ImageView) view.findViewById(R.id.adapter_employee_group_selected__image);
         remove = (ImageButton) view.findViewById(R.id.adapter_employee_group_selected__remove);
         name = (TextView) view.findViewById(R.id.adapter_employee_group_selected__name);
      }
   }
}

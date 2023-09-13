package com.yeungjin.translogic.adapter.employee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class Group extends RecyclerView.Adapter<Group.ViewHolder> {
   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group, parent, false);
      return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {

   }

   @Override
   public int getItemCount() {
      return 0;
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public int getItemViewType(int position) {
      return position;
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {
      public RadioButton group;

      public ViewHolder(View view) {
         super(view);

         group = (RadioButton) view.findViewById(R.id.adapter_employee_group__name);
      }
   }
}
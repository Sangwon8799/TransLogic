package com.yeungjin.translogic.adapter.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class Filter extends RecyclerView.Adapter<Filter.ViewHolder> {
   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_task_filter, parent, false);
      return new ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

   }

   @Override
   public int getItemCount() {
      return 0;
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      public RadioButton name;

      public ViewHolder(View view) {
         super(view);

         name = (RadioButton) view.findViewById(R.id.adapter_task_filter__name);
      }
   }
}

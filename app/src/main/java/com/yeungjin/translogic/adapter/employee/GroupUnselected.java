package com.yeungjin.translogic.adapter.employee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class GroupUnselected extends RecyclerView.Adapter<GroupUnselected.ViewHolder> implements Filterable {
   @Override
   public Filter getFilter() {
      return new Filter() {
         @Override
         protected FilterResults performFiltering(CharSequence constraint) {
            return null;
         }

         @Override
         protected void publishResults(CharSequence constraint, FilterResults results) {

         }
      };
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_employee_group_unselected, parent, false);
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
      public CheckBox checkbox;
      public ImageView image;
      public TextView name;
      public TextView company;
      public TextView contact_number;

      public ViewHolder(View view) {
         super(view);

         checkbox = (CheckBox) view.findViewById(R.id.adapter_employee_group_unselected__checkbox);
         image = (ImageView) view.findViewById(R.id.adapter_employee_group_unselected__image);
         name = (TextView) view.findViewById(R.id.adapter_employee_group_unselected__name);
         company = (TextView) view.findViewById(R.id.adapter_employee_group_unselected__company);
         contact_number = (TextView) view.findViewById(R.id.adapter_employee_group_unselected__contact_number);
      }
   }
}

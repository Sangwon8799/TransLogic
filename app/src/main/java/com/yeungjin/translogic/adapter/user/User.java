package com.yeungjin.translogic.adapter.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeungjin.translogic.R;

public class User extends RecyclerView.Adapter<User.ViewHolder> implements Filterable {
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
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_user, parent, false);
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
      public ImageView image;
      public TextView name;
      public TextView company;
      public TextView contactNumber;

      public ViewHolder(View view) {
         super(view);

         image = (ImageView) view.findViewById(R.id.adapter_user_user__image);
         name = (TextView) view.findViewById(R.id.adapter_user_user__name);
         company = (TextView) view.findViewById(R.id.adapter_user_user__company);
         contactNumber = (TextView) view.findViewById(R.id.adapter_user_user__contact_number);
      }
   }
}

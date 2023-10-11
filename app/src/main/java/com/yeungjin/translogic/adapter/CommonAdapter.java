package com.yeungjin.translogic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class CommonAdapter<OBJECT, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    protected final Context context;
    protected final ArrayList<OBJECT> data = new ArrayList<>();

    public CommonAdapter(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

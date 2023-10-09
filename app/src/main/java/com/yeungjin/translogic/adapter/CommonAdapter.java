package com.yeungjin.translogic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class CommonAdapter<OBJECT, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    protected final Context CONTEXT;
    protected final ArrayList<OBJECT> DATA = new ArrayList<>();

    public CommonAdapter(@NonNull Context CONTEXT) {
        this.CONTEXT = CONTEXT;
    }

    @Override
    public int getItemCount() {
        return DATA.size();
    }
}

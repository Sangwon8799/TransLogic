package com.yeungjin.translogic.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class CommonViewHolder extends RecyclerView.ViewHolder {
    protected final View view;

    public CommonViewHolder(@NonNull View view) {
        super(view);
        this.view = view;

        setId();
        setAdapter();
        setListener();
    }

    protected abstract void setId();

    protected void setAdapter() { }

    protected void setListener() { }
}

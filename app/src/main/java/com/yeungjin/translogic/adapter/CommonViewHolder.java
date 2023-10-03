package com.yeungjin.translogic.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class CommonViewHolder extends RecyclerView.ViewHolder {
    protected View view;

    public CommonViewHolder(View view) {
        super(view);
        this.view = view;
    }

    protected void init() {
        setId();
        setAdapter();
        setListener();
    }

    protected abstract void setId();

    protected void setAdapter() { }

    protected void setListener() { }
}

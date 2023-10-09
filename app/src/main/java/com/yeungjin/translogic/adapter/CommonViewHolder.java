package com.yeungjin.translogic.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class CommonViewHolder extends RecyclerView.ViewHolder {
    protected final View VIEW;

    public CommonViewHolder(@NonNull View VIEW) {
        super(VIEW);
        this.VIEW = VIEW;

        setId();
        setAdapter();
        setListener();
    }

    protected abstract void setId();

    protected void setAdapter() { }

    protected void setListener() { }
}

package com.yeungjin.translogic.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class CommonListAdapter<OBJECT, ViewHolder extends RecyclerView.ViewHolder> extends CommonAdapter<OBJECT, ViewHolder> {
    protected JSONObject object;
    protected JSONArray array;

    public CommonListAdapter(Context context) {
        super(context);
        reload();
    }

    protected abstract int getResponse(String response) throws Exception;

    public abstract void reload();

    public abstract void load();

    public class ReloadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            notifyItemRangeRemoved(0, data.size());
            data.clear();

            try {
                notifyItemRangeInserted(0, getResponse(response));
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    public class LoadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            try {
                notifyItemRangeInserted(data.size(), getResponse(response));
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }
}

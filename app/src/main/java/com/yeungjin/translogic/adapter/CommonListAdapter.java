package com.yeungjin.translogic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.yeungjin.translogic.request.ThreadRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class CommonListAdapter<OBJECT, ViewHolder extends RecyclerView.ViewHolder> extends CommonAdapter<OBJECT, ViewHolder> {
    protected JSONObject object;
    protected JSONArray array;

    public CommonListAdapter(@NonNull Context context, @NonNull ThreadRequest request) {
        super(context);

        request.start();
        try {
            request.join();
            notifyItemRangeInserted(0, getResponse(request.getResponse()));
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    protected abstract int getResponse(@NonNull String response) throws Exception;

    public abstract void reload();

    public abstract void load();

    public class ReloadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            try {
                int old_size = DATA.size();
                DATA.clear();
                int new_size = getResponse(response);

                if (old_size > new_size) {
                    notifyItemRangeChanged(0, new_size);
                    notifyItemRangeRemoved(new_size, old_size - new_size);
                } else if (old_size < new_size) {
                    notifyItemRangeChanged(0, old_size);
                    notifyItemRangeInserted(old_size, new_size - old_size);
                } else {
                    notifyItemRangeChanged(0, new_size);
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    public class LoadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            try {
                notifyItemRangeInserted(DATA.size(), getResponse(response));
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }
}

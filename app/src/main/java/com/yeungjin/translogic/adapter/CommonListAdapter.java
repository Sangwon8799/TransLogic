package com.yeungjin.translogic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.yeungjin.translogic.server.DBThread;
import com.yeungjin.translogic.utility.Json;

import org.json.JSONArray;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public abstract class CommonListAdapter<OBJECT, ViewHolder extends RecyclerView.ViewHolder> extends CommonAdapter<OBJECT, ViewHolder> {
    private final Class<OBJECT> type;

    @SuppressWarnings("unchecked")
    public CommonListAdapter(@NonNull Context context, @NonNull DBThread request) {
        super(context);
        this.type = (Class<OBJECT>) ((ParameterizedType) Objects.requireNonNull(getClass().getGenericSuperclass())).getActualTypeArguments()[0];

        notifyItemRangeInserted(0, addData(request.getResponse()));
    }

    private int addData(String response) {
        JSONArray json = null;
        try {
            json = new JSONArray(response);
            for (int index = 0; index < json.length(); index++) {
                data.add(Json.from(json.getJSONObject(index), type));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return Objects.requireNonNull(json).length();
    }

    public abstract void reload();

    public abstract void load();

    public class ReloadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            int old_size = data.size();
            data.clear();
            int new_size = addData(response);

            if (old_size > new_size) {
                notifyItemRangeChanged(0, new_size);
                notifyItemRangeRemoved(new_size, old_size - new_size);
            } else if (old_size < new_size) {
                notifyItemRangeChanged(0, old_size);
                notifyItemRangeInserted(old_size, new_size - old_size);
            } else {
                notifyItemRangeChanged(0, new_size);
            }
        }
    }

    public class LoadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            notifyItemRangeInserted(data.size(), addData(response));
        }
    }
}

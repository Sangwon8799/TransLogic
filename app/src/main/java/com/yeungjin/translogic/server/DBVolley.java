package com.yeungjin.translogic.server;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DBVolley extends StringRequest {
    private final Map<String, String> parameters = new HashMap<>();

    public DBVolley(Context context, String URI, @NonNull Map<String, Object> parameters, Response.Listener<String> listener) {
        super(Method.POST, Server.URL + URI + ".db", listener != null ? listener : (String) -> { }, Throwable::printStackTrace);

        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            this.parameters.put(parameter.getKey(), String.valueOf(parameter.getValue()));
        }

        Volley.newRequestQueue(context).add(this);
    }
    public DBVolley(Context context, String URI, @NonNull Map<String, Object> parameters) {
        this(context, URI, parameters, null);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

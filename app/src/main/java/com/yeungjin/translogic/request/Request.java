package com.yeungjin.translogic.request;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yeungjin.translogic.utility.Server;

import java.util.HashMap;
import java.util.Map;

public class Request extends StringRequest {
    public static RequestQueue queue;
    protected Map<String, String> parameters = new HashMap<>();

    public Request(String URI, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, Server.URL + URI + ".db", listener, errorListener);
    }
    public Request(String URI, Response.Listener<String> listener) {
        this(URI, listener, Throwable::printStackTrace);
    }
    public Request(String URI) {
        this(URI, (String response) -> { });
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

    public static void sendRequest(Context context, Request request) {
        queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}

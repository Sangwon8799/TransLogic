package com.yeungjin.translogic.request;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Request extends StringRequest {
    private static final String URL = "http://152.70.237.174/";

    protected Map<String, String> parameters = new HashMap<>();
    public static RequestQueue queue;

    public Request(String URI, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL + URI, listener, errorListener);
    }

    public Request(String URI, Response.Listener<String> listener) {
        this(URI, listener, null);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

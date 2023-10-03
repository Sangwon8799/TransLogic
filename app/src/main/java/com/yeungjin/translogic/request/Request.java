package com.yeungjin.translogic.request;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

// Request 클래스를 상속시켜 간편하게 사용함을 목적으로 만들어진 클래스
public class Request extends StringRequest {
    private static final String URL = "http://152.70.237.174/"; // 서버 위치
    public static RequestQueue queue;                           // 전송 요청들을 담아두는 큐 컨테이너 필드

    protected Map<String, String> parameters = new HashMap<>(); // 파라미터를 담는 필드

    // ErrorListener를 사용할 경우 사용되는 생성자
    public Request(String URI, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL + URI, listener, errorListener);
    }

    // 주로 사용되는 생성자
    public Request(String URI, Response.Listener<String> listener) {
        this(URI, listener, null);
    }

    // 요청 전송
    public static void sendRequest(Context context, Request request) {
        queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

package com.yeungjin.translogic.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class Json {
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private Json() { }

    public static String to(Object object) {
        return GSON.toJson(object);
    }

    public static <OBJECT> OBJECT from(String json, Class<OBJECT> object) {
        return GSON.fromJson(json, object);
    }
    public static <OBJECT> OBJECT from(Object json, Class<OBJECT> object) {
        return GSON.fromJson(String.valueOf(json), object);
    }
    public static <OBJECT> OBJECT from(JSONObject json, Class<OBJECT> object) {
        return GSON.fromJson(String.valueOf(json), object);
    }
}

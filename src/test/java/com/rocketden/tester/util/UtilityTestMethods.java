package com.rocketden.tester.util;

import com.google.gson.Gson;

public class UtilityTestMethods {

    public static String convertObjectToJsonString(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public static <T> T toObject(String json, Class<T> c) {
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }
}

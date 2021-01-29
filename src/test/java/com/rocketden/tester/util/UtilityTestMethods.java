package com.rocketden.tester.util;

import com.google.gson.Gson;

public class UtilityTestMethods {

    private static final Gson gson = new Gson();

    public static String convertObjectToJsonString(Object o) {
        return gson.toJson(o);
    }

    public static <T> T toObject(String json, Class<T> c) {
        return gson.fromJson(json, c);
    }
}

package com.e1858.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	private static Gson	gson	= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	public static String toJson(Object src) {
		if (src == null) {
			return "";
		}
		return gson.toJson(src);
	}

	public static <T> T fromJson(String json, Class<T> classOfT) {
		try {
			return gson.fromJson(json, classOfT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T fromJson(String json, Type typeOfT) {
		try {
			return gson.fromJson(json, typeOfT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

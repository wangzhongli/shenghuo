package com.e1858.mall.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.MallConstant;
import com.google.gson.Gson;

public class MallDataPersister {
	static SharedPreferences	sharedPreferences	= null;
	static Gson					gson				= new Gson();

	public static SharedPreferences getSharedPreferences() {
		if (sharedPreferences == null) {
			synchronized (MallDataPersister.class) {
				if (sharedPreferences == null) {
					sharedPreferences = BaseApplication.getBaseInstance().getSharedPreferences(MallConstant.PrefName,
							Context.MODE_PRIVATE);
				}
			}
		}
		return sharedPreferences;
	}
}

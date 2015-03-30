package com.e1858.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.e1858.common.app.BaseApplication;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.UserHouse;
import com.e1858.wuye.protocol.http.UserInfo;
import com.google.gson.Gson;

/**
 * 日期: 2015年2月5日 上午10:54:09
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class PreferencesUtils {
	public static final class Key {
		public static final String	Community	= "wuye.common.community";
		public static final String	UserInfo	= "wuye.common.userinfo";
		public static final String	UserHouse	= "wuye.common.userhouse";
		public static final String	UserID		= "wuye.common.userid";
		public static final String	LoginKey	= "wuye.common.LoginKey";
		public static final String	UserName	= "wuye.common.UserName";
		public static final String	Password	= "wuye.common.Password";
	}

	private static Community	community;
	private static UserInfo		userInfo;
	private static int			userID		= -1;
	private static UserHouse	userHouse;
	private static String		loginKey	= "";
	private static String		userName	= "";
	private static String		password	= "";

	public static Community getCommunity() {
		if (community == null) {
			community = getJsonObject(Key.Community, Community.class);
		}
		return community;
	}

	public static void setCommunity(Community community) {
		PreferencesUtils.community = community;
		putJsonObject(Key.Community, community);
	}

	public static UserInfo getUserInfo() {
		if (userInfo == null) {
			userInfo = getJsonObject(Key.UserInfo, UserInfo.class);
		}
		return userInfo;
	}

	public static void setUserInfo(UserInfo userInfo) {
		PreferencesUtils.userInfo = userInfo;
		putJsonObject(Key.UserInfo, userInfo);
	}

	public static int getUserID() {
		if (userID == -1) {
			userID = getInt(Key.UserID, userID);
		}
		return userID;
	}

	public static void setUserID(int userID) {
		PreferencesUtils.userID = userID;
		putInt(Key.UserID, userID);
	}

	public static UserHouse getUserHouse() {
		if (userHouse == null) {
			userHouse = getJsonObject(Key.UserHouse, UserHouse.class);
		}
		return userHouse;
	}

	public static void setUserHouse(UserHouse userHouse) {
		PreferencesUtils.userHouse = userHouse;
		putJsonObject(Key.UserHouse, userHouse);
	}

	public static String getLoginKey() {
		if (TextUtils.isEmpty(loginKey)) {
			loginKey = getString(Key.LoginKey, loginKey);
		}
		return loginKey;
	}

	public static void setLoginKey(String loginKey) {
		PreferencesUtils.loginKey = loginKey;
		putString(Key.LoginKey, loginKey);
	}

	public static String getUserName() {
		if (TextUtils.isEmpty(userName)) {
			userName = getString(Key.UserName, userName);
		}
		return userName;
	}

	public static void setUserName(String userName) {
		PreferencesUtils.userName = userName;
		putString(Key.UserName, userName);
	}

	public static String getPassword() {
		if (TextUtils.isEmpty(password)) {
			password = getString(Key.Password, password);
		}
		return password;
	}

	public static void setPassword(String password) {
		PreferencesUtils.password = password;
		putString(Key.Password, password);
	}

	////////////////////////////gson//////////////////////
	private static Gson	_gson;

	public static Gson gson() {
		if (_gson == null) {
			_gson = new Gson();
		}
		return _gson;
	}

	private static void putJsonObject(String key, Object obj) {
		putString(key, gson().toJson(obj));
	}

	private static <Type> Type getJsonObject(String key, Class<Type> klass) {
		String text = sharedPreferences().getString(key, "");
		if (text.length() > 0) {
			try {
				return gson().fromJson(text, klass);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

////////////////////////////gson//////////////////////

////////////////////////////SharedPreferences//////////////////////
	private static String				PREF_NAME	= "com_wuye_common_data";

	private static SharedPreferences	_sharedPreferences;

	public static SharedPreferences sharedPreferences() {
		if (_sharedPreferences == null) {
			_sharedPreferences = BaseApplication.getBaseInstance()
					.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
		return _sharedPreferences;
	}

////////////////////////////SharedPreferences//////////////////////

	/////////////////////////////////////////////////////////////
	public static boolean putString(String key, String value) {
		SharedPreferences.Editor editor = sharedPreferences().edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public static String getString(String key, String defaultValue) {
		return sharedPreferences().getString(key, defaultValue);
	}

	public static boolean putInt(String key, int value) {
		SharedPreferences.Editor editor = sharedPreferences().edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static int getInt(String key, int defaultValue) {
		return sharedPreferences().getInt(key, defaultValue);
	}

	public static boolean putLong(String key, long value) {
		SharedPreferences.Editor editor = sharedPreferences().edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static long getLong(String key, long defaultValue) {
		return sharedPreferences().getLong(key, defaultValue);
	}

	public static boolean putFloat(String key, float value) {
		SharedPreferences.Editor editor = sharedPreferences().edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public static float getFloat(String key, float defaultValue) {
		return sharedPreferences().getFloat(key, defaultValue);
	}

	public static boolean putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = sharedPreferences().edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return sharedPreferences().getBoolean(key, defaultValue);
	}
	/////////////////////////////////////////////////////////////
}

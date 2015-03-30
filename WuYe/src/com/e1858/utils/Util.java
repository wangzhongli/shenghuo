package com.e1858.utils;

import java.util.Calendar;
import java.util.Locale;

import com.e1858.wuye.protocol.http.UserHouse;

public class Util {
	private static final String	TAG	= "Util";

	public static String byte2HexString(byte abyte0[]) {
		StringBuffer stringbuffer = new StringBuffer();
		int i = abyte0.length;
		int j = 0;
		do {
			if (j >= i)
				return stringbuffer.toString();
			stringbuffer.append(Integer.toHexString(0x100 | 0xff & abyte0[j]).substring(1));
			j++;
		} while (true);
	}

	public static long getUTCTime() {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTimeInMillis();
	}

	public static String getHouseInfo(boolean isAddCommunity) {
		StringBuilder result = new StringBuilder();
		UserHouse userHouse = PreferencesUtils.getUserHouse();
		if (isAddCommunity) {
			result.append(PreferencesUtils.getCommunity().getName());
		}
		if (userHouse != null) {
			if (userHouse.getHouse() != null && !StringUtils.isEmpty(userHouse.getHouse().getName())) {
				result.append(userHouse.getHouse().getName());
			}
			if (userHouse.getHouseUnit() != null && !StringUtils.isEmpty(userHouse.getHouseUnit().getName())) {
				result.append(userHouse.getHouseUnit().getName());
			}
			if (userHouse.getHouseFloor() != null && !StringUtils.isEmpty(userHouse.getHouseFloor().getName())) {
				result.append(userHouse.getHouseFloor().getName());
			}
			if (userHouse.getHouseRoom() != null && !StringUtils.isEmpty(userHouse.getHouseRoom().getName())) {
				result.append(userHouse.getHouseRoom().getName());
			}
		}
		return result.toString();
	}

//	public static void saveStatus(MainApplication application, SharedPreferences sp) {
//		String object_key = "login_resp";
//		LoginResp login_resp = (LoginResp) application.readObject(object_key);
//		Community community = (Community) application.readObject("community");
//		application.setCommunity(community);
//		if (null != login_resp) {
////			application.setKey(login_resp.getKey());
////			application.setToken(Encrypt.MD5(login_resp.getKey()+Constant.TokenSalt));
//			application.setUserID(login_resp.getUserID());
//			application.setUserName(sp.getString(Constant.USERNAME, ""));
//			application.setPassword(sp.getString(Constant.PASSWORD, ""));
//			UserInfo userInfo = (UserInfo) application.readObject(application.getUserID() + "_#");
//			application.setUserInfo(userInfo);
//			UserHouse userHouse = (UserHouse) application.readObject(application.getUserID() + "_@");
//			application.setUserHouse(userHouse);
//		}
//
//	}
}

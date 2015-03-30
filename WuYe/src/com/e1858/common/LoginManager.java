package com.e1858.common;

import android.os.Handler;

import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.PreferencesUtils;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.Login;
import com.e1858.wuye.protocol.http.SetGetuiClientId;

public class LoginManager {

	public static void login(MainApplication application,Handler handler,String username,String password){
		Login login = new Login();
		login.setCommunityID(PreferencesUtils.getCommunity().getID());
		login.setUsername(username);
		login.setPassword(Encrypt.MD5(password));
		login.setToken(Encrypt.MD5(username+ Encrypt.MD5(password)+ Constant.TokenSalt));
		NetUtil.post(Constant.BASE_URL, login, handler, HttpDefine.LOGIN_RESP);
	}
	public static void setClientID(Handler handler,int communityID,String clientID,int userID,String key,String token){
		SetGetuiClientId setGetuiClientId=new SetGetuiClientId();
		setGetuiClientId.setClientID(clientID);
		setGetuiClientId.setDeviceToken("");
		setGetuiClientId.setDeviceType(1);
		setGetuiClientId.setCommunityID(communityID);
		if(userID>0){
			setGetuiClientId.setUserID(userID);
		}
		setGetuiClientId.setKey(key);
		setGetuiClientId.setToken(token);
		NetUtil.post(Constant.BASE_URL, setGetuiClientId, handler, HttpDefine.SET_GETUI_CLIENTID_RESP);
		
	}
}

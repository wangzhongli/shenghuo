package com.e1858.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.e1858.common.app.BaseApplication;
import com.e1858.wuye.protocol.http.PacketResp;
import com.google.gson.Gson;

public class ResponseUtils {

	static Gson	gson	= new Gson();

	public static Gson gson() {
		return gson;
	}

	public static boolean checkResponseAndToastError(PacketResp response, String error) {
		if (!BaseApplication.getBaseInstance().isNetworkAvailable()) {
			ToastUtil.show(BaseApplication.getBaseInstance(), "当前网络不可用，请检查你的网络设置。");
			return false;
		}
		if (response != null && response.isSuccess()) {
			return true;
		} else {
			if (response != null && !TextUtils.isEmpty(response.getError())) {
				Toast.makeText(BaseApplication.getBaseInstance(), response.getError(), Toast.LENGTH_SHORT).show();
			} else if (!TextUtils.isEmpty(error)) {
				Toast.makeText(BaseApplication.getBaseInstance(), "服务器连接失败，请稍后重试！", Toast.LENGTH_SHORT).show();
			} else {
				if (!"服务器返回数据格式错误".equals(error)) {//不让显示这个错误.....就这样判断了吧,本来就不是正经的做法
					Toast.makeText(BaseApplication.getBaseInstance(), "未知错误", Toast.LENGTH_SHORT).show();
				}
			}
			return false;
		}
	}

}

package com.cwtcn.loveroundlibs;

import android.content.Context;
import android.util.Log;

import com.cwtcm.kt.sdk.ILoveNetWork;
import com.cwtcm.kt.sdk.LoveSdk;
import com.cwtcn.kt.ui.CustomProgressDialog;

public class LoveAroundManager {
	public Context					mContext;
	public String					user;
	private CustomProgressDialog	dialog;

	public LoveAroundManager(Context mContext, String user) {
		this.mContext = mContext;
		this.user = user;
//		LoveSdk.setUrl(mContext, "124.116.176.165");
		LoveSdk.init(mContext);
	}

	public void init() {
		login(user);
	}

	public void login(final String user) {
		dialog = new CustomProgressDialog(mContext).createDialog(R.drawable.refresh_normal);
		dialog.show();
		Log.i("LoveAroundManager", "智能手环 开始登陆");
		LoveSdk.login(mContext, user, new ILoveNetWork() {

			@Override
			public void serverErr() {
				dialog.dismiss();
				Log.i("LoveAroundManager", "智能手环 服务器内部错误");
			}

			@Override
			public void netOverDute() {
				dialog.dismiss();
				Log.i("LoveAroundManager", "智能手环 登入超时");
			}

			@Override
			public void netErr() {
				dialog.dismiss();
				Log.i("LoveAroundManager", "智能手环 网络错误，请检查网络");
			}

			@Override
			public void getNetSucceed() {
				dialog.dismiss();
				Log.i("LoveAroundManager", "智能手环 登入成功");
				/*
				 * 登入到主界面
				 * 必须要登入加载数据完成后才能进行这一步
				 * @param context
				 */

				LoveSdk.startLove(mContext);
			}

			@Override
			public void requestErr(int errCode, String message) {
				if (errCode == 105) {
					//在此注册
					register(user);
				}
			}

		});
	}

	public void register(final String user) {
		LoveSdk.register(mContext, user, new ILoveNetWork() {

			@Override
			public void serverErr() {
				Log.i("LoveAroundManager", "服务器内部错误！");
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void requestErr(final int errCode, final String message) {
				if (dialog != null) {
					dialog.dismiss();
				}
				Log.i("LoveAroundManager", "智能手环注册错误码" + errCode + " " + message);
			}

			@Override
			public void netOverDute() {
				if (dialog != null) {
					dialog.dismiss();
				}
				Log.i("LoveAroundManager", "注册超时！");
			}

			@Override
			public void netErr() {
				if (dialog != null) {
					dialog.dismiss();
				}
				Log.i("LoveAroundManager", "网络错误，请检查网络！");
			}

			@Override
			public void getNetSucceed() {
				//注册成功
				if (dialog != null) {
					dialog.dismiss();
				}
				Log.i("LoveAroundManager", "注册成功！");
				login(user);
			}
		});
	}

	public static void logout(Context context) {
		LoveSdk.endSocket(context);
	}
}

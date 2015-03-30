package com.cwtcn.loveroundlibs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cwtcm.kt.sdk.ILoveNetWork;
import com.cwtcm.kt.sdk.LoveSdk;
import com.cwtcn.kt.ui.CustomProgressDialog;

public class LoveAroundActivity extends Activity {
	public Context				mContext;
	public static final String	USER_ID	= "user_id";

	public static void action(Context context, String user) {
		Intent intent = new Intent(context, LoveAroundActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra(USER_ID, user);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		LoveSdk.setUrl(this.getApplicationContext(), "124.116.176.165");
		LoveSdk.init(this);
		if (null != getIntent()) {
			String user = getIntent().getStringExtra(USER_ID);
			login(user);
		}
	}

	public void login(final String user) {
		final CustomProgressDialog dialog = new CustomProgressDialog(mContext).createDialog(R.drawable.refresh_normal);
		dialog.show();

		Log.i("tag", "智能手环 开始登陆");
		LoveSdk.login(mContext, user, new ILoveNetWork() {

			@Override
			public void serverErr() {
				dialog.dismiss();
				Log.i("tag", "智能手环 服务器内部错误");
				Toast.makeText(mContext, "服务器内部错误！", Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void netOverDute() {
				dialog.dismiss();
				Log.i("tag", "智能手环 登入超时");
				Toast.makeText(mContext, "登入超时！", Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void netErr() {
				dialog.dismiss();
				Log.i("tag", "智能手环 网络错误，请检查网络");
				Toast.makeText(mContext, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void getNetSucceed() {
				dialog.dismiss();
				Log.i("tag", "智能手环 登入成功");
				Toast.makeText(mContext, "登入成功！", Toast.LENGTH_SHORT).show();
				/*
				 * 登入到主界面
				 * 必须要登入加载数据完成后才能进行这一步
				 * @param context
				 */

				LoveSdk.startLove(mContext);
				finish();
			}

			@Override
			public void requestErr(int errCode, String message) {
				dialog.dismiss();
				Toast.makeText(mContext, "errCode=" + errCode + ";" + message, Toast.LENGTH_SHORT).show();
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
				finish();
			}

			@Override
			public void requestErr(final int errCode, final String message) {
				LoveAroundActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mContext, "errCode=" + errCode + ";" + message, Toast.LENGTH_SHORT).show();
						Log.i("tag", "errCode==" + errCode + "/" + message);
					}
				});
				finish();
			}

			@Override
			public void netOverDute() {
				LoveAroundActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mContext, " 注册超时！", Toast.LENGTH_SHORT).show();
					}
				});
				finish();
			}

			@Override
			public void netErr() {
				LoveAroundActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mContext, "网络错误，请检查网络！", Toast.LENGTH_SHORT).show();
					}
				});
				finish();
			}

			@Override
			public void getNetSucceed() {
				//注册成功
				login(user);
			}
		});
	}
}

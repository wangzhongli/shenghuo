package com.e1858.common.app;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.e1858.common.R;
import com.e1858.wuye.protocol.http.BannerBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public abstract class BaseApplication extends Application {

	private static BaseApplication	sBaseIntance;
//	SharedPreferences				sharedPreferences;
	DisplayImageOptions				displayImageOptions			= new DisplayImageOptions.Builder()
																		.cacheInMemory(true)
																		.cacheOnDisk(true)
																		.considerExifParams(true)
																		.showImageOnLoading(
																				R.drawable.wuye_cm_image_placeholder)
																		.showImageForEmptyUri(
																				R.drawable.wuye_cm_image_placeholder)
																		.showImageOnFail(
																				R.drawable.wuye_cm_image_placeholder)
																		.bitmapConfig(Bitmap.Config.RGB_565).build();
	DisplayImageOptions				displayImageOptions_empty	= new DisplayImageOptions.Builder().cacheInMemory(true)
																		.cacheOnDisk(true).considerExifParams(true)
																		.bitmapConfig(Bitmap.Config.RGB_565).build();

	@Override
	public void onCreate() {
		super.onCreate();
		sBaseIntance = this;
	}

	public static BaseApplication getBaseInstance() {
		return sBaseIntance;
	}

//	public static SharedPreferences sharedPreferences() {
//		if (sBaseIntance.sharedPreferences == null) {
//			sBaseIntance.sharedPreferences = sBaseIntance
//					.getSharedPreferences(Constant.USER_INFO, Context.MODE_PRIVATE);
//		}
//		return sBaseIntance.sharedPreferences;
//	}

	public static DisplayImageOptions displayImageOptions() {
		return sBaseIntance.displayImageOptions;
	}

	public static DisplayImageOptions displayImageOptions_empty() {
		return sBaseIntance.displayImageOptions_empty;
	}

	//判断是否需要登录,如果需要没有登录,则跳转到登陆界面
	public abstract boolean verifyLoggedInAndGoToLogin(Activity activity);

	public abstract boolean judgeForBannerUrl(Activity activity, BannerBean banner);

	//检查网络是否可用
	public abstract boolean isNetworkAvailable();
}

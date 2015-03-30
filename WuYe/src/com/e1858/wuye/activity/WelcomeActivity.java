package com.e1858.wuye.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.e1858.common.WuYeConstant.PrefKey;
import com.e1858.utils.PreferencesUtils;
import com.e1858.wuye.R;

/**
 * 日期: 2015年1月27日 下午3:33:07
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class WelcomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		boolean flag = PreferencesUtils.getBoolean(PrefKey.Shortcut, false);
		if (!flag && !hasShortcut()) {
			setUpShortCut();
		}
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
				finish();
			}
		}, 1000);
	}

	/**
	 * 判断是否已经创建了快捷方式
	 * 
	 * @return
	 */
	private boolean hasShortcut() {
		boolean isInstallShortcut = false;
		Cursor c = null;
		try {
			final ContentResolver cr = this.getContentResolver();
			final String AUTHORITY = "com.android.launcher.settings";
			final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
			c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "title=?", new String[] { this
					.getString(R.string.app_name).trim() }, null);
			if (c != null && c.getCount() > 0) {
				isInstallShortcut = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (c != null) {
				c.close();
			}
		}
		return isInstallShortcut;
	}

	/**
	 * 启动程序时创建桌面快捷方式
	 */
	private void setUpShortCut() {

		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 设置快捷方式图片
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));
		// 设置快捷方式名称
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
		// 设置是否允许重复创建快捷方式 false表示不允许
		intent.putExtra("duplicate", false);
		// 设置快捷方式要打开的intent
		// 第一种方法创建快捷方式要打开的目标intent
		Intent targetIntent = new Intent();
		// 设置应用程序卸载时同时也删除桌面快捷方式
		targetIntent.setAction(Intent.ACTION_MAIN);
		targetIntent.addCategory("android.intent.category.LAUNCHER");
		targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		ComponentName componentName = new ComponentName(getPackageName(), this.getClass().getName());
		targetIntent.setComponent(componentName);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
		// 发送广播
		sendBroadcast(intent);
		PreferencesUtils.putBoolean(PrefKey.Shortcut, true);
	}
}

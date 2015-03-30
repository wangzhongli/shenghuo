package com.e1858.wuye.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.utils.MLog;
import com.e1858.common.Constant;
import com.e1858.common.LoginManager;
import com.e1858.common.MessageWhat;
import com.e1858.common.app.AppManager;
import com.e1858.common.widget.CustomProgressDialog;
import com.e1858.utils.Encrypt;
import com.e1858.utils.Exit;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.LoginResp;
import com.e1858.wuye.protocol.http.SetGetuiClientIDResp;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
/**
 * activity基类
 * @author jiajia
 *
 */
public class BaseActivity extends ActionBarActivity implements Handler.Callback {
	private static final String TAG = "BaseActivity";
	protected TextView bar_title;
	protected Button bar_leftBtn;
	protected Button bar_rightBtn;
	protected MainApplication application;
	protected Handler handler;
	// private ProgressDialog progressDialog;
	private ProgressDialog progressDialog = null;
	protected Exit exit;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	protected View footer_view;
	protected ProgressBar footer_bar;
	protected TextView footer_more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getSupportActionBar().hide();
		if (null == application) {
			application = (MainApplication) this.getApplication();
		}
		if (null == handler) {
			handler = new Handler(this);
			application.setCurrentHandler(handler);
		}
//		if (null == PreferencesUtils.getCommunity()) {
//			Util.saveStatus(application, getSp());
//		}
		exit = new Exit();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.convenience_store_default_icon)
				.showImageForEmptyUri(R.drawable.convenience_store_default_icon)
				.showImageOnFail(R.drawable.convenience_store_default_icon)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	//	application.setCurrentActivity(BaseActivity.this);
		AppManager.getAppManager().addActivity(this);
		PushManager.getInstance().initialize(this.getApplicationContext());
		// PushManager.getInstance().getClientid(this.getApplicationContext());
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, this.getClass().getName().concat(" onStart"));
		application.setCurrentHandler(handler);
	//	application.setCurrentActivity(this);
		if (null != bar_leftBtn) {
			bar_leftBtn.setOnClickListener(this.button_backOnClickListener);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, this.getClass().getName().concat(" onRestart"));
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		Log.d(TAG, this.getClass().getName().concat(" onResume"));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		Log.d(TAG, this.getClass().getName().concat(" onPause"));
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, this.getClass().getName().concat(" onStop"));
	}

	@Override
	protected void onDestroy() {
		handler = null;
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
		Log.d(TAG, this.getClass().getName().concat(" onDestroy"));
	}
	
	public boolean isDestroyedApi8() {
		return handler == null;
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			return true;
		}
		switch (msg.what) {
		case MessageWhat.OPEN_PROGRESS_DIALOG:
			openProgressDialog((String) msg.obj);
			break;
		case MessageWhat.CLOSE_PROGRESS_DIALOG:
			if (null != progressDialog) {
				progressDialog.dismiss();
			}
			break;
		case Constant.FAIL_CODE:
			if (null != progressDialog) {
				closeProgressDialog();
			}
			if (msg.obj != null) {
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case HttpDefine.LOGIN_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj) {
				LoginResp loginResp = JsonUtil.fromJson((String) msg.obj,
						LoginResp.class);
				if (null == loginResp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == loginResp.getRet()) {
//					application.setKey(loginResp.getKey());
					PreferencesUtils.setUserID(loginResp.getUserID());
//					application.setToken(Encrypt.MD5(loginResp.getKey()
//							+ Constant.TokenSalt));
					if (loginResp.getCommunity() != null) {
						PreferencesUtils.setCommunity(loginResp.getCommunity());
					}
					PreferencesUtils.setUserInfo(loginResp.getUserInfo());
					PreferencesUtils.setUserHouse(loginResp.getUserHouse());
					PreferencesUtils.setLoginKey(loginResp.getKey());
					PreferencesUtils.setUserID(loginResp.getUserID());
					LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), 
							PushManager.getInstance().getClientid(getApplicationContext()), 
							loginResp.getUserID(), loginResp.getKey(),  
							Encrypt.MD5(loginResp.getKey()+Constant.TokenSalt));
					this.finish();
				} else {
					ToastUtil.show(this, loginResp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.SET_GETUI_CLIENTID_RESP:
			if ((String) msg.obj != null) {
				SetGetuiClientIDResp resp = JsonUtil.fromJson(
						(String) msg.obj, SetGetuiClientIDResp.class);
				if(resp==null){
					break;
				}
				if (resp.getRet()==HttpDefine.RESPONSE_SUCCESS){
					Log.i("TAG", "上传成功~");
				}else{
					Log.i("TAG", "失败");
				}
			}
			msg.obj = null;
			break;
		}
		return true;
	}

	public void openProgressDialog(final String message) {
		if (null == progressDialog) {
			progressDialog = new ProgressDialog(this);
			// progressDialog.setCancelable(false);
			// progressDialog.setCanceledOnTouchOutside(false);

		}
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private View.OnClickListener button_backOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			AppManager.getAppManager().finishActivity();

		}
	};
}

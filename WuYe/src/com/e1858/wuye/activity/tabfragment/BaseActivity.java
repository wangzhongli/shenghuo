package com.e1858.wuye.activity.tabfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.common.LoginManager;
import com.e1858.common.MessageWhat;
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
public class BaseActivity extends Fragment implements Handler.Callback {
	private static final String TAG = "BaseActivity";
	//protected TextView bar_title;
	//protected Button bar_leftBtn;
	//protected Button bar_rightBtn;
	protected MainApplication application;
	protected Handler handler;
	// private ProgressDialog progressDialog;
	private CustomProgressDialog progressDialog = null;
	protected View footer_view;
	protected ProgressBar footer_bar;
	protected TextView footer_more;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (null == application) {
			application = MainApplication.getInstance();
		}
		if (null == handler) {
			handler = new Handler(this);
			application.setCurrentHandler(handler);
		}
//		if (null == PreferencesUtils.getCommunity()) {
//			Util.saveStatus(application, getSp());
//		}
		// PushManager.getInstance().getClientid(this.getApplicationContext());
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, this.getClass().getName().concat(" onStart"));
		application.setCurrentHandler(handler);
//		if (null != bar_leftBtn) {
//			bar_leftBtn.setOnClickListener(this.button_backOnClickListener);
//		}
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(getClass().getSimpleName()); 
	}
	
	protected View findViewById(int id) {
		return getView().findViewById(id);
	}
	
	Context getApplicationContext() 
	{
		return getActivity().getApplicationContext();
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (getActivity() == null || isDetached()) {
			return true;
		}
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			return true;
		}
		switch (msg.what) {
		case MessageWhat.OPEN_PROGRESS_DIALOG:
			if (null == progressDialog) {
				progressDialog = CustomProgressDialog.createDialog(getActivity());
				// progressDialog.setCancelable(false);
			}
			progressDialog.setMessage((String) msg.obj);
			progressDialog.show();
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
				ToastUtil.show(getActivity(), msg.obj.toString());
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
						PreferencesUtils.setCommunity(loginResp.getCommunity());
						PreferencesUtils.setUserInfo(loginResp.getUserInfo());
						PreferencesUtils.setUserHouse(loginResp.getUserHouse());
						PreferencesUtils.setLoginKey(loginResp.getKey());
						PreferencesUtils.setUserID(loginResp.getUserID());
					LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), PushManager.getInstance().getClientid(getActivity().getApplicationContext()),
							loginResp.getUserID(), PreferencesUtils.getLoginKey(), Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				} else {
					ToastUtil.show(getActivity(), loginResp.getError());
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
			progressDialog = CustomProgressDialog.createDialog(getActivity());
			// progressDialog.setCancelable(false);
			// progressDialog.setCanceledOnTouchOutside(false);

		}
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
		}
	}

//	private final View.OnClickListener button_backOnClickListener = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			AppManager.getAppManager().finishActivity();
//
//		}
//	};
}

package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.e1858.common.Constant;
import com.e1858.common.LoginManager;
import com.e1858.common.app.AppManager;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.tabfragment.MainTabActivity;
import com.e1858.wuye.adapter.CommunityAdapter;
import com.e1858.wuye.protocol.http.ChangeCommunity;
import com.e1858.wuye.protocol.http.ChangeCommunityResp;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.GetCommunityByLocation;
import com.e1858.wuye.protocol.http.GetCommunityByLocationResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.Login;
import com.e1858.wuye.protocol.http.LoginResp;
import com.igexin.sdk.PushManager;

/**
 * 根据定位获取周边小区信息
 * 
 * @author jiajia
 */
public class LocationCommunityActivity extends BaseActivity {
	private ListView				mDownListView;
	private final List<Community>	lists			= new ArrayList<Community>();
	private CommunityAdapter		adapter;
	private Button					manualBtn;
	public LocationClient			mLocationClient	= null;
	public BDLocationListener		myListener		= new MyLocationListener();
	private double					latitude		= 0.0;
	private double					longitude		= 0.0;
	private String					cityName		= "";
	private int						remark			= -1;
	private TextView				tipTextView;
	private static String			tipError		= "无法定位到您附近的小区,请手动选择";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_community);
		if (getIntent().getExtras() != null) {
			remark = getIntent().getExtras().getInt(Constant.PIC_REMARK);
		}
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		setLocationOption();
		adapter = new CommunityAdapter(this, lists);
		if (remark != -1) {
			initView();
			mLocationClient.start();
		} else {
			judgePage();
		}
	}

	private void judgePage() {
		try {
			if (null == PreferencesUtils.getCommunity()) {
				initView();
				mLocationClient.start();
			} else {
				if (PreferencesUtils.getUserID() > 0) {
					autoLogin();
				}
				Intent intent = new Intent(this, MainTabActivity.class);
				startActivity(intent);
				finish();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void autoLogin() {
		if (application.isNetworkAvailable()) {
			if (!StringUtils.isEmpty(PreferencesUtils.getUserName())) {
				String userName = PreferencesUtils.getUserName();
				String password = PreferencesUtils.getPassword();
				Login login = new Login();
				login.setCommunityID(PreferencesUtils.getCommunity().getID());
				login.setUsername(userName);
				login.setPassword(password);
				login.setPassword(Encrypt.MD5(password));
				login.setToken(Encrypt.MD5(userName + Encrypt.MD5(password) + Constant.TokenSalt));
				NetUtil.post(Constant.BASE_URL, login, handler, HttpDefine.LOGIN_RESP);
			}
		} else {
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append("\ncity:").append(location.getCity());
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			cityName = location.getCity();
			application.setCityName(cityName);
			mLocationClient.stop();
			loadData(true);
			Log.i("sb", sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	private void initView() {
		tipTextView = (TextView) findViewById(R.id.textview);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(R.string.text_bar_switchCommunity));
		mDownListView = (ListView) findViewById(R.id.listview);
		manualBtn = (Button) findViewById(R.id.ok_btn);
		if (remark != -1) {
			bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
			bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
			bar_leftBtn.setVisibility(View.VISIBLE);
		}
		mDownListView.setAdapter(adapter);
		manualBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 进入选择城市页面
				Intent intent = new Intent(LocationCommunityActivity.this, SwitchCityActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("CurrentCity", cityName);
				if (remark != -1) {
					bundle.putInt(Constant.PIC_REMARK, 1);
				} else {
					bundle.putInt(Constant.PIC_REMARK, -1);
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mDownListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Community community = (Community) mDownListView.getAdapter().getItem(position);
				ChangeCommunity changeCommunity = new ChangeCommunity();
				if (remark == 1 && PreferencesUtils.getUserID() > 0) {
//					changeCommunity.setKey(application.getKey());
//					changeCommunity.setToken(application.getToken());\
					changeCommunity.setKey(PreferencesUtils.getLoginKey());
					changeCommunity.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
				} else {
					changeCommunity.setToken(Encrypt.MD5(Constant.TokenSalt));
				}
				changeCommunity.setCommunityID(community.getID());
				NetUtil.post(Constant.BASE_URL, changeCommunity, handler, HttpDefine.CHANGE_COMMUNITY_RESP);
			}
		});
	}

	private void loadData(boolean isDropDown) {
		if (application.isNetworkAvailable()) {
			GetCommunityByLocation getCommunityByLocation = new GetCommunityByLocation();
			getCommunityByLocation.setLatitude(latitude);
			getCommunityByLocation.setLongitute(longitude);
			getCommunityByLocation.setCity(cityName);
			getCommunityByLocation.setToken(Encrypt.MD5(Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getCommunityByLocation, handler, HttpDefine.GET_COMMUNITY_BYLOCATION_RESP);
		} else {
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
			tipTextView.setText(tipError);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			// mDownListView.onBottomComplete();
			return true;
		}
		switch (msg.what) {
		case HttpDefine.GET_COMMUNITY_BYLOCATION_RESP:
			if (null != (String) msg.obj) {
				GetCommunityByLocationResp resp = JsonUtil.fromJson((String) msg.obj, GetCommunityByLocationResp.class);
				if (null == resp) {
					tipTextView.setText(tipError);
					break;
				} else {
					if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
						lists.addAll(resp.getCommunities());
						initData(lists);
						if (lists.size() > 0) {
							tipTextView.setText("以下是定位到您附近的小区");
						} else {
							tipTextView.setText("您附近没有小区,请手动选择");
						}
					} else {
						ToastUtil.show(this, resp.getError());
						tipTextView.setText(tipError);
					}
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.CHANGE_COMMUNITY_RESP:
			if (null != (String) msg.obj) {
				ChangeCommunityResp changeCommunityResp = JsonUtil
						.fromJson((String) msg.obj, ChangeCommunityResp.class);
				if (null == changeCommunityResp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == changeCommunityResp.getRet()) {

					if (remark != -1) {
						PreferencesUtils.setUserHouse(changeCommunityResp.getUserHouse());
						PreferencesUtils.setCommunity(changeCommunityResp.getCommunity());
						application.setIsCommunityChanged(true);
						application.setIsConvienceChanged(true);
//						AppManager.getAppManager().finishActivity();
						Intent intent = new Intent(this, MainTabActivity.class);
						startActivity(intent);
						AppManager.getAppManager().finishActivity();
					} else {
						Intent intent = new Intent(this, MainTabActivity.class);
						startActivity(intent);
						AppManager.getAppManager().finishActivity();
						PreferencesUtils.setCommunity(changeCommunityResp.getCommunity());
					}
					int userID = PreferencesUtils.getUserID();
					if (userID > 0) {
						LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), PushManager
								.getInstance().getClientid(getApplicationContext()), userID, PreferencesUtils
								.getLoginKey(), Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
					} else {
						LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), PushManager
								.getInstance().getClientid(getApplicationContext()), userID, "", Encrypt
								.MD5(Constant.TokenSalt));
					}

				} else {
					ToastUtil.show(this, changeCommunityResp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.LOGIN_RESP:
			if (null != (String) msg.obj) {
				LoginResp loginResp = JsonUtil.fromJson((String) msg.obj, LoginResp.class);
				if (null == loginResp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == loginResp.getRet()) {
//					application.setKey(loginResp.getKey());
					PreferencesUtils.setUserID(loginResp.getUserID());
//					application.setToken(Encrypt.MD5(loginResp.getKey()+Constant.TokenSalt));

					PreferencesUtils.setCommunity(loginResp.getCommunity());
					PreferencesUtils.setUserInfo(loginResp.getUserInfo());
					PreferencesUtils.setUserHouse(loginResp.getUserHouse());
					PreferencesUtils.setUserID(loginResp.getUserID());
					PreferencesUtils.setLoginKey(loginResp.getKey());
					LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), PushManager
							.getInstance().getClientid(getApplicationContext()), loginResp.getUserID(),
							PreferencesUtils.getLoginKey(), Encrypt.MD5(PreferencesUtils.getLoginKey()
									+ Constant.TokenSalt));
				} else {
//					ToastUtil.show(this, loginResp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	private void initData(List<Community> list) {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (remark != -1) {
				AppManager.getAppManager().finishActivity(this);
			} else {
				if (exit.isExit()) {
					if (null != application.getDbManager()) {
						application.getDbManager().closeDatabase();
					}
					AppManager.getAppManager().AppExit(getApplicationContext());

				} else {
					ToastUtil.show(getApplicationContext(), "再按一次返回键回到桌面");
					// 退出
					exit.doExitInOneSecond();

				}
			}

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
//		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
//		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
//		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setAddrType("all");
		mLocationClient.setLocOption(option);
	}
}

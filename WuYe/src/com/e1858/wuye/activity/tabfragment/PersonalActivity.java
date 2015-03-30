package com.e1858.wuye.activity.tabfragment;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.mall.MallMineActivity;
import com.e1858.network.NetUtil;
import com.e1858.point.PointActivity;
import com.e1858.utils.Encrypt;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.widget.CircularImage;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.CommissionsActivity;
import com.e1858.wuye.activity.ComplaintsActivity;
import com.e1858.wuye.activity.LoginActivity;
import com.e1858.wuye.activity.MaintenancesActivity;
import com.e1858.wuye.activity.MoreActivity;
import com.e1858.wuye.activity.MyConvenienceRecordActivity;
import com.e1858.wuye.activity.MyTopicActivity;
import com.e1858.wuye.activity.NoticesActivity;
import com.e1858.wuye.activity.PaymentRecordActivity;
import com.e1858.wuye.activity.PersonalInfoActivity;
import com.e1858.wuye.activity.RegisterActivity;
import com.e1858.wuye.protocol.http.GetMyProfile;
import com.e1858.wuye.protocol.http.GetMyProfileResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.Logout;
import com.e1858.wuye.protocol.http.LogoutResp;
import com.e1858.wuye.protocol.http.PointProfileBean;
import com.e1858.wuye.protocol.http.UserHouse;
import com.e1858.wuye.protocol.http.UserInfo;
import com.hg.android.utils.HGUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 我的
 * @author jiajia 2014年8月22日
 *
 */
public class PersonalActivity extends BaseActivity implements OnClickListener
{
	private TextView nickname;
	private TextView house_info;
	private CircularImage head_portrait;
	private RelativeLayout my_bill;
	private RelativeLayout my_maintenance_search;
	private RelativeLayout my_notice;
	private RelativeLayout my_topic;
	private RelativeLayout my_convenience_record;
	private RelativeLayout my_shopping;
	private RelativeLayout my_point;
	private RelativeLayout my_commission;
	private RelativeLayout my_complaint;
//	private RelativeLayout my_investment;
	private Button loginOutBtn;
	private LinearLayout has_login;
	public static boolean isRefreshed = false;
	public static String nickName = "";
	public static Bitmap headBitmap;
	private Dialog loginOutDialog;
	//未登录
	private RelativeLayout no_Login;
	private Button login_btn;
	private Button regist_btn;
	private DisplayImageOptions	options;
	
	//private TextView bar_right_text;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.personal, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.personal_head_icon)
		.showImageForEmptyUri(R.drawable.personal_head_icon)
		.showImageOnFail(R.drawable.personal_head_icon)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		initView();
		judgePage();
	}

	private void judgePage(){
		if(PreferencesUtils.getUserID() > 0){
			loadData(true,true);
		}else{
			no_Login.setVisibility(View.VISIBLE);
			loginOutBtn.setVisibility(View.GONE);
			has_login.setVisibility(View.GONE);
		}
	}
	private void initView()
	{
		//bar_title = (TextView) findViewById(R.id.bar_title);
//		bar_right_text = (TextView) findViewById(R.id.bar_right_text);
		nickname = (TextView) findViewById(R.id.nickname);
		house_info = (TextView) findViewById(R.id.house_info);
		head_portrait = (CircularImage) findViewById(R.id.head_portrait);
		my_bill = (RelativeLayout) findViewById(R.id.my_bill);
		my_maintenance_search = (RelativeLayout) findViewById(R.id.my_maintenance_search);
		my_notice = (RelativeLayout) findViewById(R.id.my_notice);
		my_topic = (RelativeLayout) findViewById(R.id.my_topic);
		my_convenience_record=(RelativeLayout)findViewById(R.id.my_convenience_record);
		my_shopping = (RelativeLayout) findViewById(R.id.my_shopping);
		my_point = (RelativeLayout) findViewById(R.id.my_point);
		my_commission=(RelativeLayout)findViewById(R.id.my_commission);
		my_complaint=(RelativeLayout)findViewById(R.id.my_complaint);
//		my_investment=(RelativeLayout)findViewById(R.id.my_investment);
		has_login = (LinearLayout) findViewById(R.id.has_login);
		loginOutBtn = (Button) findViewById(R.id.login_out_btn);
		//bar_title.setText(getResources().getString(R.string.text_bar_personal));
//		bar_right_text.setVisibility(View.VISIBLE);
//		bar_right_text.setText("更多");
//		bar_rightBtn.setBackgroundResource(R.drawable.bar_more_background);
		no_Login=(RelativeLayout)findViewById(R.id.no_login);
		login_btn=(Button)findViewById(R.id.login_btn);
		regist_btn=(Button)findViewById(R.id.regist_btn);
		my_commission.setOnClickListener(this);
		my_complaint.setOnClickListener(this);
//		my_investment.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		regist_btn.setOnClickListener(this);
//		bar_right_text.setOnClickListener(this);
		has_login.setOnClickListener(this);
		my_bill.setOnClickListener(this);
		my_maintenance_search.setOnClickListener(this);
		my_notice.setOnClickListener(this);
		my_shopping.setOnClickListener(this);
		my_topic.setOnClickListener(this);
		my_point.setOnClickListener(this);
		loginOutBtn.setOnClickListener(this);
		my_convenience_record.setOnClickListener(this);
	}

	// 加载个人资料
	private void loadData(boolean isLocalCache,boolean loadFromWeb)
	{
		if(isLocalCache){
			UserInfo userInfo=PreferencesUtils.getUserInfo();
			if(userInfo!=null){
				initData(userInfo);
			}
		}
		if(loadFromWeb){
			if (application.isNetworkAvailable())
			{
//				openProgressDialog("加载中...");
				GetMyProfile getMyProfile = new GetMyProfile();
				getMyProfile.setCommunityID(PreferencesUtils.getCommunity().getID());
//				getMyProfile.setKey(application.getKey());
//				getMyProfile.setToken(application.getToken());
				getMyProfile.setKey(PreferencesUtils.getLoginKey());
				getMyProfile.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				NetUtil.post(Constant.BASE_URL, getMyProfile, handler, HttpDefine.GET_MY_PROFILE_RESP);
			}
			else
			{
				ToastUtil.show(getActivity(), getResources().getString(R.string.network_fail));
			}
		}
		

	}

	private void initData(UserInfo userInfo)
	{
		if (StringUtils.isEmpty(userInfo.getNickname()))
		{
			nickname.setText("昵称");
		}
		else
		{
			nickname.setText(userInfo.getNickname());
		}
		house_info.setText( Util.getHouseInfo(true));
		
		if (!StringUtils.isEmpty(userInfo.getHeadPortrait()))
		{
			// 设置图片
			ImageLoader.getInstance().displayImage(userInfo.getHeadPortrait(), head_portrait, options);
		}else{
			ImageLoader.getInstance().displayImage(null, head_portrait, options);
		}
		loginOutBtn.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		if (getActivity() == null || isDetached()) {
			return true;
		}
		// TODO Auto-generated method stub
		if (null == msg.obj)
		{
//			closeProgressDialog();
			return true;
		}
		switch (msg.what)
		{
		case HttpDefine.GET_MY_PROFILE_RESP:
//			closeProgressDialog();
			if (null != (String) msg.obj)
			{
				GetMyProfileResp resp = JsonUtil.fromJson((String) msg.obj, GetMyProfileResp.class);
				if (null == resp)
				{
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet())
				{
					// 成功个人资料
					PreferencesUtils.setUserInfo(resp.getUserInfo());
					initData(resp.getUserInfo());
				}
				else
				{
					if(PreferencesUtils.getUserID() <= 0){
						ToastUtil.show(getActivity(), resp.getError());
					}
					
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		boolean isLogin=PreferencesUtils.getUserID() > 0;
		Intent intent = new Intent();
		switch (v.getId())
		{
		case R.id.login_btn:
			intent.setClass(getActivity(), LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.regist_btn:
			intent.setClass(getActivity(), RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.has_login:
			intent.setClass(getActivity(), PersonalInfoActivity.class);
			startActivityForResult(intent, Constant.PERSONAL_INFO_RESULT_CODE);
			break;
		case R.id.bar_right_text:
			intent.setClass(getActivity(), MoreActivity.class);
			startActivity(intent);
			break;
		case R.id.my_bill:
			if(isLogin){
//				ToastUtil.show(getActivity(), "此功能暂未开放!");
//				break;
				intent.setClass(getActivity(), PaymentRecordActivity.class);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.my_maintenance_search:
			if(isLogin){
				intent.setClass(getActivity(), MaintenancesActivity.class);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.my_notice:
			if(isLogin){
				intent.setClass(getActivity(), NoticesActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt(Constant.PIC_REMARK, 1);
				intent.putExtras(bundle);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.my_topic:
			if(isLogin){
				intent.setClass(getActivity(), MyTopicActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(MyTopicActivity.USERID, PreferencesUtils.getUserID());
				intent.putExtras(bundle);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			
			break;
		case R.id.my_commission:
			if(isLogin){
				intent.setClass(getActivity(), CommissionsActivity.class);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.my_complaint:
			if(isLogin){
				intent.setClass(getActivity(), ComplaintsActivity.class);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.my_convenience_record:
			if(isLogin){
				intent.setClass(getActivity(), MyConvenienceRecordActivity.class);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;
		/*case R.id.my_investment:
			if(isLogin){
				ToastUtil.show(getActivity(), "此功能暂未开放!");
				break;
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;*/
		case R.id.my_shopping:
			if(isLogin){
				intent.setClass(getActivity(), MallMineActivity.class);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.my_point:
			if(isLogin){
				intent.setClass(getActivity(), PointActivity.class);
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.login_out_btn:
			if(isLogin){
				createLoginOut();
			}else{
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			
			break;
		}
	}


	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		UserInfo userInfo=PreferencesUtils.getUserInfo();
		if(userInfo!=null){
			initData(userInfo);
		}
		if(PreferencesUtils.getUserID() > 0||application.getIsRefresh()){
			no_Login.setVisibility(View.GONE);
			has_login.setVisibility(View.VISIBLE);
			loadData(false,true);
			application.setIsRefresh(false);
		}else{
			no_Login.setVisibility(View.VISIBLE);
			has_login.setVisibility(View.GONE);
		}
		
		if (!StringUtils.isEmpty(nickName))
		{
			nickname.setText(nickName);
		}
		if (null != headBitmap)
		{
			head_portrait.setImageBitmap(headBitmap);
		}
	}

	private void createLoginOut()
	{
		loginOutDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
		loginOutDialog.setContentView(R.layout.login_out_dialog);
		Window window = loginOutDialog.getWindow();  
		WindowManager.LayoutParams lp=window.getAttributes(); 
		lp.dimAmount=0.8f;  
		window.setAttributes(lp); 
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		Button ok_btn = (Button) loginOutDialog.findViewById(R.id.ok_btn);
		Button cancleBtn = (Button) loginOutDialog.findViewById(R.id.cancel_btn);
		ok_btn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				loginOutDialog.dismiss();
				Logout logout=new Logout();
				logout.setCommunityID(PreferencesUtils.getCommunity().getID());
				logout.setKey(PreferencesUtils.getLoginKey());
				logout.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				ResponseHandler<LogoutResp> responseHandler = new ResponseHandler<LogoutResp>() {
					
					@Override
					public void onStart() {
						showProgressDialog(getActivity(), "正在退出登录");
					}
					
					@Override
					public void onFinish(LogoutResp response, String error) {
						hideProgressDialog();
						ToastUtil.show(getActivity(), "已退出登录");
						PreferencesUtils.setLoginKey("");
						PreferencesUtils.setUserID(0);
						no_Login.setVisibility(View.VISIBLE);
						has_login.setVisibility(View.GONE);
						loginOutBtn.setVisibility(View.GONE);
					}
				};
				HttpPacketClient.postPacketAsynchronous(logout, LogoutResp.class, responseHandler );
			}
		});
		cancleBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				loginOutDialog.dismiss();
			}
		});
		loginOutDialog.show();
	}
	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.personal, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_personal_more) {
			startActivity(new Intent(getActivity(), MoreActivity.class));
			return true;
		}
		return false;
	}
}

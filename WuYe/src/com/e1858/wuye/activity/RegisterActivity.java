package com.e1858.wuye.activity;
import java.util.regex.Pattern;

import com.e1858.common.Constant;
import com.e1858.common.LoginManager;
import com.e1858.common.app.AppManager;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.SIMCardUtil;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.GetVerifyCode;
import com.e1858.wuye.protocol.http.GetVerifyCodeResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.Regist;
import com.e1858.wuye.protocol.http.RegistResp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 注册
 * 
 * @author jiajia 2014年7月22日
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {

	private EditText muserName;
	private EditText mpassword;
	private Button mregisterBtn;
	private CheckBox magreement;
	private TextView magreement_Link;
	private EditText mVerify;
	private Button mget_verifyBtn;
	private TimeCount time;
	private boolean isFinished=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		time = new TimeCount(90000, 1000);//构造CountDownTimer对象
		initView();
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		muserName = (EditText) findViewById(R.id.input_phone);
		mpassword = (EditText) findViewById(R.id.input_password);
		mregisterBtn = (Button) findViewById(R.id.register_btn);
		magreement = (CheckBox) findViewById(R.id.agreement_checkbox);
		magreement_Link = (TextView) findViewById(R.id.agreement_textview);
		mVerify = (EditText) findViewById(R.id.input_verify);
		mget_verifyBtn = (Button) findViewById(R.id.get_verify_btn);
		// magreement_Link.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(R.string.text_bar_register));
		mget_verifyBtn.setOnClickListener(this);
		mregisterBtn.setOnClickListener(this);
		magreement_Link.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_btn:
			if (StringUtils.isBlank(muserName.getText().toString())) {
				ToastUtil.show(this, Constant.ToastMessage.EMPTY_USERNAME);
			} else if (!Pattern.matches(Constant.MOBILE_REGP, muserName
					.getText().toString())) {
				ToastUtil.show(this, "请输入有效的手机号码");
			} else if (StringUtils.isBlank(mpassword.getText().toString())) {
				ToastUtil.show(this, Constant.ToastMessage.EMPTY_PASSWORD);
			} else if (!Pattern.matches(Constant.PASSWORD_REGP, mpassword
					.getText().toString())
					|| StringUtils.getStringLength(mpassword.getText()
							.toString()) < 6
					|| StringUtils.getStringLength(mpassword.getText()
							.toString()) > 24) {
				ToastUtil
						.show(this, Constant.ToastMessage.PASSWORD_LACK_LENGTH);
			} else if (StringUtils.isBlank(mVerify.getText().toString())) {
				ToastUtil.show(this, Constant.ToastMessage.EMPTY_VERIFY);
			} else if (!magreement.isChecked()) {
				ToastUtil.show(this, Constant.ToastMessage.AGREEMENT);
			} else {
				registe();
			}
			break;
		case R.id.agreement_textview:
			// 显示协议
			Intent intent = new Intent();
			intent.setClass(this, AppProtocolActivity.class);
			startActivity(intent);
			break;
		case R.id.get_verify_btn:
			if (StringUtils.isEmpty(muserName.getText().toString())) {
				ToastUtil.show(this, "手机号不能为空");
			} else if (!Pattern.matches(Constant.MOBILE_REGP, muserName
					.getText().toString())) {
				ToastUtil.show(this, "请输入有效的手机号");
			} else {
				GetVerifyCode getVerifyCode = new GetVerifyCode();
				getVerifyCode.setDeviceToken(SIMCardUtil.getDeviceId(this));
				getVerifyCode.setActionType(Constant.ACTION_TYPE.REGISTE);
				getVerifyCode.setMobile(muserName.getText().toString());
				getVerifyCode.setToken(Encrypt.MD5(muserName.getText().toString()+Constant.TokenSalt));
				NetUtil.post(Constant.BASE_URL, getVerifyCode, handler,
						HttpDefine.GET_VERIFY_CODE_RESP);
			}
			break;
		}

	}

	private void registe() {
		if (application.isNetworkAvailable()) {
			try {
				isFinished=true;
				openProgressDialog("请稍候...");
				Regist regist = new Regist();
				regist.setCommunityID(PreferencesUtils.getCommunity().getID());
				regist.setMobile(muserName.getText().toString());
				regist.setVerify(mVerify.getText().toString());
				regist.setToken(Encrypt.MD5(muserName.getText().toString()
						+ Encrypt.MD5(mpassword.getText().toString())
						+ Constant.TokenSalt));
				regist.setPassword(Encrypt.MD5((mpassword.getText().toString())));
				NetUtil.post(Constant.BASE_URL, regist, handler,
						HttpDefine.REGIST_RESP);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			closeProgressDialog();
			return true;
		}
		switch (msg.what) {
		case HttpDefine.GET_VERIFY_CODE_RESP:
			if (null != (String) msg.obj) {
				closeProgressDialog();
				GetVerifyCodeResp getVerifyCodeResp = JsonUtil.fromJson(
						(String) msg.obj, GetVerifyCodeResp.class);
				if (null == getVerifyCodeResp) {
					break;
				}
				if (getVerifyCodeResp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					// 发送手机验证
					time.start();//开始计时
					isFinished=false;
					break;
				} else {
					isFinished=true;
					time.cancel();
					ToastUtil.show(this, getVerifyCodeResp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.REGIST_RESP:
			if (null != (String) msg.obj) {
				closeProgressDialog();
				RegistResp registResp = JsonUtil.fromJson((String) msg.obj,
						RegistResp.class);
				if (null == registResp) {
					break;
				}
				if (registResp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					PreferencesUtils.setUserName(muserName.getText().toString());
					PreferencesUtils.setPassword(mpassword.getText().toString());
					LoginManager.login(application, handler, muserName.getText().toString(), mpassword.getText().toString());
					AppManager.getAppManager().finishActivity(LoginActivity.class);
				} else {
					ToastUtil.show(this, registResp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() 
		{
			//计时完毕时触发
			mget_verifyBtn.setText("获取验证码");
			mget_verifyBtn.setEnabled(true);
		}
		
		@Override
		public void onTick(long millisUntilFinished)
		{//计时过程显示
			if(isFinished){
				time.cancel();
				mget_verifyBtn.setText("获取验证码");
				mget_verifyBtn.setEnabled(true);
			}else{
				mget_verifyBtn.setEnabled(false);
				mget_verifyBtn.setText(millisUntilFinished /1000+"秒");
			}
			
		}
	}
}

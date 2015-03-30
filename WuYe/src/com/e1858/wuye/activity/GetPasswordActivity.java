package com.e1858.wuye.activity;

import java.util.regex.Pattern;

import com.e1858.common.Constant;
import com.e1858.common.app.AppManager;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.SIMCardUtil;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.GetVerifyCode;
import com.e1858.wuye.protocol.http.GetVerifyCodeResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.ResetPass;
import com.e1858.wuye.protocol.http.ResetPassResp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 找回密码
 * 
 * @author jiajia 2014年7月22日
 * 
 */
public class GetPasswordActivity extends BaseActivity implements OnClickListener
{
	public static final String USERNAME = "USERNAME";
	private EditText mPhone;
	private EditText mVerify;
	private EditText mpassword;
	private Button mget_verifyBtn;
	private Button mokBtn;
	private EditText mconfirm_password;
	private TimeCount time;
	private boolean isFinished=false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		time=new TimeCount(90000, 1000);
		initView();
	}

	private void initView()
	{
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		mPhone = (EditText) findViewById(R.id.input_phone);
		mVerify = (EditText) findViewById(R.id.input_verify);
		mpassword = (EditText) findViewById(R.id.input_password);
		mconfirm_password = (EditText) findViewById(R.id.input_confirm_password);
		mget_verifyBtn = (Button) findViewById(R.id.get_verify_btn);
		mokBtn = (Button) findViewById(R.id.ok_btn);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(R.string.text_bar_getPassword));
		mget_verifyBtn.setOnClickListener(this);
		mokBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.get_verify_btn:
			if (StringUtils.isBlank(mPhone.getText().toString()))
			{
				ToastUtil.show(this, "手机号不能为空");
			}
			else if (!Pattern.matches(Constant.MOBILE_REGP, mPhone.getText().toString()))
			{
				ToastUtil.show(this, "请输入有效的手机号");
			}
			else
			{
//				openProgressDialog("获取中...");
				GetVerifyCode getVerifyCode = new GetVerifyCode();
				getVerifyCode.setToken(Encrypt.MD5(mPhone.getText().toString()+Constant.TokenSalt));
				getVerifyCode.setMobile(mPhone.getText().toString());
				getVerifyCode.setDeviceToken(SIMCardUtil.getDeviceId(this));
				getVerifyCode.setActionType(Constant.ACTION_TYPE.RESET_PASSWORD);
				NetUtil.post(Constant.BASE_URL, getVerifyCode, handler, HttpDefine.GET_VERIFY_CODE_RESP);
			}
			break;
		case R.id.ok_btn:
			if (StringUtils.isBlank(mVerify.getText().toString()))
			{
				ToastUtil.show(this, Constant.ToastMessage.EMPTY_VERIFY);
			}
			else if (StringUtils.isBlank(mpassword.getText().toString()))
			{
				ToastUtil.show(this, Constant.ToastMessage.EMPTY_PASSWORD);
			}
			else if (!Pattern.matches(Constant.PASSWORD_REGP, mpassword.getText().toString())||StringUtils.getStringLength(mpassword.getText().toString()) < 6||StringUtils.getStringLength(mpassword.getText().toString())>24)
			{
				ToastUtil.show(this, Constant.ToastMessage.PASSWORD_LACK_LENGTH);
			}
			else if (StringUtils.isBlank(mconfirm_password.getText().toString()))
			{
				ToastUtil.show(this, Constant.ToastMessage.CONFIRM_PASSWORD);
			}
			else if (!StringUtils.isEquals(mpassword.getText().toString(), mconfirm_password.getText().toString()))
			{
				ToastUtil.show(this, Constant.ToastMessage.NO_EQUAL_PASSWORD);
			}
			else
			{
				getPassword();
			}
			break;
		}
	}

	private void getPassword()
	{
		if (application.isNetworkAvailable())
		{
			try
			{
				isFinished=true;
				openProgressDialog("请稍候...");
				ResetPass resetPass = new ResetPass();
				resetPass.setCommunityID(PreferencesUtils.getCommunity().getID());
				resetPass.setMobile(mPhone.getText().toString());
				resetPass.setVerify(mVerify.getText().toString());
				resetPass.setToken(Encrypt.MD5(mPhone.getText().toString()+mVerify.getText().toString()+Encrypt.MD5(mpassword.getText().toString())+Constant.TokenSalt));
				resetPass.setPassword(Encrypt.MD5(mpassword.getText().toString()));
				NetUtil.post(Constant.BASE_URL, resetPass, handler, HttpDefine.RESET_PASS_RESP);
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		if (null == msg.obj)
		{
			closeProgressDialog();
			return true;
		}
		switch (msg.what)
		{
		case HttpDefine.GET_VERIFY_CODE_RESP:
			if (null != (String) msg.obj)
			{
				closeProgressDialog();
				GetVerifyCodeResp getVerifyCodeResp = JsonUtil.fromJson((String) msg.obj, GetVerifyCodeResp.class);
				if (null == getVerifyCodeResp)
				{
					break;
				}
				if (getVerifyCodeResp.getRet() == HttpDefine.RESPONSE_SUCCESS)
				{
					// 发送手机验证
					time.start();
					isFinished=false;
					break;
				}
				else
				{
					isFinished=true;
					time.cancel();
					ToastUtil.show(this, getVerifyCodeResp.getError());
				}
			}
			msg.obj = null;

			break;
		case HttpDefine.RESET_PASS_RESP:
			if (null != (String) msg.obj)
			{
				closeProgressDialog();
				ResetPassResp resetPassResp = JsonUtil.fromJson((String) msg.obj, ResetPassResp.class);
				if (null == resetPassResp)
				{
					break;
				}
				if (resetPassResp.getRet() == HttpDefine.RESPONSE_SUCCESS)
				{
					bar_leftBtn.setEnabled(false);
					PreferencesUtils.setUserName(mPhone.getText().toString());
					PreferencesUtils.setPassword(mpassword.getText().toString());
					ToastUtil.show(this, Constant.ToastMessage.RESETPASS_SUCCESS);
					new Handler().postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							AppManager.getAppManager().finishActivity();
						}
					}, 1000);
				}
				else
				{
					ToastUtil.show(this, resetPassResp.getError());
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
		{//计时完毕时触发
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

package com.e1858.wuye.activity;

import com.e1858.common.Constant;
import com.e1858.common.LoginManager;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登录
 * 
 * @author jiajia 2014年7月22日
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText mUserName;
	private EditText mPassword;
	private Button mLogin_btn;
	private Button mJump_register;
	private Button mForget_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
		initLoginData();
	}

	private void initLoginData() {
		if (!StringUtils.isEmpty(PreferencesUtils.getUserName())) {
			mUserName.setText(PreferencesUtils.getUserName());
			mPassword.setText(PreferencesUtils.getPassword());
		}
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(R.string.text_bar_login));
		mUserName = (EditText) findViewById(R.id.input_userName);
		mPassword = (EditText) findViewById(R.id.input_password);
		mLogin_btn = (Button) findViewById(R.id.login_btn);
		mJump_register = (Button) findViewById(R.id.jump_register);
		mForget_password = (Button) findViewById(R.id.forget_password);
		mLogin_btn.setOnClickListener(this);
		mJump_register.setOnClickListener(this);
		mForget_password.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.login_btn:
			if (StringUtils.isBlank(mUserName.getText().toString())) {
				ToastUtil.show(this, Constant.ToastMessage.EMPTY_USERNAME);
			} else if (StringUtils.isBlank(mPassword.getText().toString())) {
				ToastUtil.show(this, Constant.ToastMessage.EMPTY_PASSWORD);
			} else {
				login();
			}
			break;
		case R.id.jump_register:
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.forget_password:
			intent = new Intent(this, GetPasswordActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(GetPasswordActivity.USERNAME, mUserName.getText().toString());
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}

	private void login() {
		if (application.isNetworkAvailable()) {
			try {
				openProgressDialog("登录中...");
				PreferencesUtils.setUserName(mUserName.getText().toString());
				PreferencesUtils.setPassword(mPassword.getText().toString());
				LoginManager.login(application, handler, mUserName.getText().toString(), mPassword.getText().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ToastUtil.show(this, getResources()
					.getString(R.string.network_fail));
		}

	}
}

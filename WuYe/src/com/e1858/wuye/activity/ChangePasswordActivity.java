package com.e1858.wuye.activity;

import java.util.regex.Pattern;

import com.e1858.common.Constant;
import com.e1858.common.LoginManager;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.ChangePassword;
import com.e1858.wuye.protocol.http.ChangePasswordResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 修改密码
 * @author jiajia
 *
 */
public class ChangePasswordActivity extends BaseActivity implements
		OnClickListener {

	private TextView mobile;
	private EditText new_pass;
	private EditText confirm_new_pass;
	private EditText old_pass;
	private Button ok_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		initView();
	}

	private void initView() {
		mobile = (TextView) findViewById(R.id.mobile);
		new_pass = (EditText) findViewById(R.id.new_pass);
		confirm_new_pass = (EditText) findViewById(R.id.confirm_new_pass);
		old_pass = (EditText) findViewById(R.id.old_pass);
		ok_btn = (Button) findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(this);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(
				R.string.text_bar_changepassword));
		mobile.setText(PreferencesUtils.getUserName());
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			closeProgressDialog();
			return true;
		}
		switch (msg.what) {
		case HttpDefine.CHANGE_PASSWORD_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj) {
				ChangePasswordResp resp = JsonUtil.fromJson((String) msg.obj,
						ChangePasswordResp.class);
				if (null == resp) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					ToastUtil.show(this, "密码已修改");
					PreferencesUtils.setUserName(mobile.getText().toString());
					PreferencesUtils.setPassword(new_pass.getText().toString());
					LoginManager.login(application, handler, mobile.getText().toString(), new_pass.getText().toString());
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ok_btn:
			if (StringUtils.isBlank(mobile.getText().toString())) {
				ToastUtil.show(this, "用户还未登录");
			} else if (StringUtils.isBlank(old_pass.getText().toString())) {
				ToastUtil.show(this, "原密码不能为空");
			} else if (StringUtils.isEmpty(new_pass.getText().toString())) {
				ToastUtil.show(this, "新密码不能为空");
			} else if (!Pattern.matches(Constant.PASSWORD_REGP, new_pass
					.getText().toString())
					|| StringUtils.getStringLength(new_pass.getText()
							.toString()) < 6
					|| StringUtils.getStringLength(new_pass.getText()
							.toString()) > 24) {
				ToastUtil
						.show(this, Constant.ToastMessage.PASSWORD_LACK_LENGTH);
			} else if (StringUtils.isBlank(confirm_new_pass.getText()
					.toString())
					|| !StringUtils.isEquals(new_pass.getText().toString(),
							confirm_new_pass.getText().toString())) {
				ToastUtil.show(this, "请确认密码");
			} else {
				if (application.isNetworkAvailable()) {
					openProgressDialog("请稍候...");
					ChangePassword changePassword = new ChangePassword();
					changePassword.setCommunityID(PreferencesUtils.getCommunity()
							.getID());
					changePassword.setKey(PreferencesUtils.getLoginKey());
					changePassword.setMobile(mobile.getText().toString());
					changePassword.setToken(Encrypt.MD5(mobile.getText()
							.toString()
							+ Encrypt.MD5(old_pass.getText().toString())
							+ Encrypt.MD5(new_pass.getText().toString())
							+ PreferencesUtils.getLoginKey() + Constant.TokenSalt));
					changePassword.setOldPassword(Encrypt.MD5(old_pass
							.getText().toString()));
					changePassword.setNewPassword(Encrypt.MD5(new_pass
							.getText().toString()));
					NetUtil.post(Constant.BASE_URL, changePassword, handler,
							HttpDefine.CHANGE_PASSWORD_RESP);
				} else {
					ToastUtil.show(this,
							getResources().getString(R.string.network_fail));
				}

			}
			break;
		}
	}

}

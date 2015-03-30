package com.e1858.wuye.activity;

import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.e1858.common.AddSuccessDialogManager;
import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.AddComplaint;
import com.e1858.wuye.protocol.http.AddComplaintResp;
import com.e1858.wuye.protocol.http.HttpDefine;

/**
 * 新增投诉与建议
 * 
 * @author jiajia 2014年8月22日
 */
public class AddComplaintActivity extends BaseActivity implements OnClickListener {
	private EditText	input_complaint;
	private CheckBox	checkBox_anonymous;
	private Button		okBtn;

	private TextView	house_info;
	private EditText	phone_info;
	private EditText	name_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_complaint);
		initView();
	}

	private void initView() {
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		input_complaint = (EditText) findViewById(R.id.input_complaint);
		okBtn = (Button) findViewById(R.id.ok_btn);
		checkBox_anonymous = (CheckBox) findViewById(R.id.checkBox_anonymous);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(R.string.text_bar_add_complaint));
		okBtn.setOnClickListener(this);

		house_info = (TextView) findViewById(R.id.house_info);
		phone_info = (EditText) findViewById(R.id.input_phone);
		name_info = (EditText) findViewById(R.id.editText_name);
		name_info.setText(PreferencesUtils.getUserInfo().getName());
		phone_info.setText(PreferencesUtils.getUserInfo().getMobile());

		if (null != PreferencesUtils.getUserHouse()) {
			house_info.setText(Util.getHouseInfo(true));
		}

		house_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SwitchResidentAddressActivity.class);
				startActivityForResult(intent, Constant.HOUSE_INFO_RESULT_CODE);
			}
		});
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			closeProgressDialog();
			return true;
		}
		switch (msg.what) {
		case Constant.FAIL_CODE:
			closeProgressDialog();
			if (msg.obj != null) {
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case HttpDefine.ADD_COMPLAINT_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj) {
				AddComplaintResp resp = JsonUtil.fromJson((String) msg.obj, AddComplaintResp.class);
				if (null == resp) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					// 添加成功
					ToastUtil.show(this, "提交成功!");
					bar_leftBtn.setEnabled(false);
					//延迟一秒跳转
//					new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							AppManager.getAppManager().finishActivity();
//						}
//					}, 1000);
					application.setIsRefresh(true);
					AddSuccessDialogManager.createCallDialog(AddComplaintActivity.this,
							Constant.ADDSUCCESS_MODULE.COMPLAINT, resp.getID(), null);
				} else {
					okBtn.setEnabled(true);
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
			if (application.isNetworkAvailable()) {
				if (StringUtils.isBlank(house_info.getText().toString())) {
					ToastUtil.show(this, "请选择房屋信息");
				}
				if (StringUtils.isBlank(phone_info.getText().toString())) {
					ToastUtil.show(this, "请填写联系方式");
				} else if (!Pattern.matches(Constant.MOBILE_REGP, phone_info.getText().toString())) {
					ToastUtil.show(this, Constant.ToastMessage.MOBILE_REGP_ERROR);
				} else if (StringUtils.isBlank(input_complaint.getText().toString())) {
					ToastUtil.show(this, "投诉与建议内容不能为空");
				} else {
					openProgressDialog("新增提交中...");
					okBtn.setEnabled(false);
					// 匿名
					AddComplaint addComplaint = new AddComplaint();
					addComplaint.setAnonymous(checkBox_anonymous.isChecked());
					addComplaint.setCommunityID(PreferencesUtils.getCommunity().getID());
					addComplaint.setPhone(phone_info.getText().toString());
					addComplaint.setName(name_info.getText().toString());
//					addComplaint.setKey(application.getKey());
//					addComplaint.setToken(application.getToken());
					addComplaint.setKey(PreferencesUtils.getLoginKey());
					addComplaint.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
					addComplaint.setContent(input_complaint.getText().toString());
					NetUtil.post(Constant.BASE_URL, addComplaint, handler, HttpDefine.ADD_COMPLAINT_RESP);
				}
			} else {
				ToastUtil.show(this, getResources().getString(R.string.network_fail));
			}

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.HOUSE_INFO_RESULT_CODE:
			house_info.setText(Util.getHouseInfo(true));
			break;
		}
	}
}

package com.e1858.wuye.activity;

import com.e1858.common.Constant;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 更多
 * @author jiajia
 *
 */
public class MoreActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout switch_community;
	private RelativeLayout check_update;
	private RelativeLayout feedback;
	private RelativeLayout about;
	private RelativeLayout function_setting;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		handler = new Handler(this);
		initView();
	}

	private void initView() {
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		switch_community = (RelativeLayout) findViewById(R.id.switch_community);
		check_update = (RelativeLayout) findViewById(R.id.check_update);
		feedback = (RelativeLayout) findViewById(R.id.feedback);
		about = (RelativeLayout) findViewById(R.id.about);
		function_setting = (RelativeLayout) findViewById(R.id.setting);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(R.string.text_bar_more));
		// invite_friends=(RelativeLayout)findViewById(R.id.invite_friends);
		switch_community.setOnClickListener(this);
		check_update.setOnClickListener(this);
		feedback.setOnClickListener(this);
		about.setOnClickListener(this);
		// invite_friends.setOnClickListener(this);
		function_setting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.switch_community:
			intent = new Intent(this, LocationCommunityActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(Constant.PIC_REMARK, 1);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.check_update:
			// 版本升级
//			openProgressDialog("请稍候...");
//			CheckVersionDialogManager.checkVersion(this, handler);
			update();
			break;
		case R.id.feedback:
//			intent = new Intent(this, FeedBackActivity.class);
//			startActivity(intent);
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			break;
		case R.id.about:
			intent = new Intent(this, AboutUsActivity.class);
			startActivity(intent);
			break;
		/*
		 * case R.id.invite_friends: ToastUtil.show(this, "此功能暂未开放"); break;
		 */
		case R.id.setting:
			intent = new Intent(this, FunctionSetting.class);
			startActivity(intent);
			break;
		}

	}
	
	void update() {
		final ProgressDialog dialog = ProgressDialog.show(this, null, "正在检查更新");
		dialog.setCancelable(true);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new com.umeng.update.UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus, com.umeng.update.UpdateResponse updateInfo) {
				dialog.dismiss();
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(MoreActivity.this, updateInfo);
					break;
				case UpdateStatus.No: // has no update
					Toast.makeText(MoreActivity.this, "当前是最新版本", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.NoneWifi: // none wifi
					Toast.makeText(MoreActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Timeout: // time out
					Toast.makeText(MoreActivity.this, "检查版本超时", Toast.LENGTH_SHORT).show();
					break;
				}
			}

		});
		UmengUpdateAgent.forceUpdate(MoreActivity.this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			closeProgressDialog();
			return true;
		}
		switch (msg.what) {
		case HttpDefine.CHECK_VERSION_RESP:
//			closeProgressDialog();
//			if (null != (String) msg.obj) {
//				closeProgressDialog();
//				CheckVersionResp resp = JsonUtil.fromJson((String) msg.obj,
//						CheckVersionResp.class);
//				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
//					if (StringUtils.isEmpty(resp.getUrl())) {
//						ToastUtil.show(this, "已是最新版本!");
//					} else {
//						CheckVersionDialogManager.showDialog(this, handler,
//								resp.getVersionName(), resp.getEdescription(),
//								resp.getUrl());
//					}
//
//				} else {
//					ToastUtil.show(this, resp.getError());
//				}
//			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}
}

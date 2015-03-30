package com.e1858.wuye.activity;

import com.e1858.common.Constant;
import com.e1858.common.app.AppManager;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.EditNickname;
import com.e1858.wuye.protocol.http.EditNicknameResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 昵称
 * @author jiajia 2014年8月22日
 *
 */
public class NickNameActivity extends BaseActivity implements OnClickListener
{
	public static final String USERNAME = "USERNAME";
	private EditText nickname;
	private Button  okBtn;
	private String nickName_Str;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_nick_name);
		nickName_Str=getIntent().getExtras().getString(USERNAME);
		initView();
	}
	private void initView(){
		bar_leftBtn=(Button)findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title=(TextView)findViewById(R.id.bar_title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(R.string.text_bar_nickname));
		nickname=(EditText)findViewById(R.id.nickname);
		okBtn=(Button)findViewById(R.id.ok_btn);
		nickname.setText(nickName_Str);
		okBtn.setOnClickListener(this);
		bar_leftBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bar_left_btn:
			this.setResult(Constant.NICK_NAME_RESULT_CODE);
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.ok_btn:
			if(StringUtils.isBlank(nickname.getText().toString())){
				ToastUtil.show(this, "请输入昵称");
			}else{
				openProgressDialog("提交中...");
				EditNickname editNickname=new EditNickname();
//				editNickname.setKey(application.getKey());
//				editNickname.setToken(application.getToken());
				editNickname.setKey(PreferencesUtils.getLoginKey());
				editNickname.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				editNickname.setNickname(nickname.getText().toString());
				NetUtil.post(Constant.BASE_URL, editNickname, handler, HttpDefine.EDIT_NICKNAME_RESP);
			}
			break;
		}
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
		case HttpDefine.EDIT_NICKNAME_RESP:
			if(null!=(String)msg.obj){
				EditNicknameResp resp=JsonUtil.fromJson((String)msg.obj, EditNicknameResp.class);
				if(resp==null){
					break;
				}
				if(resp.getRet()==HttpDefine.RESPONSE_SUCCESS){
					Intent data=new Intent();
					Bundle bundle=new Bundle();
					bundle.putString(Constant.NICKNAME, nickname.getText().toString());
					data.putExtras(bundle);
					this.setResult(Constant.NICK_NAME_RESULT_CODE, data);
					application.setIsRefresh(true);
					AppManager.getAppManager().finishActivity();
				}else{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj=null;
			break;
		}
		return super.handleMessage(msg);
	}


}

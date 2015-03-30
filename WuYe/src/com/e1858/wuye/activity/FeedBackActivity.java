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
import com.e1858.wuye.protocol.http.AddFeedBack;
import com.e1858.wuye.protocol.http.AddFeedBackResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 意见反馈
 * @author jiajia 2014年8月22日
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener
{

	private EditText feedback;
	private Button okButton;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		initView();
	}
	private void initView(){
		bar_leftBtn=(Button)findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title=(TextView)findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(R.string.text_bar_feed_back));
		feedback=(EditText)findViewById(R.id.feedback);
		okButton=(Button)findViewById(R.id.ok_btn);
		okButton.setOnClickListener(this);
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
		case HttpDefine.ADD_FEEDBACK_RESP:
			closeProgressDialog();
			if(null!=(String)msg.obj){
				AddFeedBackResp resp=JsonUtil.fromJson((String)msg.obj, AddFeedBackResp.class);
				if(null==resp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
					ToastUtil.show(this, "反馈成功!");
//					feedback.setText("");
					AppManager.getAppManager().finishActivity(this);
				}else{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj=null;
			break;
		}
		return super.handleMessage(msg);
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ok_btn:
			if(StringUtils.isBlank(feedback.getText().toString())){
				ToastUtil.show(this, "意见或建议不能为空");
			}else{
				openProgressDialog("提交中...");
				//提交
				AddFeedBack addFeedBack=new AddFeedBack();
				addFeedBack.setCommunityID(PreferencesUtils.getCommunity().getID());
				if(!StringUtils.isEmpty(PreferencesUtils.getLoginKey())){
					addFeedBack.setKey(PreferencesUtils.getLoginKey());
					addFeedBack.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				}else{
					addFeedBack.setToken(Encrypt.MD5(Constant.TokenSalt));
				}
				addFeedBack.setContent(feedback.getText().toString());
				NetUtil.post(Constant.BASE_URL, addFeedBack, handler, HttpDefine.ADD_FEEDBACK_RESP);
			}
			break;
		}
	}

}

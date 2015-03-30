package com.e1858.wuye.activity;

import java.util.LinkedList;

import com.e1858.common.CallDialgoManager;
import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DateUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.GetMaintenanceById;
import com.e1858.wuye.protocol.http.GetMaintenanceByIdResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.Maintenance;
import com.hg.android.widget.ImagesGridView;
import com.hg.android.widget.MyGridView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 报修详细
 * @author jiajia 2014年8月22日
 *
 */
public class MaintenanceDetailActivity extends BaseActivity implements OnClickListener
{
	private TextView mAddress;
	private TextView mPhone;
	private TextView mType;
	private TextView mCreateTime;
	private TextView mServiceTime;
	private TextView mStatus;
	private TextView mhandlerName;
	private TextView mhandlerPhone;
	private TextView mhandlerTime;
	private LinearLayout mexit_pic_lin;
	private TextView maintenance_description;
	private ImagesGridView mGridView;
	private int maintenance;
	private Button okBtn;
	private Maintenance maintenanceObject;
//	private LinkedList<String> pics = new LinkedList<String>();
	private LinearLayout reply_lin;
	private LinearLayout handlerName_lin;
	private LinearLayout handlerPhone_lin;
	private LinearLayout handlerTime_lin;
	private TextView maintenance_reply;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintenance_detail);
		maintenance = getIntent().getExtras().getInt(Constant.DETAIL_ID);
		initView();
		loadData();
	}

	private void initView()
	{
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(R.string.text_bar_maintenance_detail));
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		mAddress = (TextView) findViewById(R.id.maintenance_address);
		mPhone = (TextView) findViewById(R.id.maintenance_phone);
		mType = (TextView) findViewById(R.id.maintenance_type);
		mCreateTime = (TextView) findViewById(R.id.maintenance_createtime);
		mServiceTime = (TextView) findViewById(R.id.maintenance_servicetime);
		mhandlerName=(TextView)findViewById(R.id.maintenance_handlerName);
		mhandlerPhone=(TextView)findViewById(R.id.maintenance_handlerPhone);
		mhandlerTime=(TextView)findViewById(R.id.maintenance_handlerTime);
		maintenance_description=(TextView)findViewById(R.id.maintenance_description);
		mStatus = (TextView) findViewById(R.id.maintenance_status);
		handlerName_lin=(LinearLayout)findViewById(R.id.handlerName_lin);
		handlerPhone_lin=(LinearLayout)findViewById(R.id.handlerPhone_lin);
		handlerTime_lin=(LinearLayout)findViewById(R.id.handlerTime_lin);
		mexit_pic_lin = (LinearLayout) findViewById(R.id.exit_pic_lin);
		mGridView = (ImagesGridView) findViewById(R.id.gridview);
		reply_lin=(LinearLayout)findViewById(R.id.reply_lin);
		maintenance_reply=(TextView)findViewById(R.id.maintenance_reply);
		okBtn = (Button) findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(this);
		mhandlerPhone.setOnClickListener(this);
		okBtn.setVisibility(View.GONE);
	}

	private void loadData()
	{
		if (application.isNetworkAvailable())
		{
			openProgressDialog("加载中...");
			GetMaintenanceById getMaintenanceById = new GetMaintenanceById();
			getMaintenanceById.setCommunityID(PreferencesUtils.getCommunity().getID());
			getMaintenanceById.setKey(PreferencesUtils.getLoginKey());
			getMaintenanceById.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			getMaintenanceById.setID(maintenance);
			NetUtil.post(Constant.BASE_URL, getMaintenanceById, handler, HttpDefine.GET_MAINTENANCE_BY_ID_RESP);
		}
		else
		{
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
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
		case HttpDefine.GET_MAINTENANCE_BY_ID_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj)
			{
				GetMaintenanceByIdResp resp = JsonUtil.fromJson((String) msg.obj, GetMaintenanceByIdResp.class);
				if (resp == null)
				{
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet())
				{
					maintenanceObject=resp.getMaintenance();
					initData(resp.getMaintenance());
				}
				else
				{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	private void initData(Maintenance maintenance)
	{
		// TODO Auto-generated method stub
		mAddress.setText(maintenance.getHouseRoom());
		mPhone.setText(maintenance.getPhone());
		if(null!=maintenance.getType()){
			mType.setText(maintenance.getType().getName());
		}else{
			mType.setText("无");
		}
		
		maintenance_description.setText(maintenance.getContent());
		mCreateTime.setText(DateUtil.dateToZh(maintenance.getCreateTime()));
		mServiceTime.setText(maintenance.getServiceTime());
		if(maintenance.getState()==0){
			mStatus.setText("未受理");
		}else if(maintenance.getState()==1){
			if(!StringUtils.isEmpty(maintenance.getReply())){
//				String content="已受理"+"<br /><font color='#ff6600' size='10pt'>"+maintenance.getReply()+"</font>";
//				mStatus.setText(Html.fromHtml(content));
				reply_lin.setVisibility(View.VISIBLE);
				mStatus.setText("已受理");
				maintenance_reply.setText(maintenance.getReply());
			}else{
				mStatus.setText("已受理");
			}
		}else if(maintenance.getState()==2){
			if(!StringUtils.isEmpty(maintenance.getReply())){
				reply_lin.setVisibility(View.VISIBLE);
				mStatus.setText("已完成");
				maintenance_reply.setText(maintenance.getReply());
			}else{
				mStatus.setText("已完成");
			}
		}
		if(!StringUtils.isEmpty(maintenance.getHandlerName())){
			handlerName_lin.setVisibility(View.VISIBLE);
			mhandlerName.setText(maintenance.getHandlerName());
		}
		if(!StringUtils.isEmpty(maintenance.getHandlerPhone())){
			handlerPhone_lin.setVisibility(View.VISIBLE);
			mhandlerPhone.setText(maintenance.getHandlerPhone());
		}
		if(!StringUtils.isEmpty(maintenance.getHandleTime())){
			handlerTime_lin.setVisibility(View.VISIBLE);
			mhandlerTime.setText(DateUtil.dateToZh(maintenance.getHandleTime()));
		}
		if (null != maintenance.getImages() && maintenance.getImages().size() > 0)
		{
			mexit_pic_lin.setVisibility(View.VISIBLE);
			mGridView.setImageUrls(maintenance.getImages());
		}
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.ok_btn:
			break;
		case R.id.maintenance_handlerPhone:
			if(!StringUtils.isEmpty(mhandlerPhone.getText().toString())){
				CallDialgoManager.createCallDialog(MaintenanceDetailActivity.this, mhandlerPhone.getText().toString());
			}
			break;
		}
		

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case Constant.SEE_PIC_RESULT_CODE:
			break;
		}
	}
}

package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.List;

import com.e1858.common.CallDialgoManager;
import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.Convenient;
import com.e1858.wuye.protocol.http.GetConvenientById;
import com.e1858.wuye.protocol.http.GetConvenientByIdResp;
import com.e1858.wuye.protocol.http.HttpDefine;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 便民详情
 * @author jiajia
 *
 */
public class ConvenienceDetailActivity extends BaseActivity
{
	private TextView title;
	private LinearLayout exit_pic_lin;
	private ImagesGridView myGridView;
	private TextView item_address;
	private TextView item_phone;
	private TextView item_business_hours;
	private TextView item_content;
//	private List<String> pics = new ArrayList<String>();
	private int convenienceId;
	private String titleStr;
	private Convenient convenient;
	private RelativeLayout call_relative;
	private RelativeLayout map_location;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convenience_detail);
		convenienceId=getIntent().getExtras().getInt(Constant.DETAIL_ID);
		titleStr=getIntent().getExtras().getString(Constant.DETAIL_TITLE);
		initView();
		loadData(convenienceId);
	}
	private void initView(){
		bar_leftBtn=(Button)findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title=(TextView)findViewById(R.id.bar_title);
		bar_title.setText(titleStr+"详情");
		title=(TextView)findViewById(R.id.item_title);
		exit_pic_lin=(LinearLayout)findViewById(R.id.exit_pic_lin);
		myGridView=(ImagesGridView)findViewById(R.id.gridview);
		myGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		item_address=(TextView)findViewById(R.id.item_address);
		item_phone=(TextView)findViewById(R.id.item_phone);
		item_business_hours=(TextView)findViewById(R.id.item_business_hours);
		call_relative=(RelativeLayout)findViewById(R.id.call_relative);
		map_location=(RelativeLayout)findViewById(R.id.map_location);
		item_content=(TextView)findViewById(R.id.item_content);
		call_relative.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				CallDialgoManager.createCallDialog(ConvenienceDetailActivity.this, item_phone.getText().toString().trim());
			}
		});
		map_location.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				//位置定位
			}
		});
	}
	private void loadData(long id){
		if(application.isNetworkAvailable()){
			GetConvenientById getConvenientById=new GetConvenientById();
			getConvenientById.setCommunityID(PreferencesUtils.getCommunity().getID());
			getConvenientById.setToken(Encrypt.MD5(Constant.TokenSalt));
			getConvenientById.setID(convenienceId);
			NetUtil.post(Constant.BASE_URL, getConvenientById, handler, HttpDefine.GET_CONVENIENT_BY_ID_RESP);
		}else{
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}
	private void initData(Convenient convenient){
		title.setText(convenient.getName());
		List<String> images = convenient.getImages();
		if(images != null && images.size() > 0){
			exit_pic_lin.setVisibility(View.VISIBLE);
			if (images.size() > 4) {
				images = images.subList(0, 4);
			}
			myGridView.setImageUrls(images);
		}
		item_address.setText(convenient.getAddress());
		item_phone.setText(convenient.getPhone());
		item_business_hours.setText(convenient.getServiceTime());
		item_content.setText(convenient.getEdescription());
	}
	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		if(null==msg.obj){
			return true;
		}
		switch(msg.what){
		case HttpDefine.GET_CONVENIENT_BY_ID_RESP:
			if(null!=(String)msg.obj){
				GetConvenientByIdResp resp=JsonUtil.fromJson((String)msg.obj, GetConvenientByIdResp.class);
				if(null==resp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
					convenient=resp.getConvenience();
					initData(convenient);
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

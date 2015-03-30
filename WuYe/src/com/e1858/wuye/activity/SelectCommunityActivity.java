package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.common.LoginManager;
import com.e1858.common.app.AppManager;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.tabfragment.MainTabActivity;
import com.e1858.wuye.adapter.CommunityAdapter;
import com.e1858.wuye.protocol.http.ChangeCommunity;
import com.e1858.wuye.protocol.http.ChangeCommunityResp;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.GetCommunityByArea;
import com.e1858.wuye.protocol.http.GetCommunityByAreaResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.igexin.sdk.PushManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 选择小区
 * @author jiajia
 *
 */
public class SelectCommunityActivity extends BaseActivity
{
	private TextView about;
	private ListView mDownListView;
	private List<Community> lists = new ArrayList<Community>();
	private CommunityAdapter adapter;
	private String currentCity="";
	private int remark=-1;//-1表示进首页面   1表示可返回
	private Community community=new Community();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_community);
		currentCity=getIntent().getExtras().getString("CurrentCity");
		remark=getIntent().getExtras().getInt(Constant.PIC_REMARK);
		adapter=new CommunityAdapter(this, lists);
		initView();
		loadData();
	}

	private void loadData()
	{
		// TODO Auto-generated method stub
		if (application.isNetworkAvailable())
		{
			openProgressDialog("加载中...");
			GetCommunityByArea getCommunityByArea = new GetCommunityByArea();
			getCommunityByArea.setCity(currentCity);
			getCommunityByArea.setToken(Encrypt.MD5(Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getCommunityByArea, handler, HttpDefine.GET_COMMUNITY_BY_AREA_RESP);
		}
		else
		{
		
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}

	}

	private void initView()
	{
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(R.string.text_bar_switchCommunity));
		mDownListView = (ListView) findViewById(R.id.listview);
		mDownListView.setAdapter(adapter);
		about = (TextView) findViewById(R.id.about);
		mDownListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				community = (Community) mDownListView.getAdapter().getItem(position);
				if(remark==1&&!StringUtils.isEmpty(PreferencesUtils.getLoginKey())){
					ChangeCommunity changeCommunity=new ChangeCommunity();
//					changeCommunity.setKey(application.getKey());
//					changeCommunity.setToken(application.getToken());
					changeCommunity.setKey(PreferencesUtils.getLoginKey());
					changeCommunity.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					changeCommunity.setCommunityID(community.getID());
					NetUtil.post(Constant.BASE_URL, changeCommunity, handler, HttpDefine.CHANGE_COMMUNITY_RESP);
				}else{
					ChangeCommunity changeCommunity=new ChangeCommunity();
					changeCommunity.setToken(Encrypt.MD5(Constant.TokenSalt));
					changeCommunity.setCommunityID(community.getID());
					NetUtil.post(Constant.BASE_URL, changeCommunity,handler,HttpDefine.CHANGE_COMMUNITY_RESP);
				}
			}
		});
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
		case HttpDefine.GET_COMMUNITY_BY_AREA_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj)
			{
				GetCommunityByAreaResp resp = JsonUtil.fromJson((String) msg.obj, GetCommunityByAreaResp.class);
				if (null == resp)
				{
					mDownListView.setVisibility(View.GONE);
					about.setVisibility(View.VISIBLE);
					about.setText("很抱歉,你所选择的城市暂没有相关小区");
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet())
				{
					if (resp.getCommunities().size() == 0)
					{
						mDownListView.setVisibility(View.GONE);
						about.setVisibility(View.VISIBLE);
						about.setText("很抱歉,你所选择的城市暂没有相关小区");
					}
					else
					{
						lists.addAll(resp.getCommunities());
						adapter.notifyDataSetChanged();
						
					}
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.CHANGE_COMMUNITY_RESP:
			if (null != (String) msg.obj)
			{
				ChangeCommunityResp changeCommunityResp = JsonUtil.fromJson((String) msg.obj, ChangeCommunityResp.class);
				if (null == changeCommunityResp)
				{
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == changeCommunityResp.getRet())
				{
					if(remark==1){
						Intent data = new Intent();
						data.putExtra(Constant.HOUSE_DATA, changeCommunityResp.getCommunity());
						SelectCommunityActivity.this.setResult(Constant.SELECT_COMMUNITY_RESULT_CODE,data);
//						AppManager.getAppManager().finishActivity();
//						AppManager.getAppManager().finishActivity();
//						AppManager.getAppManager().finishActivity();
						PreferencesUtils.setUserHouse(changeCommunityResp.getUserHouse());
						application.setIsCommunityChanged(true);
						application.setIsConvienceChanged(true);
						Intent intent=new Intent(this,MainTabActivity.class);
						startActivity(intent);
						AppManager.getAppManager().finishActivity();
					}else{
						Intent intent=new Intent(SelectCommunityActivity.this,MainTabActivity.class);
						SelectCommunityActivity.this.startActivity(intent);
						AppManager.getAppManager().finishActivity();
					}
					PreferencesUtils.setCommunity(changeCommunityResp.getCommunity());
					int userID = PreferencesUtils.getUserID();
					if(userID>0){
						LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), PushManager.getInstance().getClientid(getApplicationContext()), userID, PreferencesUtils.getLoginKey(), Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					}else{
						LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), PushManager.getInstance().getClientid(getApplicationContext()), userID, "", Encrypt.MD5(Constant.TokenSalt));
					}
					
				}
				else
				{
					ToastUtil.show(this, changeCommunityResp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}
}

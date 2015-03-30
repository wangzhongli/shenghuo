package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.PullToRefreshListView;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.NeighborhoodListViewAdapter;
import com.e1858.wuye.protocol.http.BbsBoard;
import com.e1858.wuye.protocol.http.GetBbsBoards;
import com.e1858.wuye.protocol.http.GetBbsBoardsResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 邻里互动
 * @author jiajia 2014年8月22日
 *
 */
public class NeighborhoodActivity extends BaseActivity
{

	private PullToRefreshListView listView;
	private List<BbsBoard> bbsBoards = new ArrayList<BbsBoard>();
	private String object_key;
	private boolean isPull=false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neighborhood_interactions);
		object_key=NeighborhoodActivity.class.getSimpleName()+"_"+PreferencesUtils.getUserName();
		bar_leftBtn=(Button)findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title=(TextView)findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(R.string.text_bar_interact));
		listView = (PullToRefreshListView)findViewById(R.id.listview);
		listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
				if (application.isNetworkAvailable()) {
					isPull=true;
					loadData(false);
				} else {
					ToastUtil.show(getApplicationContext(), getResources()
							.getString(R.string.network_fail));
				}
            }
        });		
		loadData(true);
	}

	private void loadData(boolean localCache)
	{
		if(localCache){

			GetBbsBoardsResp resp=(GetBbsBoardsResp)DataFileUtils.readObject(object_key);
			if(null!=resp){
				bbsBoards.addAll(resp.getBbsBoards());
				Message msg = handler.obtainMessage(HttpDefine.RESPONSE_SUCCESS, bbsBoards);
				msg.arg1 = 1;
				msg.sendToTarget();
			}
		
		}
		if (application.isNetworkAvailable())
		{
			GetBbsBoards getBbsBoards = new GetBbsBoards();
//			getBbsBoards.setKey(application.getKey());
//			getBbsBoards.setToken(application.getToken());
			getBbsBoards.setKey(PreferencesUtils.getLoginKey());
			getBbsBoards.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			getBbsBoards.setCommunityID(PreferencesUtils.getCommunity().getID());
			NetUtil.post(Constant.BASE_URL, getBbsBoards, handler, HttpDefine.GET_BBS_BOARDS_RESP);
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
			listView.onRefreshComplete();
			return true;
		}
		switch (msg.what)
		{
		case HttpDefine.RESPONSE_SUCCESS:
			if(msg.arg1==1){
				listView.setAdapter(new NeighborhoodListViewAdapter(this, bbsBoards));
			}
			break;
		case HttpDefine.GET_BBS_BOARDS_RESP:
			if (null != (String) msg.obj)
			{
				GetBbsBoardsResp resp = JsonUtil.fromJson((String) msg.obj, GetBbsBoardsResp.class);
				if (resp == null)
				{
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet())
				{
					if(resp.getBbsBoards()==null||resp.getBbsBoards().size()==0){
						if(isPull){
							bbsBoards.clear();
							bbsBoards.addAll(resp.getBbsBoards());
							DataFileUtils.saveObject(resp, object_key);
							listView.setAdapter(new NeighborhoodListViewAdapter(this, bbsBoards));
							listView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						}else{
							listView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						}
						break;
					}
					bbsBoards.clear();
					bbsBoards.addAll(resp.getBbsBoards());
					DataFileUtils.saveObject(resp, object_key);
					listView.setAdapter(new NeighborhoodListViewAdapter(this, bbsBoards));
					listView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
				}
				else
				{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj=null;
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadData(false);
	}
	
}

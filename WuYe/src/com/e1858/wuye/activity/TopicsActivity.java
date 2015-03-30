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
import com.e1858.wuye.adapter.BbsTopicListViewAdapter;
import com.e1858.wuye.protocol.http.BbsTopic;
import com.e1858.wuye.protocol.http.GetBbsTopics;
import com.e1858.wuye.protocol.http.GetBbsTopicsByCreator;
import com.e1858.wuye.protocol.http.GetBbsTopicsByCreatorResp;
import com.e1858.wuye.protocol.http.GetBbsTopicsResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
/**
 * 话题
 * @author jiajia
 *
 */
public class TopicsActivity extends BaseActivity implements OnClickListener
{

	private PullToRefreshListView mDownListView;
	private TextView toast_empty;
	private int detail_ID;
	private String detail_Title;
	private int direction = 1;
	private List<BbsTopic> topics = new ArrayList<BbsTopic>();
	private BbsTopicListViewAdapter adapter = null;
	public static List<String> pics = new ArrayList<String>();
	private int remark;
	private String object_key;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic);
		remark = getIntent().getExtras().getInt(Constant.PIC_REMARK);
		if (remark == 1)
		{
			detail_ID = getIntent().getExtras().getInt(Constant.DETAIL_ID);
			detail_Title = getIntent().getExtras().getString(Constant.DETAIL_TITLE);
		}
		else
		{
			detail_ID = -1;
			detail_Title = "我的话题";
		}
		object_key = TopicsActivity.class.getSimpleName() + "_" + PreferencesUtils.getUserName() + "_" + remark + "_" + detail_ID+PreferencesUtils.getCommunity().getID();
		initView();
		loadData(direction, 0, true);
		footer_more.setVisibility(View.GONE);
	}

	private void initView()
	{
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_rightBtn = (Button) findViewById(R.id.bar_right_btn);
		bar_rightBtn.setBackgroundResource(R.drawable.add_new_icon_background);
		bar_rightBtn.setVisibility(View.VISIBLE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText(detail_Title);
		mDownListView = (PullToRefreshListView) findViewById(R.id.listview);
		toast_empty=(TextView)findViewById(R.id.toast_empty);
		bar_rightBtn.setOnClickListener(this);
		footer_view=getLayoutInflater().inflate(R.layout.listview_footer, null);
		footer_bar=(ProgressBar)footer_view.findViewById(R.id.listview_foot_progress);
		footer_more=(TextView)footer_view.findViewById(R.id.listview_foot_more);	
		
		mDownListView.addFooterView(footer_view);
		
		mDownListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mDownListView.onScrollStateChanged(view, scrollState);
				//数据为空--不用继续下面代码了
				if(topics.isEmpty()) return;	
				//判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if(view.getPositionForView(footer_view) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				if(scrollEnd)
				{
					if(view.getCount()>=Constant.PAGE_SIZE){
						footer_more.setText(R.string.pull_to_refresh_refreshing_label);
						footer_bar.setVisibility(View.VISIBLE);
						direction = Constant.DIRECTION.BACKWARD;
						loadData(direction, topics.size(), false);
					}else{
						footer_bar.setVisibility(View.GONE);
						footer_more.setText(getResources().getString(R.string.loading_full));
					}
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				mDownListView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
		mDownListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
				if (application.isNetworkAvailable()) {
					direction = Constant.DIRECTION.FORWARD;
					loadData(direction, 0, false);
				} else {
					ToastUtil.show(getApplicationContext(), getResources()
							.getString(R.string.network_fail));
				}
            }
        });		
		
	}

	private void loadData(int direction, int offset, boolean localCache)
	{
		try
		{
			if (localCache)
			{
				if (remark == 1)
				{
					GetBbsTopicsResp resp = (GetBbsTopicsResp) DataFileUtils.readObject(object_key);
					if (null != resp)
					{
						topics.addAll(resp.getBbsTopics());
						Message msg = handler.obtainMessage(HttpDefine.RESPONSE_SUCCESS, topics);
						msg.arg1 = 1;
						msg.sendToTarget();
					}
				}
				else
				{
					GetBbsTopicsByCreatorResp resp = (GetBbsTopicsByCreatorResp) DataFileUtils.readObject(object_key);
					if (null != resp)
					{
						topics.addAll(resp.getBbsTopics());
						Message msg = handler.obtainMessage(HttpDefine.RESPONSE_SUCCESS, topics);
						msg.arg1 = 1;
						msg.sendToTarget();
					}
				}

			}
			if (application.isNetworkAvailable())
			{
				if (remark == 1)
				{
					GetBbsTopics getBbsTopics = new GetBbsTopics();
					getBbsTopics.setCommunityID(PreferencesUtils.getCommunity().getID());
//					getBbsTopics.setKey(application.getKey());
//					getBbsTopics.setToken(application.getToken());
					getBbsTopics.setKey(PreferencesUtils.getLoginKey());
					getBbsTopics.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					getBbsTopics.setBoardID(detail_ID);
					getBbsTopics.setCount(direction);
					getBbsTopics.setOffset(offset);
					getBbsTopics.setCount(Constant.PAGE_SIZE);
					NetUtil.post(Constant.BASE_URL, getBbsTopics, handler, HttpDefine.GET_BBS_TOPICS_RESP);
				}
				else
				{

					GetBbsTopicsByCreator getBbsTopicsByCreator = new GetBbsTopicsByCreator();
					getBbsTopicsByCreator.setCommunityID(PreferencesUtils.getCommunity().getID());
//					getBbsTopicsByCreator.setKey(application.getKey());
//					getBbsTopicsByCreator.setToken(application.getToken());
					getBbsTopicsByCreator.setKey(PreferencesUtils.getLoginKey());
					getBbsTopicsByCreator.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					getBbsTopicsByCreator.setCreatorID(PreferencesUtils.getUserID());
					getBbsTopicsByCreator.setCount(direction);
					getBbsTopicsByCreator.setOffset(offset);
					getBbsTopicsByCreator.setCount(Constant.PAGE_SIZE);
					NetUtil.post(Constant.BASE_URL, getBbsTopicsByCreator, handler, HttpDefine.GET_BBS_POSTS_BY_CREATOR_RESP);
				}

			}
			else
			{
				ToastUtil.show(this, getResources().getString(R.string.network_fail));
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		if (null == msg.obj)
		{
			footer_bar.setVisibility(View.GONE);
			footer_more.setVisibility(View.GONE);
			mDownListView.onRefreshComplete();
			return true;
		}
		switch (msg.what)
		{
		case Constant.FAIL_CODE:
			if (msg.obj != null) {
				footer_bar.setVisibility(View.GONE);
				footer_more.setVisibility(View.GONE);
				mDownListView.onRefreshComplete();
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case HttpDefine.RESPONSE_SUCCESS:
			if (msg.arg1 == 1)
			{
				if(!application.isNetworkAvailable()&&topics.size()<Constant.PAGE_SIZE){
					mDownListView.removeFooterView(footer_view);
				}
				if(topics.size()==0||topics==null){
					toast_empty.setVisibility(View.VISIBLE);
					mDownListView.setVisibility(View.GONE);
				}else{
					initList(topics);	
				}
			}
			break;
		case HttpDefine.GET_BBS_TOPICS_RESP:
			if (null != (String) msg.obj)
			{
				GetBbsTopicsResp resp = JsonUtil.fromJson((String) msg.obj, GetBbsTopicsResp.class);
				if (null == resp)
				{
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS)
				{
					if((resp.getBbsTopics()==null||resp.getBbsTopics().size()==0)&&direction==Constant.DIRECTION.BACKWARD){
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						footer_more.setText(getResources().getString(R.string.loading_full));
						break;
					}
					if((resp.getBbsTopics()==null||resp.getBbsTopics().size()==0)&&direction==Constant.DIRECTION.FORWARD){
//						topics.clear();
//						initList(topics);
						footer_bar.setVisibility(View.GONE);
//						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						toast_empty.setVisibility(View.VISIBLE);
						mDownListView.setVisibility(View.GONE);
						break;
					}
					if (direction == Constant.DIRECTION.FORWARD)
					{
						// 下拉刷新
						mDownListView.setVisibility(View.VISIBLE);
						toast_empty.setVisibility(View.GONE);
						List<BbsTopic> forward_list = resp.getBbsTopics();
						topics.clear();
						topics.addAll(forward_list);
						DataFileUtils.saveObject(resp, object_key);
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						if(forward_list.size()<Constant.PAGE_SIZE){
							mDownListView.removeFooterView(footer_view);
						}
						initList(topics);
					}
					else
					{
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						if(resp.getBbsTopics().size()<Constant.PAGE_SIZE){
							footer_more.setText(getResources().getString(R.string.loading_full));
						}else{
							footer_more.setText(getResources().getString(R.string.footer_more));
						}
						mDownListView.setSelection(mDownListView.getCount()-resp.getBbsTopics().size()+1);
						topics.addAll(resp.getBbsTopics());
						adapter.notifyDataSetChanged();
					}
				}
				else
				{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.GET_BBS_POSTS_BY_CREATOR_RESP:
			if (null != (String) msg.obj)
			{
				GetBbsTopicsByCreatorResp resp = JsonUtil.fromJson((String) msg.obj, GetBbsTopicsByCreatorResp.class);
				if (null == resp)
				{
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS)
				{

					if((resp.getBbsTopics()==null||resp.getBbsTopics().size()==0)&&direction==Constant.DIRECTION.BACKWARD){
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						footer_more.setText(getResources().getString(R.string.loading_full));
						break;
					}
					if((resp.getBbsTopics()==null||resp.getBbsTopics().size()==0)&&direction==Constant.DIRECTION.FORWARD){
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						break;
					}

					if (direction == Constant.DIRECTION.FORWARD)
					{
						// 下拉刷新
						List<BbsTopic> forward_list = resp.getBbsTopics();
						topics.clear();
						topics.addAll(forward_list);
						DataFileUtils.saveObject(resp, object_key);
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						if(forward_list.size()<Constant.PAGE_SIZE){
							mDownListView.removeFooterView(footer_view);
						}
						initList(topics);
					}
					else
					{
						// 底部加载更多
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						if(resp.getBbsTopics().size()<Constant.PAGE_SIZE){
							footer_more.setText(getResources().getString(R.string.loading_full));
						}else{
							footer_more.setText(getResources().getString(R.string.footer_more));
						}
						mDownListView.setSelection(mDownListView.getCount()-resp.getBbsTopics().size()+1);
						topics.addAll(resp.getBbsTopics());
						adapter.notifyDataSetChanged();

					}

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

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.bar_right_btn:
			if (null == PreferencesUtils.getUserHouse())
			{
				ToastUtil.show(this, "请先选择房屋信息");
				new Handler().postDelayed(new Runnable()
				{

					@Override
					public void run()
					{
						Intent intent = new Intent(TopicsActivity.this, SwitchResidentAddressActivity.class);
						startActivityForResult(intent, Constant.HOUSE_INFO_RESULT_CODE);
					}
				}, 1000);

			}
			else
			{
				Intent intent = new Intent(this, AddTopicActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(Constant.DETAIL_ID, detail_ID);
				intent.putExtras(bundle);
				startActivity(intent);

			}
			break;
		}

	}

	private void initList(List<BbsTopic> topics)
	{
		adapter = new BbsTopicListViewAdapter(application, TopicsActivity.this, topics, detail_ID,imageLoader,options);
		mDownListView.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case Constant.HOUSE_INFO_RESULT_CODE:
			if (PreferencesUtils.getUserHouse() == null)
			{
				ToastUtil.show(this, "对不起,您还未选择房屋信息");
			}
			else
			{
				Intent intent = new Intent(this, AddTopicActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(Constant.DETAIL_ID, detail_ID);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;
		case Constant.SEE_PIC_RESULT_CODE:
			break;

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(application.getIsRefresh()){
			direction = Constant.DIRECTION.FORWARD;
			loadData(direction, 0, true);
			application.setIsRefresh(false);
		}
	}
}

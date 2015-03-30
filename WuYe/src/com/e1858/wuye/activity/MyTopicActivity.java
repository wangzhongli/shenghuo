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
import com.e1858.wuye.adapter.MyTopicAdapter;
import com.e1858.wuye.protocol.http.BbsTopic;
import com.e1858.wuye.protocol.http.GetBbsTopicsByCreator;
import com.e1858.wuye.protocol.http.GetBbsTopicsByCreatorResp;
import com.e1858.wuye.protocol.http.HttpDefine;

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
/**
 * 我的话题
 * @author jiajia
 *
 */
public class MyTopicActivity extends BaseActivity implements OnClickListener{
	
	public static final  String USERID = "USERID";

	private PullToRefreshListView   mDownListView;
	private List<BbsTopic> topics = new ArrayList<BbsTopic>();
	private MyTopicAdapter adapter = null;
	public static List<String> pics = new ArrayList<String>();
	private String object_key;
	private int userID;
	private int direction = 1;
	private int topicNum=0;
	private int topicComment=0;
	private int detail_ID=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_topic);
		userID=getIntent().getExtras().getInt(USERID);
		object_key = TopicsActivity.class.getSimpleName() + "_" + PreferencesUtils.getUserName() +"_"+PreferencesUtils.getCommunity().getID()+userID;
		initView();
		loadData(direction, 0, true);
		footer_more.setVisibility(View.GONE);
	}
	private void initView(){
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_rightBtn = (Button) findViewById(R.id.bar_right_btn);
		bar_rightBtn.setBackgroundResource(R.drawable.add_new_icon_background);
		bar_rightBtn.setVisibility(View.VISIBLE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText("我的话题");
		bar_rightBtn.setOnClickListener(this);
		mDownListView = (PullToRefreshListView) findViewById(R.id.listview);
		footer_view=getLayoutInflater().inflate(R.layout.listview_footer, null);
		footer_bar=(ProgressBar)footer_view.findViewById(R.id.listview_foot_progress);
		footer_more=(TextView)footer_view.findViewById(R.id.listview_foot_more);	
		
		mDownListView.addFooterView(footer_view);
		adapter= new MyTopicAdapter(application,this, topics,topicNum,topicComment);
		mDownListView.setAdapter(adapter);
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
					GetBbsTopicsByCreatorResp resp = (GetBbsTopicsByCreatorResp) DataFileUtils.readObject(object_key);
					if (null != resp)
					{
						topics.addAll(resp.getBbsTopics());
						topicNum=resp.getTopicCount();
						topicComment=resp.getCommentCount();
						Message msg = handler.obtainMessage(HttpDefine.RESPONSE_SUCCESS, topics);
						msg.arg1 = 1;
						msg.sendToTarget();
					}
			}
			if (application.isNetworkAvailable())
			{
					GetBbsTopicsByCreator getBbsTopicsByCreator = new GetBbsTopicsByCreator();
					getBbsTopicsByCreator.setCommunityID(PreferencesUtils.getCommunity().getID());
//					getBbsTopicsByCreator.setKey(application.getKey());
//					getBbsTopicsByCreator.setToken(application.getToken());
					getBbsTopicsByCreator.setKey(PreferencesUtils.getLoginKey());
					getBbsTopicsByCreator.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					getBbsTopicsByCreator.setCreatorID(userID);
					getBbsTopicsByCreator.setCount(direction);
					getBbsTopicsByCreator.setOffset(offset);
					getBbsTopicsByCreator.setCount(Constant.PAGE_SIZE);
					NetUtil.post(Constant.BASE_URL, getBbsTopicsByCreator, handler, HttpDefine.GET_BBS_POSTS_BY_CREATOR_RESP);

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
	public boolean handleMessage(Message msg) {
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
				initList(topics);
			}
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
						mDownListView.setDividerHeight(0);
						footer_bar.setVisibility(View.GONE);
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						break;
					}
					if (direction == Constant.DIRECTION.FORWARD)
					{
						// 下拉刷新
						mDownListView.setDividerHeight(1);
						List<BbsTopic> forward_list = resp.getBbsTopics();
						topics.clear();
						topics.addAll(forward_list);
						DataFileUtils.saveObject(resp, object_key);
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						if(forward_list.size()<Constant.PAGE_SIZE){
							mDownListView.removeFooterView(footer_view);
						}
						topicNum=resp.getTopicCount();
						topicComment=resp.getCommentCount();
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
						topicNum=resp.getTopicCount();
						topicComment=resp.getCommentCount();
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
	private void initList(List<BbsTopic> topics)
	{
		adapter = new MyTopicAdapter(application,this, topics,topicNum,topicComment);
		mDownListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View v) {
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
						Intent intent = new Intent(MyTopicActivity.this, SwitchResidentAddressActivity.class);
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
			direction=Constant.DIRECTION.FORWARD;
			loadData(direction, 0, false);
			application.setIsRefresh(false);
		}
	}
	
}

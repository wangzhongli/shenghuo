package com.e1858.wuye.activity;
import java.util.Date;
import java.util.LinkedList;
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
import com.e1858.wuye.adapter.NoticeListViewAdapter;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.GetNotices;
import com.e1858.wuye.protocol.http.GetNoticesResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.Notice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 通知列表
 * 
 * @author jiajia 2014年8月22日
 * 
 */
@SuppressLint("SimpleDateFormat")
public class NoticesActivity extends BaseActivity {
	
	private TextView toast_empty;
	private PullToRefreshListView mDownListView;
	private LinkedList<Notice> notices = new LinkedList<Notice>();
	private int direction = 1;// 默认=1
	private NoticeListViewAdapter adapter = null;
	private String object_key;
	private int remark=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notices);
		initView();
		direction = Constant.DIRECTION.FORWARD;
		remark=getIntent().getExtras().getInt(Constant.PIC_REMARK);
		Community community = PreferencesUtils.getCommunity();
		if (null != community) {
			object_key = "notice_" + community.getID()+remark;
		} else {
			object_key = "notice"+remark;
		}
		loadData(direction, 0, true);
		footer_more.setVisibility(View.GONE);
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		toast_empty=(TextView)findViewById(R.id.toast_empty);
		mDownListView = (PullToRefreshListView) findViewById(R.id.listview);
		footer_view=getLayoutInflater().inflate(R.layout.listview_footer, null);
		footer_bar=(ProgressBar)footer_view.findViewById(R.id.listview_foot_progress);
		footer_more=(TextView)footer_view.findViewById(R.id.listview_foot_more);	
		mDownListView.addFooterView(footer_view);
		bar_title.setText(getResources().getString(R.string.text_bar_notice));
		bar_leftBtn.setVisibility(View.VISIBLE);
		// 下拉刷新
		mDownListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mDownListView.onScrollStateChanged(view, scrollState);
				//数据为空--不用继续下面代码了
				if(notices.isEmpty()) return;	
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
						loadData(direction, notices.size(), false);
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

	private void loadData(int direction, int offset, boolean localCache) {
		if (localCache) {
			GetNoticesResp resp = (GetNoticesResp) DataFileUtils
					.readObject(object_key);
			if (null != resp) {
				notices.addAll(resp.getNotices());
				Message msg = handler.obtainMessage(
						HttpDefine.RESPONSE_SUCCESS, notices);
				msg.arg1 = 1;
				msg.sendToTarget();
			}
		}
		if (application.isNetworkAvailable()) {
			// openProgressDialog("加载中...");
			
			GetNotices getNotices = new GetNotices();
			getNotices.setCommand(HttpDefine.GET_NOTICES_SELF);
			getNotices.setCommunityID(PreferencesUtils.getCommunity().getID());
			getNotices.setOffset(offset);
//			getNotices.setKey(application.getKey());
//			getNotices.setToken(application.getToken());
			getNotices.setKey(PreferencesUtils.getLoginKey());
			getNotices.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			getNotices.setCount(Constant.PAGE_SIZE);
			if(remark==1){
				getNotices.setType(Constant.NOTICE_TYPE.ALL);
			}else{
				getNotices.setType(Constant.NOTICE_TYPE.PUBLIC);
			}
			NetUtil.post(Constant.BASE_URL, getNotices, handler,
					HttpDefine.GET_NOTICES_SELF_RESP);
		} else {
			ToastUtil.show(this, getResources()
					.getString(R.string.network_fail));
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			// closeProgressDialog();
			footer_bar.setVisibility(View.GONE);
			footer_more.setVisibility(View.GONE);
			mDownListView.onRefreshComplete();
			return true;
		}
		switch (msg.what) {
		case Constant.FAIL_CODE:
			if (msg.obj != null) {
				footer_bar.setVisibility(View.GONE);
				footer_more.setVisibility(View.GONE);
				mDownListView.onRefreshComplete();
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case HttpDefine.RESPONSE_SUCCESS:
			if (msg.arg1 == 1) {
				if(!application.isNetworkAvailable()&&notices.size()<Constant.PAGE_SIZE){
					mDownListView.removeFooterView(footer_view);
				}
				if(notices.size()==0||notices==null){
					mDownListView.setVisibility(View.GONE);
					toast_empty.setVisibility(View.VISIBLE);
				}else{
					initList(notices);
				}
				
			}
			break;
		case HttpDefine.GET_NOTICES_SELF_RESP:
			// closeProgressDialog();
			if (null != (String) msg.obj) {
				GetNoticesResp resp = JsonUtil.fromJson((String) msg.obj,
						GetNoticesResp.class);
				if (null == resp) {
					break;
				}
				try {
					if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
						if((resp.getNotices()==null||resp.getNotices().size()==0)&&direction==Constant.DIRECTION.BACKWARD){
							footer_bar.setVisibility(View.GONE);
							footer_more.setVisibility(View.VISIBLE);
							footer_more.setText(getResources().getString(R.string.loading_full));
							break;
						}
						if((resp.getNotices()==null||resp.getNotices().size()==0)&&direction==Constant.DIRECTION.FORWARD){
//							notices.clear();
//							initList(notices);
//							mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
							footer_bar.setVisibility(View.GONE);
							mDownListView.setVisibility(View.GONE);
							toast_empty.setVisibility(View.VISIBLE);
							break;
						}
						if (direction == Constant.DIRECTION.FORWARD) {
							// 下拉刷新
							List<Notice> forward_list = resp.getNotices();
							notices.clear();
							notices.addAll(forward_list);
							DataFileUtils.saveObject(resp, object_key);
							mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
							if(forward_list.size()<Constant.PAGE_SIZE){
								mDownListView.removeFooterView(footer_view);
							}
							initList(notices);
						} else {
							// 底部加载更多
							footer_bar.setVisibility(View.GONE);
							footer_more.setVisibility(View.VISIBLE);
							if(resp.getNotices().size()<Constant.PAGE_SIZE){
								footer_more.setText(getResources().getString(R.string.loading_full));
							}else{
								footer_more.setText(getResources().getString(R.string.footer_more));
							}
							mDownListView.setSelection(mDownListView.getCount()-resp.getNotices().size()+1);
							notices.addAll(resp.getNotices());
							adapter.notifyDataSetChanged();
						}
					} else {
						mDownListView.onRefreshComplete();
						ToastUtil.show(this, resp.getError());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	private void initList(LinkedList<Notice> notices) {
		adapter = new NoticeListViewAdapter(this, notices);
		mDownListView.setAdapter(adapter);
	}
}

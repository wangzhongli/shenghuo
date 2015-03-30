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
import com.e1858.wuye.adapter.ConvenienceInfoAdapter;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.Convenient;
import com.e1858.wuye.protocol.http.GetConvenients;
import com.e1858.wuye.protocol.http.GetConvenientsResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 便民记录
 * @author jiajia
 *
 */
public class ConvenienceInfoActivity extends BaseActivity {
	private int type;
	private String title;
	private PullToRefreshListView mDownListView;
	private TextView toast_empty;
	private List<Convenient> convenients = new ArrayList<Convenient>();
	private ConvenienceInfoAdapter adapter = null;
	private String object_key;
	private int direction=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convenience_info);
		type = getIntent().getExtras().getInt(Constant.DETAIL_ID);
		title = getIntent().getExtras().getString(Constant.DETAIL_TITLE);
		direction = Constant.DIRECTION.FORWARD;
		Community community=PreferencesUtils.getCommunity();
		initView();
		if(null!=community){
			object_key = ConvenienceInfoActivity.class.getSimpleName() + "_" + type+"_"+community.getID();
		}else{
			object_key=ConvenienceInfoActivity.class.getSimpleName() + "_" + type;
		}
		loadData(direction, 0, true,type);
		footer_more.setVisibility(View.GONE);
	}

	private void initView() {
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title.setText(title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		mDownListView = (PullToRefreshListView) findViewById(R.id.listview);
		footer_view=getLayoutInflater().inflate(R.layout.listview_footer, null);
		footer_bar=(ProgressBar)footer_view.findViewById(R.id.listview_foot_progress);
		footer_more=(TextView)footer_view.findViewById(R.id.listview_foot_more);	
		mDownListView.addFooterView(footer_view);
		toast_empty=(TextView)findViewById(R.id.toast_empty);
		mDownListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mDownListView.onScrollStateChanged(view, scrollState);
				//数据为空--不用继续下面代码了
				if(convenients.isEmpty()) return;	
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
						loadData(direction, convenients.size(), false,type);
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
					loadData(direction, 0, false,type);
				} else {
					ToastUtil.show(getApplicationContext(), getResources()
							.getString(R.string.network_fail));
				}
            }
        });		
	}

	private void loadData(int direction,int offset,boolean isLocalCache,int type) {
		if (isLocalCache) {
			GetConvenientsResp resp = (GetConvenientsResp) DataFileUtils
					.readObject(object_key);
			if (null != resp) {
				convenients.addAll(resp.getConveniences());
				Message msg = handler.obtainMessage(
						HttpDefine.RESPONSE_SUCCESS, convenients);
				msg.arg1 = 1;
				msg.sendToTarget();
			}
		}
		if (application.isNetworkAvailable()) {
			GetConvenients getConvenients = new GetConvenients();
			getConvenients.setToken(Encrypt.MD5(Constant.TokenSalt));
			getConvenients.setTypeID(type);
			getConvenients.setOffset(offset);
			getConvenients.setCount(Constant.PAGE_SIZE);
			getConvenients.setCommunityID(PreferencesUtils.getCommunity().getID());
			NetUtil.post(Constant.BASE_URL, getConvenients, handler,
					HttpDefine.GET_CONVENIENTS_RESP);
		} else {
			ToastUtil.show(this, getResources()
					.getString(R.string.network_fail));
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
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
		case HttpDefine.RESPONSE_SUCCESS:
			if (msg.arg1 == 1) {
				if(!application.isNetworkAvailable()&&convenients.size()<Constant.PAGE_SIZE){
					mDownListView.removeFooterView(footer_view);
				}
				if(convenients==null||convenients.size()==0){
					mDownListView.setVisibility(View.GONE);
					toast_empty.setVisibility(View.VISIBLE);
				}else{
					initList(convenients);
				}
				
			}
			break;
		case HttpDefine.GET_CONVENIENTS_RESP:
			if (null != (String) msg.obj) {
				GetConvenientsResp resp = JsonUtil.fromJson((String) msg.obj,
						GetConvenientsResp.class);
				if (null == resp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					
					if((resp.getConveniences()==null||resp.getConveniences().size()==0)&&direction==Constant.DIRECTION.BACKWARD){
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						footer_more.setText(getResources().getString(R.string.loading_full));
						break;
					}
					if((resp.getConveniences()==null||resp.getConveniences().size()==0)&&direction==Constant.DIRECTION.FORWARD){
						footer_bar.setVisibility(View.GONE);
						mDownListView.setVisibility(View.GONE);
						toast_empty.setVisibility(View.VISIBLE);
//						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						break;
					}
					if (direction == Constant.DIRECTION.FORWARD) {
						mDownListView.setVisibility(View.VISIBLE);
						toast_empty.setVisibility(View.GONE);
						convenients.clear();
						convenients.addAll(resp.getConveniences());
						DataFileUtils.saveObject(resp, object_key);
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						if(resp.getConveniences().size()<Constant.PAGE_SIZE){
							mDownListView.removeFooterView(footer_view);
						}
						initList(convenients);
					}else{
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						if(resp.getConveniences().size()<Constant.PAGE_SIZE){
							footer_more.setText(getResources().getString(R.string.loading_full));
						}else{
							footer_more.setText(getResources().getString(R.string.footer_more));
						}
						mDownListView.setSelection(mDownListView.getCount()-resp.getConveniences().size()+1);
						convenients.addAll(resp.getConveniences());
						adapter.notifyDataSetChanged();
					}
					
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	private void initList(List<Convenient> lists) {
		// TODO Auto-generated method stub
		adapter = new ConvenienceInfoAdapter(this, lists);
		mDownListView.setAdapter(adapter);
	}

}

package com.e1858.wuye.activity;

import java.util.Date;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.PullToRefreshListView;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.CommissionListViewAdapter;
import com.e1858.wuye.protocol.http.Commission;
import com.e1858.wuye.protocol.http.GetCommissions;
import com.e1858.wuye.protocol.http.GetCommissionsResp;
import com.e1858.wuye.protocol.http.HttpDefine;

/**
 * 代办列表
 * 
 * @author jiajia 2014年8月22日
 */
@SuppressLint("SimpleDateFormat")
public class CommissionsActivity extends BaseActivity implements OnClickListener {
	private PullToRefreshListView		mDownListView;
	private TextView					toast_empty;
	private int							direction	= 1;
	private LinkedList<Commission>		commissions	= new LinkedList<Commission>();
	private CommissionListViewAdapter	adapter		= null;
	private String						object_key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commissions);
		object_key = CommissionsActivity.class.getSimpleName() + PreferencesUtils.getUserName()
				+ PreferencesUtils.getCommunity().getID();
		initView();
		direction = Constant.DIRECTION.FORWARD;
		loadData(Constant.DIRECTION.FORWARD, 0, true);
		footer_more.setVisibility(View.GONE);
	}

	private void initView() {
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_rightBtn = (Button) findViewById(R.id.bar_right_btn);
		mDownListView = (PullToRefreshListView) findViewById(R.id.listview);
		toast_empty = (TextView) findViewById(R.id.toast_empty);
		footer_view = getLayoutInflater().inflate(R.layout.listview_footer, null);
		footer_bar = (ProgressBar) footer_view.findViewById(R.id.listview_foot_progress);
		footer_more = (TextView) footer_view.findViewById(R.id.listview_foot_more);
		mDownListView.addFooterView(footer_view);

		bar_title.setText(getResources().getString(R.string.text_bar_commissions));
		bar_rightBtn.setBackgroundResource(R.drawable.add_new_icon_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_rightBtn.setVisibility(View.VISIBLE);
		bar_rightBtn.setOnClickListener(this);

		// 下拉刷新
		mDownListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mDownListView.onScrollStateChanged(view, scrollState);
				// 数据为空--不用继续下面代码了
				if (commissions.isEmpty())
					return;
				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(footer_view) == view.getLastVisiblePosition())
						scrollEnd = true;
				}
				catch (Exception e) {
					scrollEnd = false;
				}
				if (scrollEnd) {
					if (view.getCount() >= Constant.PAGE_SIZE) {
						footer_more.setText(R.string.pull_to_refresh_refreshing_label);
						footer_bar.setVisibility(View.VISIBLE);
						direction = Constant.DIRECTION.BACKWARD;
						loadData(direction, commissions.size(), false);
					} else {
						footer_bar.setVisibility(View.GONE);
						footer_more.setText(getResources().getString(R.string.loading_full));
					}

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				mDownListView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
		mDownListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (application.isNetworkAvailable()) {
					direction = Constant.DIRECTION.FORWARD;
					loadData(direction, 0, false);
				} else {
					ToastUtil.show(getApplicationContext(), getResources().getString(R.string.network_fail));
				}
			}
		});

	}

	private void loadData(int direction, int offset, boolean localcache) {
		if (localcache) {
			GetCommissionsResp resp = (GetCommissionsResp) DataFileUtils.readObject(object_key);
			if (null != resp) {
				commissions.addAll(resp.getCommissions());
				Message msg = handler.obtainMessage(HttpDefine.RESPONSE_SUCCESS, commissions);
				msg.arg1 = 1;
				msg.sendToTarget();
			}
		}

		if (application.isNetworkAvailable()) {
			GetCommissions getCommissions = new GetCommissions();
			getCommissions.setCommunityID(PreferencesUtils.getCommunity().getID());
			getCommissions.setOffset(offset);
//			getCommissions.setKey(application.getKey());
//			getCommissions.setToken(application.getToken());
			getCommissions.setKey(PreferencesUtils.getLoginKey());
			getCommissions.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
			getCommissions.setCount(Constant.PAGE_SIZE);
			NetUtil.post(Constant.BASE_URL, getCommissions, handler, HttpDefine.GET_COMMISSIONS_RESP);
		} else {
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
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
				if (!application.isNetworkAvailable() && commissions.size() < Constant.PAGE_SIZE) {
					mDownListView.removeFooterView(footer_view);
				}
				if (commissions == null || commissions.size() == 0) {
					mDownListView.setVisibility(View.GONE);
					toast_empty.setVisibility(View.VISIBLE);
				} else {
					initList(commissions);
				}

			}
			break;
		case HttpDefine.GET_COMMISSIONS_RESP:
			if (null != (String) msg.obj) {
				GetCommissionsResp resp = JsonUtil.fromJson((String) msg.obj, GetCommissionsResp.class);
				if (null == resp) {

					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {

					if ((resp.getCommissions() == null || resp.getCommissions().size() == 0)
							&& direction == Constant.DIRECTION.BACKWARD) {
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						footer_more.setText(getResources().getString(R.string.loading_full));
						break;
					}
					if ((resp.getCommissions() == null || resp.getCommissions().size() == 0)
							&& direction == Constant.DIRECTION.FORWARD) {
//						commissions.clear();
//						initList(commissions);
						footer_bar.setVisibility(View.GONE);
//						mDownListView
//								.onRefreshComplete(getString(R.string.pull_to_refresh_update)
//										+ new Date().toLocaleString());
						mDownListView.setVisibility(View.GONE);
						toast_empty.setVisibility(View.VISIBLE);
						break;
					}

					if (direction == Constant.DIRECTION.FORWARD) {
						// 下拉刷新
						mDownListView.setVisibility(View.VISIBLE);
						toast_empty.setVisibility(View.GONE);
						commissions.clear();
						commissions.addAll(resp.getCommissions());
						DataFileUtils.saveObject(resp, object_key);
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update)
								+ new Date().toLocaleString());
						if (resp.getCommissions().size() < Constant.PAGE_SIZE) {
							mDownListView.removeFooterView(footer_view);
						}
						initList(commissions);
					} else {
						// 底部加载更多
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						if (resp.getCommissions().size() < Constant.PAGE_SIZE) {
							footer_more.setText(getResources().getString(R.string.loading_full));
						} else {
							footer_more.setText(getResources().getString(R.string.footer_more));
						}
						mDownListView.setSelection(mDownListView.getCount() - resp.getCommissions().size() + 1);
						commissions.addAll(resp.getCommissions());
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

	private void initList(LinkedList<Commission> commissions) {
		adapter = new CommissionListViewAdapter(this, commissions);
		mDownListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bar_right_btn:
//			if (null == PreferencesUtils.getUserHouse()()) {
//				ToastUtil.show(this, "请先选择房屋信息");
//				new Handler().postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						Intent intent = new Intent(CommissionsActivity.this,
//								SwitchResidentAddressActivity.class);
//						startActivityForResult(intent,
//								Constant.HOUSE_INFO_RESULT_CODE);
//					}
//				}, 1000);
//
//			} else {
			Intent intent = new Intent(this, AddCommissionActivity.class);
			startActivity(intent);

//			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.HOUSE_INFO_RESULT_CODE:
			if (PreferencesUtils.getUserHouse() == null) {
				ToastUtil.show(this, "对不起,您还未选择房屋信息");
			} else {
				Intent intent = new Intent(this, AddCommissionActivity.class);
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (application.getIsRefresh()) {
			direction = Constant.DIRECTION.FORWARD;
			loadData(direction, 0, false);
			application.setIsRefresh(false);
		}
	}

}

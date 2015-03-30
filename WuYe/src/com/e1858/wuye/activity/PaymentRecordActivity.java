package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.common.app.PaymentMethodActivity;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.PullToRefreshListView;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.PaymentRecordListViewAdapter;
import com.e1858.wuye.protocol.http.FeeBill;
import com.e1858.wuye.protocol.http.GetFeeBills;
import com.e1858.wuye.protocol.http.GetFeeBillsResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 缴费记录
 * 
 * @author jiajia
 * 
 */
public class PaymentRecordActivity extends BaseActivity implements
		OnClickListener {

	private PullToRefreshListView mDownListView;
	private TextView toast_empty;
	private int direction = 1;
	private List<FeeBill> feeBills = new ArrayList<FeeBill>();
	private PaymentRecordListViewAdapter adapter = null;
	private String object_key;
	private BroadcastReceiver	payReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_record);
		object_key = PaymentRecordActivity.class.getSimpleName()
				+ PreferencesUtils.getUserName()
				+ PreferencesUtils.getCommunity().getID();
		initView();
		direction = Constant.DIRECTION.FORWARD;
		loadData(direction, 0, false);
		footer_more.setVisibility(View.GONE);
		payReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				loadData(direction, 0, false);
			}
		};
		registerReceiver(payReceiver, new IntentFilter(PaymentMethodActivity.BroadcastAction_PaySuccess));
	}
	
	public void onDestroy() {
		unregisterReceiver(payReceiver);
		super.onDestroy();
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_rightBtn = (Button) findViewById(R.id.bar_right_btn);
		mDownListView = (PullToRefreshListView) findViewById(R.id.listview);
		toast_empty = (TextView) findViewById(R.id.toast_empty);
		bar_title.setText("缴费记录");
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_rightBtn.setBackgroundResource(R.drawable.add_new_icon_background);
		bar_rightBtn.setVisibility(View.VISIBLE);
		bar_rightBtn.setOnClickListener(this);
		footer_view = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		footer_bar = (ProgressBar) footer_view
				.findViewById(R.id.listview_foot_progress);
		footer_more = (TextView) footer_view
				.findViewById(R.id.listview_foot_more);
		mDownListView.addFooterView(footer_view);
		// 下拉刷新
		mDownListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mDownListView.onScrollStateChanged(view, scrollState);
				// 数据为空--不用继续下面代码了
				if (feeBills.isEmpty())
					return;
				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(footer_view) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				if (scrollEnd) {
					if (view.getCount() >= Constant.PAGE_SIZE) {
						footer_more
								.setText(R.string.pull_to_refresh_refreshing_label);
						footer_bar.setVisibility(View.VISIBLE);
						direction = Constant.DIRECTION.BACKWARD;
						loadData(direction, feeBills.size(), false);
					} else {
						footer_bar.setVisibility(View.GONE);
						footer_more.setText(getResources().getString(
								R.string.loading_full));
					}

				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mDownListView.onScroll(view, firstVisibleItem,
						visibleItemCount, totalItemCount);
			}
		});
		mDownListView
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						if (application.isNetworkAvailable()) {
							direction = Constant.DIRECTION.FORWARD;
							loadData(direction, 0, false);
						} else {
							ToastUtil.show(
									getApplicationContext(),
									getResources().getString(
											R.string.network_fail));
						}
					}
				});

	}

	private void loadData(int direction, int offset, boolean localcache) {
		if (localcache) {
			GetFeeBillsResp resp = (GetFeeBillsResp) DataFileUtils
					.readObject(object_key);
			if (null != resp) {
				feeBills.addAll(resp.getFeeBills());
				Message msg = handler.obtainMessage(
						HttpDefine.RESPONSE_SUCCESS, feeBills);
				msg.arg1 = 1;
				msg.sendToTarget();
			}
		}

		if (application.isNetworkAvailable()) {
			GetFeeBills getFeeBills = new GetFeeBills();
			getFeeBills.setCommunityID(PreferencesUtils.getCommunity().getID());
			getFeeBills.setOffset(offset);
			getFeeBills.setCount(Constant.PAGE_SIZE);
			getFeeBills.setKey(PreferencesUtils.getLoginKey());
			getFeeBills.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()
					+ Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getFeeBills, handler,
					HttpDefine.GET_FEEBILLS_RESP);
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
			break;
		case HttpDefine.RESPONSE_SUCCESS:
			if (msg.arg1 == 1) {
				if (!application.isNetworkAvailable()
						&& feeBills.size() < Constant.PAGE_SIZE) {
					mDownListView.removeFooterView(footer_view);
				}
				if (feeBills == null || feeBills.size() == 0) {
					mDownListView.setVisibility(View.GONE);
					toast_empty.setVisibility(View.VISIBLE);
				} else {
					initList(feeBills);
				}

			}
			break;
		case HttpDefine.GET_FEEBILLS_RESP:
			if (null != (String) msg.obj) {
				GetFeeBillsResp resp = JsonUtil.fromJson((String) msg.obj,
						GetFeeBillsResp.class);
				if (null == resp) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					if ((resp.getFeeBills() == null || resp.getFeeBills()
							.size() == 0)
							&& direction == Constant.DIRECTION.BACKWARD) {
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						footer_more.setText(getResources().getString(
								R.string.loading_full));
						break;
					}
					if ((resp.getFeeBills() == null || resp.getFeeBills()
							.size() == 0)
							&& direction == Constant.DIRECTION.FORWARD) {
						feeBills.clear();
						mDownListView.setVisibility(View.GONE);
						toast_empty.setVisibility(View.VISIBLE);
						break;
					}
					if (direction == Constant.DIRECTION.FORWARD) {
						mDownListView.setVisibility(View.VISIBLE);
						toast_empty.setVisibility(View.GONE);
						List<FeeBill> forward_list = resp.getFeeBills();
						// 下拉刷新
						feeBills.clear();
						feeBills.addAll(forward_list);
						DataFileUtils.saveObject(resp, object_key);
						mDownListView
								.onRefreshComplete(getString(R.string.pull_to_refresh_update)
										+ new Date().toLocaleString());
						if (forward_list.size() < Constant.PAGE_SIZE) {
							mDownListView.removeFooterView(footer_view);
						}
						initList(feeBills);
					} else {
						// 底部加载更多
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						if (resp.getFeeBills().size() < Constant.PAGE_SIZE) {
							footer_more.setText(getResources().getString(
									R.string.loading_full));
						} else {
							footer_more.setText(getResources().getString(
									R.string.footer_more));
						}
						mDownListView.setSelection(mDownListView.getCount()
								- resp.getFeeBills().size() + 1);
						feeBills.addAll(resp.getFeeBills());
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

	private void initList(List<FeeBill> feeBills) {
		adapter = new PaymentRecordListViewAdapter(this, feeBills);
		mDownListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bar_right_btn:
			Intent intent = new Intent(this, PaymentMainActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		direction = Constant.DIRECTION.FORWARD;
		loadData(direction, 0, false);
	}

}

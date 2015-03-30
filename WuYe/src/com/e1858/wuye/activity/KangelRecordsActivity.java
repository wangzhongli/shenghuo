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
import com.e1858.wuye.adapter.KangelOrderListViewAdapter;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.KangelCancelOrderResp;
import com.e1858.wuye.protocol.http.KangelDeleteOrderResp;
import com.e1858.wuye.protocol.http.KangelGetOrders;
import com.e1858.wuye.protocol.http.KangelGetOrdersResp;
import com.e1858.wuye.protocol.http.KangelOrder;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author jiajia 洗衣记录
 * 
 */
public class KangelRecordsActivity extends BaseActivity {

	private PullToRefreshListView mDownListView;
	private TextView toast_empty;
	private int direction = 1;
	private List<KangelOrder> orders = new ArrayList<KangelOrder>();
	private KangelOrderListViewAdapter adapter = null;
	private String object_key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_record);
		object_key = KangelRecordsActivity.class.getSimpleName()
				+ PreferencesUtils.getUserName()
				+ PreferencesUtils.getCommunity().getID();
		initView();
		direction = Constant.DIRECTION.FORWARD;
		loadData(direction, 0, true);
		footer_more.setVisibility(View.GONE);
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		mDownListView = (PullToRefreshListView) findViewById(R.id.listview);
		toast_empty = (TextView) findViewById(R.id.toast_empty);
		bar_title.setText("洗衣订单");
		bar_leftBtn.setVisibility(View.VISIBLE);
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
				if (orders.isEmpty())
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
						loadData(direction, orders.size(), false);
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
			KangelGetOrdersResp resp = (KangelGetOrdersResp) DataFileUtils
					.readObject(object_key);
			if (null != resp) {
				orders.addAll(resp.getOrders());
				Message msg = handler.obtainMessage(
						HttpDefine.RESPONSE_SUCCESS, orders);
				msg.arg1 = 1;
				msg.sendToTarget();
			}
		}

		if (application.isNetworkAvailable()) {
			KangelGetOrders getOrders = new KangelGetOrders();
			getOrders.setCommunityID(PreferencesUtils.getCommunity().getID());
			getOrders.setOffset(offset);
			getOrders.setCount(Constant.PAGE_SIZE);
			getOrders.setKey(PreferencesUtils.getLoginKey());
			getOrders.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()
					+ Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getOrders, handler,
					HttpDefine.KANGEL_GET_ORDERS_RESP);
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
						&& orders.size() < Constant.PAGE_SIZE) {
					mDownListView.removeFooterView(footer_view);
				}
				if (orders == null || orders.size() == 0) {
					mDownListView.setVisibility(View.GONE);
					toast_empty.setVisibility(View.VISIBLE);
				} else {
					initList(orders);
				}

			}
			break;
		case HttpDefine.KANGEL_GET_ORDERS_RESP:
			if (null != (String) msg.obj) {
				KangelGetOrdersResp resp = JsonUtil.fromJson((String) msg.obj,
						KangelGetOrdersResp.class);
				if (null == resp) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					if ((resp.getOrders() == null || resp.getOrders().size() == 0)
							&& direction == Constant.DIRECTION.BACKWARD) {
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						footer_more.setText(getResources().getString(
								R.string.loading_full));
						break;
					}
					if ((resp.getOrders() == null || resp.getOrders().size() == 0)
							&& direction == Constant.DIRECTION.FORWARD) {
						orders.clear();
						mDownListView.setVisibility(View.GONE);
						toast_empty.setVisibility(View.VISIBLE);
						break;
					}
					if (direction == Constant.DIRECTION.FORWARD) {
						mDownListView.setVisibility(View.VISIBLE);
						toast_empty.setVisibility(View.GONE);
						List<KangelOrder> forward_list = resp.getOrders();
						// 下拉刷新
						orders.clear();
						orders.addAll(forward_list);
						DataFileUtils.saveObject(resp, object_key);
						mDownListView
								.onRefreshComplete(getString(R.string.pull_to_refresh_update)
										+ new Date().toLocaleString());
						if (forward_list.size() < Constant.PAGE_SIZE) {
							mDownListView.removeFooterView(footer_view);
						}
						initList(orders);
					} else {
						// 底部加载更多
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						if (resp.getOrders().size() < Constant.PAGE_SIZE) {
							footer_more.setText(getResources().getString(
									R.string.loading_full));
						} else {
							footer_more.setText(getResources().getString(
									R.string.footer_more));
						}
						mDownListView.setSelection(mDownListView.getCount()
								- resp.getOrders().size() + 1);
						orders.addAll(resp.getOrders());
						adapter.notifyDataSetChanged();

					}
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.KANGEL_CANCLE_ORDER_RESP:
			// 取消订单
			if (null != (String) msg.obj) {
				KangelCancelOrderResp resp = JsonUtil.fromJson(
						(String) msg.obj, KangelCancelOrderResp.class);
				if (resp == null) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					ToastUtil.show(this, "取消订单成功");
					direction = Constant.DIRECTION.FORWARD;
					loadData(direction, 0, false);
					adapter.notifyDataSetChanged();
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.KANGEL_DELETE_ORDER_RESP:
			// 删除订单
			if (null != (String) msg.obj) {
				KangelDeleteOrderResp resp = JsonUtil.fromJson(
						(String) msg.obj, KangelDeleteOrderResp.class);
				if (resp == null) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					ToastUtil.show(this, "删除订单成功");
					direction = Constant.DIRECTION.FORWARD;
					loadData(direction, 0, false);
					adapter.notifyDataSetChanged();
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	private void initList(List<KangelOrder> orders) {
		adapter = new KangelOrderListViewAdapter(this, orders, handler,
				PreferencesUtils.getCommunity().getID(), PreferencesUtils.getLoginKey());
		mDownListView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		direction = Constant.DIRECTION.FORWARD;
		loadData(direction, 0, false);
	}

}

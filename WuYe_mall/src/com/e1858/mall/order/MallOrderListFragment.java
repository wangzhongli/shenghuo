package com.e1858.mall.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.e1858.common.app.PaymentMethodActivity;
import com.e1858.mall.MallConstant.MallOrderStatus;
import com.e1858.mall.MallPullRefreshListFragment;
import com.e1858.mall.entity.MallOrderEntity;
import com.e1858.mall.protocol.packet.MallGetOrderRequest;
import com.e1858.mall.protocol.packet.MallGetOrderResponse;
import com.e1858.mall.protocol.packet.MallGetOrdersRequest;
import com.e1858.mall.protocol.packet.MallGetOrdersResponse;
import com.e1858.mall.widget.MallOrderCardNormalHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.wuye.mall.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.hg.android.utils.HGUtils;
import com.j256.ormlite.stmt.QueryBuilder;

public class MallOrderListFragment extends MallPullRefreshListFragment {
	public static final int		RequestCode_Pay		= 100000;

	public static final String	IntentKey_Status	= "IntentKey_Status";

	MyAdapter					adapter;
	BroadcastReceiver			payReceiver, refundReceiver;

	int							status				= MallOrderStatus.All;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		EntityCacheHelper.sharedInstance().deleteAllEntities(MallOrderEntity.class);

		status = getArguments().getInt(IntentKey_Status, status);
		initViews();
		payReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				String orderID = arg1.getStringExtra(PaymentMethodActivity.IntentKey_TAG);
				refreshOrder(orderID);
			}
		};
		getActivity().registerReceiver(payReceiver, new IntentFilter(PaymentMethodActivity.BroadcastAction_PaySuccess));

		refundReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				String orderID = arg1.getStringExtra(MallOrderRefundActivity.IntentKey_OrderID);
				refreshOrder(orderID);
			}
		};
		getActivity().registerReceiver(refundReceiver,
				new IntentFilter(MallOrderRefundActivity.BroadcastAction_RefundSuccess));
	}

	@Override
	public void onDestroyView() {
		if (adapter != null) {
			adapter.unload();
		}
		getActivity().unregisterReceiver(payReceiver);
		getActivity().unregisterReceiver(refundReceiver);
		super.onDestroyView();
	}

	void initViews() {
		listView.getRefreshableView().setBackgroundColor(getResources().getColor(R.color.mall_bg));
		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.getRefreshableView().setDividerHeight(HGUtils.dip2px(getActivity(), 8));
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadOrders(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadOrders(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.setAdapter(adapter = new MyAdapter(getActivity()));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MallOrderEntity entity = (MallOrderEntity) parent.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), MallOrderDetailActivity.class);
				intent.putExtra(MallOrderDetailActivity.IntentKey_OrderID, entity.getBean().getID());
				startActivity(intent);
			}
		});

		adapter.load(getLoaderManager(), SqliteOpenHelper.class, MallOrderEntity.class);

		setEmptyText("没有订单");
	}

	@Override
	public void onResume() {
		super.onResume();
		loadOrders(0);
	}

	protected void loadOrders(final int offset) {
		ResponseHandler<MallGetOrdersResponse> responseHandler = new ResponseHandler<MallGetOrdersResponse>() {
			@Override
			public void onStart() {
				updateEmptyStatus(true, false);
			}

			@Override
			public void onFinish(MallGetOrdersResponse response, String error) {
				updateEmptyStatus(false, response == null || HGUtils.isListEmpty(response.getOrders()));
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(MallOrderEntity.class, response.getOrders(),
							offset);
				}
			}
		};

		MallGetOrdersRequest request = new MallGetOrdersRequest();
		request.setOffset(offset);
		request.setCount(5/* MallConstant.FetchCount */);
		request.setStatus(status);
		HttpPacketClient.postPacketAsynchronous(request, MallGetOrdersResponse.class, responseHandler, true);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<MallOrderEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, MallOrderEntity entity, ViewGroup parent) {
			final View view = View.inflate(context, R.layout.mall_card_order_normal, null);
			view.setTag(new MallOrderCardNormalHolder(context, view.findViewById(R.id.orderNormalCardView)));
			return view;
		}

		@Override
		public void bindView(View view, Context context, MallOrderEntity entity, int position) {
			MallOrderCardNormalHolder handler = (MallOrderCardNormalHolder) view.getTag();
			handler.updateForOrder(entity.getBean());
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<MallOrderEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}
	}

	void refreshOrder(String orderID) {
		if (TextUtils.isEmpty(orderID)) {
			return;
		}

		final MallGetOrderRequest request = new MallGetOrderRequest();
		request.setID(orderID);
		ResponseHandler<MallGetOrderResponse> responseHandler = new ResponseHandler<MallGetOrderResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在处理");
			}

			@Override
			public void onFinish(MallGetOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntity(MallOrderEntity.class, response.getOrder());
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGetOrderResponse.class, responseHandler, true);
	}
}

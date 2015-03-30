package com.e1858.mall.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.PaymentMethodActivity;
import com.e1858.mall.order.MallOrderButtonsManager.OnUpdateListener;
import com.e1858.mall.protocol.bean.MallManagedAddressBean;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.mall.protocol.packet.MallGetOrderRequest;
import com.e1858.mall.protocol.packet.MallGetOrderResponse;
import com.e1858.mall.widget.MallOrderCardDetailHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.mall.R;

public class MallOrderDetailActivity extends BaseActionBarActivity {
	public static final String	IntentKey_OrderID	= "IntentKey_OrderID";

	String						orderID;

	MallOrderCardDetailHolder	orderDetailHandler;
	MallOrderStatusList			orderStatusList;
	MallOrderButtonsManager		buttonsManager;

	TextView					textView_address;

	BroadcastReceiver			payReceiver, refundReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent() != null) {
			orderID = getIntent().getStringExtra(IntentKey_OrderID);
		}

		if (TextUtils.isEmpty(orderID)) {
			finish();
			return;
		}

		setContentView(R.layout.mall_activity_orderdetail);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews();

		payReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				loadOrder();
			}
		};
		registerReceiver(payReceiver, new IntentFilter(PaymentMethodActivity.BroadcastAction_PaySuccess));

		refundReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				loadOrder();
			}
		};
		registerReceiver(refundReceiver, new IntentFilter(MallOrderRefundActivity.BroadcastAction_RefundSuccess));

		loadOrder();

	}

	@Override
	public void onDestroy() {
		unregisterReceiver(payReceiver);
		unregisterReceiver(refundReceiver);
		super.onDestroy();
	}

	void initViews() {
		textView_address = (TextView) findViewById(R.id.textView_address);
		View button_pay = findViewById(R.id.button_pay);
		View button_receive = findViewById(R.id.button_receive);
		View button_cancel = findViewById(R.id.button_cancel);
		View button_delete = findViewById(R.id.button_delete);
		View button_review = findViewById(R.id.button_review);
		View bottomBar = findViewById(R.id.bottomBar);
		buttonsManager = new MallOrderButtonsManager(bottomBar, button_pay, button_receive, button_cancel,
				button_delete, button_review);
		buttonsManager.setOnUpdateListener(new OnUpdateListener() {

			@Override
			public void onOrderUpdated(MallOrderBean newOrderBean) {
				updateForOrder(newOrderBean);
			}

			@Override
			public void onOrderDeleted(MallOrderBean oldOrderBean) {
				finish();
			}
		});
		orderStatusList = (MallOrderStatusList) findViewById(R.id.orderStatusList);
		orderDetailHandler = new MallOrderCardDetailHolder(getActivity(), findViewById(R.id.orderDetailCardView));
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	void loadOrder() {
		final MallGetOrderRequest request = new MallGetOrderRequest();
		request.setID(orderID);
		ResponseHandler<MallGetOrderResponse> responseHandler = new ResponseHandler<MallGetOrderResponse>() {
			View		container	= findViewById(R.id.progressContainer);
			ProgressBar	progressBar	= (ProgressBar) container.findViewById(R.id.progressBar);

			@Override
			public void onStart() {
				container.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onFinish(MallGetOrderResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					container.setVisibility(View.GONE);
					updateForOrder(response.getOrder());
				} else {
					container.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGetOrderResponse.class, responseHandler, true);
	}

	public void updateForOrder(MallOrderBean orderBean) {

		buttonsManager.updateForOrderBean(orderBean);
		if (orderBean == null) {
			return;
		}
		orderStatusList.updateForOrderStatusList(orderBean.getOrderStatus());
		orderDetailHandler.updateForOrder(orderBean);

		MallManagedAddressBean address = orderBean.getBuyerAddress();
		if (address == null) {
			textView_address.setText(null);
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append("姓名：");
			if (address.getName() != null) {
				builder.append(address.getName());
			}
			builder.append("\n手机：");
			if (address.getPhone() != null) {
				builder.append(address.getPhone());
			}
			builder.append("\n详细地址：");
			String city = address.provinceCityDistrict();
			if (city != null) {
				builder.append(city);
			}
			String detail = address.getAddress();
			if (detail != null) {
				builder.append(detail);
			}
			textView_address.setText(builder);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}

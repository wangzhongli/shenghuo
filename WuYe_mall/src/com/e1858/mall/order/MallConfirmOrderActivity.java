package com.e1858.mall.order;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.PaymentMethodActivity;
import com.e1858.mall.address.MallAddressActivity;
import com.e1858.mall.protocol.bean.MallConfirmOrderBean;
import com.e1858.mall.protocol.bean.MallManagedAddressBean;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.packet.MallCalcShippingCostRequest;
import com.e1858.mall.protocol.packet.MallCalcShippingCostResponse;
import com.e1858.mall.protocol.packet.MallGenerateOrderRequest;
import com.e1858.mall.protocol.packet.MallGenerateOrderResponse;
import com.e1858.mall.protocol.packet.MallGeneratePaymentOrderRequest;
import com.e1858.mall.protocol.packet.MallGeneratePaymentOrderResponse;
import com.e1858.mall.protocol.packet.MallGetManagedAddressesRequest;
import com.e1858.mall.protocol.packet.MallGetManagedAddressesResponse;
import com.e1858.mall.widget.MallOrderCardConfirmHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;

public class MallConfirmOrderActivity extends BaseActionBarActivity {
	public static final int					RequestCode_PickAddress	= 10000;
	public static final String				IntentKey_ConfirmOrders	= "IntentKey_ConfirmOrders";
	ArrayList<MallOrderCardConfirmHolder>	cardConfirmHolders		= new ArrayList<MallOrderCardConfirmHolder>();
	ArrayList<MallConfirmOrderBean>			confirmOrders;
	MallManagedAddressBean					addressBean;
	TextView								textView_address;

	BroadcastReceiver						deleteReceiver;
	private TextView						textView_amount;
	private TextView						textView_shippingCost;

	MallCalcShippingCostResponse			calcShippingCostResponse;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		confirmOrders = null;
		if (getIntent() != null) {
			confirmOrders = (ArrayList<MallConfirmOrderBean>) getIntent().getSerializableExtra(IntentKey_ConfirmOrders);
		}
		if (HGUtils.isListEmpty(confirmOrders)) {
			finish();
			return;
		}

		setContentView(R.layout.mall_activity_confirmorder);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews(confirmOrders);
		loadAddress();

		deleteReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				if (addressBean != null)
					if (arg1.getStringExtra(MallAddressActivity.IntentKey_AddressID).equals(addressBean.getID())) {
						updateAddressView(null);
						loadAddress();
					}
			}
		};
		registerReceiver(deleteReceiver, new IntentFilter(MallAddressActivity.BroadcastAction_Delete));
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(deleteReceiver);
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	void initViews(ArrayList<MallConfirmOrderBean> confirmOrders) {
		View listitem_address = findViewById(R.id.listitem_address);
		{
			listitem_address.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), MallAddressActivity.class);
					intent.putExtra(MallAddressActivity.IntentKey_isPick, true);
					startActivityForResult(intent, RequestCode_PickAddress);
				}
			});
			textView_address = ((TextView) listitem_address.findViewById(R.id.textView));
			//textView_address.setTextColor(Color.LTGRAY);
			textView_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			updateAddressView(null);
		}

		findViewById(R.id.button_submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				submitOrders();
			}
		});

		LinearLayout confirmGroupView = (LinearLayout) findViewById(R.id.confirmGroupView);
		confirmGroupView.removeAllViews();

		textView_amount = ((TextView) findViewById(R.id.textView_amount));
		textView_shippingCost = ((TextView) findViewById(R.id.textView_shippingCost));

		for (int iOrder = 0; iOrder < confirmOrders.size(); iOrder++) {
			MallConfirmOrderBean order = confirmOrders.get(iOrder);
			MallOrderCardConfirmHolder handler = new MallOrderCardConfirmHolder(getActivity(), null);
			cardConfirmHolders.add(handler);
			handler.updateForConfirmOrder(order);
			confirmGroupView.addView(handler.detach());
			if (iOrder != confirmOrders.size() - 1) {
				View sep = new View(getActivity());
				sep.setLayoutParams(new LayoutParams(0, HGUtils.dip2px(getActivity(), 8)));
				confirmGroupView.addView(sep);
			}
		}

		textView_amount.setText(String.format("实付款:¥%.2f", calcProductsAmount()));
		textView_shippingCost.setText("含运费:¥" + 0);
	}

	float calcProductsAmount() {
		float totalAmount = 0;
		for (int iOrder = 0; iOrder < confirmOrders.size(); iOrder++) {
			MallConfirmOrderBean order = confirmOrders.get(iOrder);
			for (int iProduct = 0; iProduct < order.getOrderProducts().size(); iProduct++) {
				MallOrderProductBean cartProduct = order.getOrderProducts().get(iProduct);
				totalAmount += cartProduct.getPrice() * cartProduct.getQuantity();
			}
		}
		return totalAmount;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RequestCode_PickAddress) {
			if (resultCode == RESULT_OK) {
				MallManagedAddressBean addressBean = (MallManagedAddressBean) data
						.getSerializableExtra(MallAddressActivity.IntentKey_PickedAddress);
				if (addressBean != null) {
					updateAddressView(addressBean);
				}
			} else {
				addressBean = null;
				updateAddressView(addressBean);
				loadAddress();
			}
		}
	}

	void loadAddress() {
		if (addressBean != null) {
			return;
		}
		MallGetManagedAddressesRequest request = new MallGetManagedAddressesRequest();
		request.setOffset(0);
		request.setCount(1);
		ResponseHandler<MallGetManagedAddressesResponse> responseHandler = new ResponseHandler<MallGetManagedAddressesResponse>() {

			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetManagedAddressesResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (!HGUtils.isListEmpty(response.getAddresses())) {
						updateAddressView(response.getAddresses().get(0));
					}
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGetManagedAddressesResponse.class, responseHandler);
	}

	void updateAddressView(MallManagedAddressBean addressBean) {
		this.addressBean = addressBean;
		if (addressBean == null) {
			textView_address.setText("请完善您的收货信息");
		} else {
			textView_address.setText("姓名： " + addressBean.getName() + "\n电话： " + addressBean.getPhone() + "\n详细地址： "
					+ addressBean.provinceCityDistrict() + addressBean.getAddress());
			calcShippingCost(addressBean);
		}
	}

	void fillOrderBeans() {
		List<Float> shippingCosts = null;
		if (calcShippingCostResponse != null) {
			shippingCosts = calcShippingCostResponse.getShippingCosts();
		}
		if (shippingCosts == null) {
			shippingCosts = new ArrayList<Float>();
		}
		for (int i = 0; i < cardConfirmHolders.size(); i++) {
			MallOrderCardConfirmHolder handler = cardConfirmHolders.get(i);
			MallConfirmOrderBean orderBean = handler.getConfirmOrder();
			orderBean.setRemark(handler.getRemark());
			if (shippingCosts.size() > i) {
				orderBean.setShippingCost(shippingCosts.get(i));
			}
		}
	}

	void calcShippingCost(MallManagedAddressBean addressBean) {
		calcShippingCostResponse = null;
		MallCalcShippingCostRequest request = new MallCalcShippingCostRequest();
		request.setAddressID(addressBean.getID());
		request.confirmOrders = confirmOrders;
		ResponseHandler<MallCalcShippingCostResponse> responseHandler = new ResponseHandler<MallCalcShippingCostResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在处理");
			}

			@Override
			public void onFinish(MallCalcShippingCostResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					calcShippingCostResponse = response;
					fillOrderBeans();
					float shippingCost = 0;
					for (float cost : response.getShippingCosts()) {
						shippingCost += cost;
					}
					for (MallOrderCardConfirmHolder holder : cardConfirmHolders) {
						holder.updateShippingCost();
					}
					textView_amount.setText(String.format("实付款:¥%.2f", calcProductsAmount() + shippingCost));
					textView_shippingCost.setText(String.format("含运费:¥%.2f", shippingCost));
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallCalcShippingCostResponse.class, responseHandler);

	}

	void submitOrders() {
		if (addressBean == null) {
			Toast.makeText(getActivity(), "请填写收货人信息", Toast.LENGTH_LONG).show();
			return;
		}

		if (calcShippingCostResponse == null) {
			calcShippingCost(addressBean);
			return;
		}

		fillOrderBeans();
		MallGenerateOrderRequest request = new MallGenerateOrderRequest();
		request.setAddressID(addressBean.getID());
		request.confirmOrders = confirmOrders;
		ResponseHandler<MallGenerateOrderResponse> responseHandler = new HttpPacketClient.ResponseHandler<MallGenerateOrderResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "订单提交中");
			}

			@Override
			public void onFinish(MallGenerateOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					onSubmitSuccess(response.getOrders());
				}
			}

		};
		HttpPacketClient.postPacketAsynchronous(request, MallGenerateOrderResponse.class, responseHandler, true);
	}

	private void onSubmitSuccess(List<MallOrderBean> orders) {
		Toast.makeText(MallConfirmOrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();

		float amount = 0;
		List<String> orderIDs = new ArrayList<String>();
		for (MallOrderBean order : orders) {
			orderIDs.add(order.getID());
			amount += order.getAmount() + order.getShippingCost();
		}
		final float finalAmount = amount;

		MallGeneratePaymentOrderRequest request = new MallGeneratePaymentOrderRequest();
		request.setOrderIDs(orderIDs);
		ResponseHandler<MallGeneratePaymentOrderResponse> responseHandler = new ResponseHandler<MallGeneratePaymentOrderResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在处理");
			}

			@Override
			public void onFinish(MallGeneratePaymentOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					Intent intent = new Intent(getActivity(), PaymentMethodActivity.class);
					intent.putExtra(PaymentMethodActivity.IntentKey_Amount, finalAmount);
					intent.putExtra(PaymentMethodActivity.IntentKey_PaymentParam, response.getPaymentParam());
					getActivity().startActivity(intent);
				}
				finish();
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGeneratePaymentOrderResponse.class, responseHandler, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

}

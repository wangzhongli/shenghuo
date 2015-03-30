package com.e1858.mall.order;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.Constant;
import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.mall.MallConstant.MallOrderRefundType;
import com.e1858.mall.entity.MallOrderEntity;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.bean.MallOrderStatusBean;
import com.e1858.mall.protocol.packet.MallGetOrderRequest;
import com.e1858.mall.protocol.packet.MallGetOrderResponse;
import com.e1858.mall.protocol.packet.MallRefundRequest;
import com.e1858.mall.protocol.packet.MallRefundResponse;
import com.e1858.mall.widget.MallProductOrderCardHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.HttpPacketClient.SimpleResponseHandler;
import com.e1858.wuye.mall.R;
import com.e1858.wuye.protocol.http.UploadJson;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.utils.HGUtils;
import com.hg.android.utils.ImageUtils;
import com.hg.android.widget.ImagesGridEditView;

public class MallOrderRefundActivity extends BaseActionBarActivity {
	public static final String	BroadcastAction_RefundSuccess	= "MallOrderRefundActivity.BroadcastAction_RefundSuccess";
	public static final String	IntentKey_OrderID				= "IntentKey_OrderID";
	public static final String	IntentKey_OrderProductID		= "IntentKey_OrderProductID";

	String						orderID;
	String						orderProductID;

	ViewGroup					productContainer;
	ImagesGridEditView			imagesGridEditView;
	TextView					textView_orderSn;
	TextView					textView_orderTime;
	TextView					textView_orderState;
	Spinner						spinner_type;
	EditText					editText_description;
	Spinner						spinner_reason;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent() != null) {
			orderID = getIntent().getStringExtra(IntentKey_OrderID);
			orderProductID = getIntent().getStringExtra(IntentKey_OrderProductID);
		}

		if (TextUtils.isEmpty(orderID)) {
			finish();
			return;
		}

		setContentView(R.layout.mall_activity_orderrefund);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews();

		loadOrder();
	}

	void initViews() {
		imagesGridEditView = (ImagesGridEditView) findViewById(R.id.imagesGridEditView);
		productContainer = (ViewGroup) findViewById(R.id.productContainer);
		textView_orderSn = (TextView) findViewById(R.id.textView_orderSn);
		textView_orderState = (TextView) findViewById(R.id.textView_orderState);

		spinner_type = (Spinner) findViewById(R.id.spinner_type);
		spinner_reason = (Spinner) findViewById(R.id.spinner_reason);
		editText_description = (EditText) findViewById(R.id.editText_description);

		RefundType[] typeItems = new RefundType[] { new RefundType("退货", MallOrderRefundType.Refund),
				new RefundType("换货", MallOrderRefundType.Exchange) };
		spinner_type.setAdapter(new ArrayAdapter<RefundType>(getActivity(), android.R.layout.simple_list_item_1,
				typeItems));

		String[] reasonItems = getResources().getStringArray(R.array.mall_refund_reasons);
		spinner_reason.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
				reasonItems));
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (imagesGridEditView.handleForRequestCode(requestCode)) {
			imagesGridEditView.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_refund, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_accept) {
			onSubmitRefund();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean verifyInput() {

		if (TextUtils.isEmpty(editText_description.getText())) {
			Toast.makeText(getActivity(), "退换说明不能为空", Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}

	void onSubmitRefund() {
		if (!verifyInput()) {
			return;
		}

		HGUtils.showConfirmDialog(getActivity(), null, "要提交退/换货申请吗?", new Runnable() {

			@Override
			public void run() {
				submitRefund();
			}
		});
	}

	private void submitRefund() {

		final MallRefundRequest request = new MallRefundRequest();
		request.setID(orderID);
		request.setOrderProductID(orderProductID);
		request.setEdescription(editText_description.getText().toString());
		request.setReason(spinner_reason.getSelectedItem().toString());
		request.setType(((RefundType) spinner_type.getSelectedItem()).type);

		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage("正在处理");

		final List<String> imageFiles = imagesGridEditView.getImageUrls();
		final Thread thread = new Thread() {
			@Override
			public void run() {
				List<String> imageUrls = new ArrayList<String>();
				boolean hasError = false;
				String error = null;
				for (String file : imageFiles) {
					if (isInterrupted()) {
						break;
					}
					String destJPG = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg")
							.getAbsolutePath();
					if (!ImageUtils.scaleImage(file, destJPG, Constant.PICTURE_MIN_SIDE_LENGTH,
							Constant.PICTURE_MAX_SIZE)) {
						hasError = true;
						error = "图片读取错误";
						break;
					}
					if (isInterrupted()) {
						break;
					}
					UploadJson uploadJson = HttpPacketClient.syncUpdaloadFile(destJPG);
					if (!TextUtils.isEmpty(uploadJson.getUrl())) {
						imageUrls.add(uploadJson.getUrl());
					} else {
						hasError = true;
						error = uploadJson.getError();
						break;
					}
				}

				if ((!hasError) && (!isInterrupted())) {
					request.setImageUrls(imageUrls);
					ResponseHandler<MallRefundResponse> responseHandler = new SimpleResponseHandler<MallRefundResponse>();
					HttpPacketClient.postPacketSynchronous(request, MallRefundResponse.class, responseHandler, true);
					if (responseHandler.responseObject == null || !responseHandler.responseObject.isSuccess()) {
						hasError = true;
						if (responseHandler.responseObject != null) {
							error = responseHandler.responseObject.getError();
						} else {
							error = responseHandler.error;
						}
					}
				}

				final boolean finalHasError = hasError;
				final String finalError = error;
				uiHandler.post(new Runnable() {

					@Override
					public void run() {
						dialog.dismiss();
						if (finalHasError) {
							Toast.makeText(getActivity(), finalError, Toast.LENGTH_LONG).show();
						} else {
							Intent intent = new Intent(BroadcastAction_RefundSuccess);
							intent.putExtra(IntentKey_OrderID, orderID);
							sendBroadcast(intent);
							Toast.makeText(getActivity(), "退换货成功", Toast.LENGTH_LONG).show();
							finish();
						}
					}
				});
			}
		};

		thread.start();
		dialog.show();
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				thread.interrupt();
				Toast.makeText(getActivity(), "退换货已取消", Toast.LENGTH_LONG).show();
			}
		});
	}

	void loadOrder() {
		final MallGetOrderRequest request = new MallGetOrderRequest();
		request.setID(orderID);
		ResponseHandler<MallGetOrderResponse> responseHandler = new ResponseHandler<MallGetOrderResponse>() {

			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetOrderResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					updateForOrder(response.getOrder());
					EntityCacheHelper.sharedInstance().saveCacheEntity(MallOrderEntity.class, response.getOrder());
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGetOrderResponse.class, responseHandler);
	}

	public void updateForOrder(MallOrderBean orderBean) {

		if (orderBean == null) {
			return;
		}

		if (hasDestroyed()) {
			return;
		}

//		textView_orderSn.setText("订单编号:" + orderBean.getOrderSn());
		textView_orderSn.setText(orderBean.getShopName());

		if (!HGUtils.isListEmpty(orderBean.getOrderStatus())) {
			MallOrderStatusBean statusBean = orderBean.getOrderStatus().get(0);
			textView_orderState.setText(statusBean.getName());
		} else {
			textView_orderState.setText(null);
		}

		productContainer.removeAllViews();
		if (HGUtils.isListEmpty(orderBean.getOrderProducts())) {
			return;
		}

		for (int count = orderBean.getOrderProducts().size(), i = 0; i < count; i++) {
			MallOrderProductBean product = orderBean.getOrderProducts().get(i);
			if (product.getID().equals(orderProductID)) {
				MallProductOrderCardHolder handler = new MallProductOrderCardHolder(getActivity(), null);
				handler.updateForEntity(product);
				handler.getView().setTag(handler);
				int dip8 = HGUtils.dip2px(getActivity(), 8);
				handler.getView().setPadding(dip8, dip8, dip8, dip8);
				productContainer.addView(handler.detach());
				break;
			}
		}

	}

	class RefundType {
		String	name;
		int		type;

		public RefundType(String name, int type) {
			super();
			this.name = name;
			this.type = type;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}

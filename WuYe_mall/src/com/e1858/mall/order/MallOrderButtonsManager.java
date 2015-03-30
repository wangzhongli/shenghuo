package com.e1858.mall.order;

import java.util.Arrays;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.app.PaymentMethodActivity;
import com.e1858.mall.MallConstant.MallOrderStatus;
import com.e1858.mall.MallConstant.MallRefundStatus;
import com.e1858.mall.entity.MallOrderEntity;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.packet.MallCancelOrderRequest;
import com.e1858.mall.protocol.packet.MallCancelOrderResponse;
import com.e1858.mall.protocol.packet.MallDeleteOrderRequest;
import com.e1858.mall.protocol.packet.MallDeleteOrderResponse;
import com.e1858.mall.protocol.packet.MallGeneratePaymentOrderRequest;
import com.e1858.mall.protocol.packet.MallGeneratePaymentOrderResponse;
import com.e1858.mall.protocol.packet.MallGetOrderRequest;
import com.e1858.mall.protocol.packet.MallGetOrderResponse;
import com.e1858.mall.protocol.packet.MallReceiveOrderRequest;
import com.e1858.mall.protocol.packet.MallReceiveOrderResponse;
import com.e1858.mall.review.MallProductReviewAddActivity;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.mall.R;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.utils.HGUtils;

public class MallOrderButtonsManager implements OnClickListener {
	View				button_pay;
	View				button_receive;
	View				button_cancel;
	View				button_delete;
//	View				button_refund;
	View				button_review;
	View				buttonsBar;
	MallOrderBean		orderBean;

	OnUpdateListener	onUpdateListener;

	public MallOrderButtonsManager(View buttonsBar, View button_pay, View button_receive, View button_cancel,
			View button_delete, View button_review) {

		this.buttonsBar = buttonsBar;
		this.button_cancel = button_cancel;
		this.button_delete = button_delete;
		this.button_pay = button_pay;
		this.button_receive = button_receive;
		this.button_review = button_review;
//		this.button_refund = button_refund;

		button_pay.setOnClickListener(this);
		button_receive.setOnClickListener(this);
		button_cancel.setOnClickListener(this);
		button_delete.setOnClickListener(this);
		button_review.setOnClickListener(this);
	}

	public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
		this.onUpdateListener = onUpdateListener;
	}

	Context getContext() {
		return button_pay.getContext();
	}

	@Override
	public void onClick(View v) {
		if (v == button_pay) {
			onButtonPay();
		} else if (v == button_receive) {
			HGUtils.showConfirmDialog(getContext(), null, "确认收货？", new Runnable() {
				@Override
				public void run() {
					onButtonReceive();
				}
			});
		} else if (v == button_cancel) {
			onButtonCancel();
		} else if (v == button_delete) {
			HGUtils.showConfirmDialog(getContext(), null, "确认删除订单？", new Runnable() {
				@Override
				public void run() {
					onButtonDelete();
				}
			});
//		}else if (v == button_refund) {
//			onButtonRefund();
		} else if (v == button_review) {
			Intent intent = new Intent(getContext(), MallProductReviewAddActivity.class);
			intent.putExtra(MallProductReviewAddActivity.IntentKey_Order, orderBean);
			intent.putExtra(MallProductReviewAddActivity.IntentKey_Title, ((TextView) button_review).getText()
					.toString());
			getContext().startActivity(intent);
		}
	}

	public MallOrderBean getOrderBean() {
		return orderBean;
	}

	public void updateForOrderBean(MallOrderBean orderBean) {
		this.orderBean = orderBean;
		button_pay.setVisibility(View.GONE);
		button_receive.setVisibility(View.GONE);
		button_cancel.setVisibility(View.GONE);
		button_delete.setVisibility(View.GONE);
		button_review.setVisibility(View.GONE);
		buttonsBar.setVisibility(View.GONE);
//		button_refund.setVisibility(View.GONE);
		if (orderBean == null) {
			return;
		}
		int orderStatus = MallOrderStatus.Unknow;
		if (!HGUtils.isListEmpty(orderBean.getOrderStatus())) {
			orderStatus = orderBean.getOrderStatus().get(0).getStatus();
		}
		switch (orderStatus) {
		case MallOrderStatus.WaitingPay:
			button_pay.setVisibility(View.VISIBLE);
			button_cancel.setVisibility(View.VISIBLE);
			buttonsBar.setVisibility(View.VISIBLE);
			break;
		case MallOrderStatus.Paid:
			break;
		case MallOrderStatus.WaitingReceive:
			button_receive.setVisibility(View.VISIBLE);
			buttonsBar.setVisibility(View.VISIBLE);
			break;
		case MallOrderStatus.Done:
			button_delete.setVisibility(View.VISIBLE);
			buttonsBar.setVisibility(View.VISIBLE);
			{
				int productCount = orderBean.getOrderProducts().size();
				int reviewCount = 0, appendReviewCount = 0;
				for (MallOrderProductBean product : orderBean.getOrderProducts()) {
					if (product.getReview() != null) {
						reviewCount++;
						if (product.getReview().getAppendReview() != null) {
							appendReviewCount++;
						}
					}
				}
				if (appendReviewCount < productCount) {
					button_review.setVisibility(View.VISIBLE);
				}
				if (reviewCount < productCount) {
					((TextView) button_review).setText("评价订单");
				} else if (appendReviewCount < productCount) {
					((TextView) button_review).setText("追加评价");
				}
			}
			break;
		case MallOrderStatus.Closed:
			button_delete.setVisibility(View.VISIBLE);
			buttonsBar.setVisibility(View.VISIBLE);
			break;
		}
		for (MallOrderProductBean product : orderBean.getOrderProducts()) {
			if (product.getRefund() != null
					&& (product.getRefund().getStatus() == MallRefundStatus.Exchanging || product.getRefund()
							.getStatus() == MallRefundStatus.Refunding)) {
				button_delete.setVisibility(View.GONE);
				if (orderStatus == MallOrderStatus.Done || orderStatus == MallOrderStatus.Closed) {
					buttonsBar.setVisibility(View.GONE);
				}
				break;
			}
		}
	}

	protected void onButtonDelete() {
		final MallDeleteOrderRequest request = new MallDeleteOrderRequest();
		request.setID(orderBean.getID());
		ResponseHandler<MallDeleteOrderResponse> responseHandler = new ResponseHandler<MallDeleteOrderResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getContext(), "正在删除订单");
			}

			@Override
			public void onFinish(MallDeleteOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					Toast.makeText(getContext(), "订单已删除", Toast.LENGTH_LONG).show();
					if (request.getID().equals(orderBean.getID())) {
						onUpdateListener.onOrderDeleted(orderBean);
					}
					EntityCacheHelper.sharedInstance().deleteEntityByID(orderBean.getID(), MallOrderEntity.class);
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallDeleteOrderResponse.class, responseHandler, true);
	}

	protected void onButtonCancel() {
		final String[] selectedReson = new String[1];
		final String[] reasons = getContext().getResources().getStringArray(R.array.mall_cancel_reasons);
		new AlertDialog.Builder(getContext()).setTitle("请选择取消订单的理由")
				.setSingleChoiceItems(reasons, -1, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						selectedReson[0] = reasons[arg1];
					}
				}).setNegativeButton(getContext().getString(R.string.common_ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						doCancel(selectedReson[0]);
					}
				}).setPositiveButton(getContext().getString(R.string.common_cancel), null).show();
	}

	void doCancel(String reason) {
		MallCancelOrderRequest request = new MallCancelOrderRequest();
		request.setID(orderBean.getID());
		request.setReason(reason);
		ResponseHandler<MallCancelOrderResponse> responseHandler = new ResponseHandler<MallCancelOrderResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getContext(), "正在取消订单");
			}

			@Override
			public void onFinish(MallCancelOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					Toast.makeText(getContext(), "订单已取消", Toast.LENGTH_LONG).show();
					refreshOrder();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallCancelOrderResponse.class, responseHandler, true);

	}

	protected void onButtonReceive() {
		MallReceiveOrderRequest request = new MallReceiveOrderRequest();
		request.setID(orderBean.getID());
		ResponseHandler<MallReceiveOrderResponse> responseHandler = new ResponseHandler<MallReceiveOrderResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getContext(), "正在确认收货");
			}

			@Override
			public void onFinish(MallReceiveOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					Toast.makeText(getContext(), "已确认收货", Toast.LENGTH_LONG).show();
					refreshOrder();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallReceiveOrderResponse.class, responseHandler, true);
	}

	void onButtonPay() {
		MallGeneratePaymentOrderRequest request = new MallGeneratePaymentOrderRequest();
		request.setOrderIDs(Arrays.asList(new String[] { orderBean.getID() }));
		ResponseHandler<MallGeneratePaymentOrderResponse> responseHandler = new ResponseHandler<MallGeneratePaymentOrderResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getContext(), "正在处理");
			}

			@Override
			public void onFinish(MallGeneratePaymentOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					Intent intent = new Intent(getContext(), PaymentMethodActivity.class);
					intent.putExtra(PaymentMethodActivity.IntentKey_TAG, orderBean.getID());
					intent.putExtra(PaymentMethodActivity.IntentKey_Amount,
							orderBean.getAmount() + orderBean.getShippingCost());
					intent.putExtra(PaymentMethodActivity.IntentKey_PaymentParam, response.getPaymentParam());
					getContext().startActivity(intent);
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGeneratePaymentOrderResponse.class, responseHandler, true);
	}

//	private void onButtonRefund() {
//		Intent intent = new Intent(getContext(), MallOrderRefundActivity.class);
//		intent.putExtra(MallOrderRefundActivity.IntentKey_OrderID, orderBean.getID());
//		getContext().startActivity(intent);
//	}

	void refreshOrder() {
		final MallGetOrderRequest request = new MallGetOrderRequest();
		request.setID(orderBean.getID());
		ResponseHandler<MallGetOrderResponse> responseHandler = new ResponseHandler<MallGetOrderResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getContext(), "正在处理");
			}

			@Override
			public void onFinish(MallGetOrderResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (response.getOrder().getID().equals(request.getID())) {
						//保证还是当前的订单
						onUpdateListener.onOrderUpdated(response.getOrder());
					}
					EntityCacheHelper.sharedInstance().saveCacheEntity(MallOrderEntity.class, response.getOrder());
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGetOrderResponse.class, responseHandler, true);
	}

	public interface OnUpdateListener {

		public void onOrderUpdated(MallOrderBean newOrderBean);

		public void onOrderDeleted(MallOrderBean oldOrderBean);
	}
}

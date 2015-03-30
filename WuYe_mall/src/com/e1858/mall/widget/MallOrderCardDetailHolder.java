package com.e1858.mall.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e1858.mall.MallConstant.MallOrderStatus;
import com.e1858.mall.order.MallOrderRefundActivity;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.bean.MallOrderStatusBean;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;

public class MallOrderCardDetailHolder extends MallViewBaseHolder {
	ViewGroup		productGroupView;
	TextView		textView_title;
	TextView		textView_orderSn;
	TextView		textView_orderState;
	TextView		textView_amount;
	TextView		textView_shippingCost;
	TextView		textView_appointmentTime;
	TextView		textView_remark;

	MallOrderBean	orderBean;

	public MallOrderCardDetailHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_order_confirm);
	}

	@Override
	protected void initSubviews() {
		textView_title = (TextView) findViewById(R.id.textView_title);
		textView_orderSn = (TextView) findViewById(R.id.textView_orderSn);
		textView_orderState = (TextView) findViewById(R.id.textView_orderState);
		textView_shippingCost = (TextView) findViewById(R.id.textView_shippingCost);
		textView_appointmentTime = (TextView) findViewById(R.id.textView_appointmentTime);
		textView_remark = (TextView) findViewById(R.id.textView_remark);
		textView_amount = (TextView) findViewById(R.id.textView_amount);
		productGroupView = (ViewGroup) findViewById(R.id.productContainer);
	}

	public void updateForOrder(final MallOrderBean orderBean) {
		this.orderBean = orderBean;
		textView_amount.setText(String.format("合计:¥%.2f", orderBean.getAmount() + orderBean.getShippingCost()));
		textView_shippingCost.setText(String.format("(含运费:¥%.2f)", orderBean.getShippingCost()));
		textView_orderSn.setText(orderBean.getShopName());
		if (TextUtils.isEmpty(orderBean.getRemark())) {
			textView_remark.setVisibility(View.GONE);
		} else {
			textView_remark.setVisibility(View.VISIBLE);
			textView_remark.setText("备注:" + orderBean.getRemark());
		}

		int orderStatus = MallOrderStatus.Unknow;
		if (!HGUtils.isListEmpty(orderBean.getOrderStatus())) {
			MallOrderStatusBean statusBean = orderBean.getOrderStatus().get(0);
			textView_orderState.setText(statusBean.getName());
			orderStatus = statusBean.getStatus();
		} else {
			textView_orderState.setText(null);
		}

		if (TextUtils.isEmpty(orderBean.getAppointmentShippingTime())) {
			textView_appointmentTime.setVisibility(View.GONE);
		} else {
			textView_appointmentTime.setVisibility(View.VISIBLE);
			textView_appointmentTime.setText("配送时间:" + orderBean.getAppointmentShippingTime());
		}

		productGroupView.removeAllViews();
		if (HGUtils.isListEmpty(orderBean.getOrderProducts())) {
			return;
		}

		for (int count = orderBean.getOrderProducts().size(), i = 0; i < count; i++) {
			final MallOrderProductBean product = orderBean.getOrderProducts().get(i);
			MallProductOrderCardHolder handler = new MallProductOrderCardHolder(getContext(), null);
			handler.updateForEntity(product);
			int padding = HGUtils.dip2px(getContext(), 8);
			handler.getView().setPadding(padding, padding, padding, padding);
			productGroupView.addView(handler.detach());
			if (i < count - 1) {
				View.inflate(getContext(), R.layout.mall_sep_h, productGroupView);
			}
			//handler.textView_amount.setVisibility(View.GONE);
			if (orderStatus == MallOrderStatus.Done) {
				handler.button_refund.setVisibility(View.VISIBLE);
			} else {
				handler.button_refund.setVisibility(View.GONE);
			}
			if (product.getRefund() != null) {
				handler.button_refund.setEnabled(false);
				handler.button_refund.setText(product.getRefund().getStatusName());
				handler.button_refund.setOnClickListener(null);
			} else {
				handler.button_refund.setEnabled(true);
				handler.button_refund.setText("退/换货");
				handler.button_refund.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getContext(), MallOrderRefundActivity.class);
						intent.putExtra(MallOrderRefundActivity.IntentKey_OrderID, orderBean.getID());
						intent.putExtra(MallOrderRefundActivity.IntentKey_OrderProductID, product.getID());
						getContext().startActivity(intent);
					}
				});
			}
		}
	}
}

package com.e1858.mall.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e1858.mall.order.MallOrderButtonsManager;
import com.e1858.mall.order.MallOrderButtonsManager.OnUpdateListener;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.bean.MallOrderStatusBean;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;

public class MallOrderCardNormalHolder extends MallViewBaseHolder {
	ViewGroup				productGroupView;
	TextView				textView_orderSn;
	TextView				textView_orderTime;
	TextView				textView_orderState;

	MallOrderButtonsManager	buttonsManager;

	String					appointmentTime;
	MallOrderBean			orderBean;

	public MallOrderCardNormalHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_order_normal);
	}

	@Override
	protected void initSubviews() {
		textView_orderSn = (TextView) findViewById(R.id.textView_orderSn);
		textView_orderTime = (TextView) findViewById(R.id.textView_orderTime);
		textView_orderState = (TextView) findViewById(R.id.textView_orderState);
		productGroupView = (ViewGroup) findViewById(R.id.productContainer);

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

			}
		});
	}

	public void updateForOrder(MallOrderBean orderBean) {
		this.orderBean = orderBean;
		productGroupView.removeAllViews();
		buttonsManager.updateForOrderBean(orderBean);
		textView_orderSn.setText(orderBean.getShopName());
		textView_orderTime.setText(orderBean.getCreateTime());

		if (!HGUtils.isListEmpty(orderBean.getOrderStatus())) {
			MallOrderStatusBean statusBean = orderBean.getOrderStatus().get(0);
			textView_orderState.setText(statusBean.getName());
		} else {
			textView_orderState.setText(null);
		}

		if (HGUtils.isListEmpty(orderBean.getOrderProducts())) {
			return;
		}
		for (int count = orderBean.getOrderProducts().size(), i = 0; i < count; i++) {
			MallOrderProductBean product = orderBean.getOrderProducts().get(i);
			MallProductOrderCardHolder handler = new MallProductOrderCardHolder(getContext(), null);
			handler.updateForEntity(product);
			int padding = HGUtils.dip2px(getContext(), 8);
			handler.getView().setPadding(padding, padding, padding, padding);
			handler.getView().setOnClickListener(null);
			handler.getView().setClickable(false);
			productGroupView.addView(handler.detach());
			if (i < count - 1) {
				View.inflate(getContext(), R.layout.mall_sep_h, productGroupView);
			}
		}
	}

	public MallOrderBean getOrderBean() {
		return orderBean;
	}

}

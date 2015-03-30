package com.e1858.mall.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.e1858.mall.protocol.bean.MallConfirmOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;

public class MallOrderCardConfirmHolder extends MallViewBaseHolder {
	ViewGroup				productGroupView;
	TextView				textView_title;
	TextView				textView_shippingType;
	TextView				textView_shippingCost;
	TextView				textView_appointmentTime;
	EditText				editText_remark;

	String					appointmentTime;
	MallConfirmOrderBean	confirmOrder;

	public MallOrderCardConfirmHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_order_confirm);
	}

	@Override
	protected void initSubviews() {
		textView_title = (TextView) findViewById(R.id.textView_title);
		textView_shippingType = (TextView) findViewById(R.id.textView_shippingType);
		textView_shippingCost = (TextView) findViewById(R.id.textView_shippingCost);
		textView_appointmentTime = (TextView) findViewById(R.id.listitem_appointmentTime).findViewById(R.id.textView);
		editText_remark = (EditText) findViewById(R.id.editText_remark);
		productGroupView = (ViewGroup) findViewById(R.id.productContainer);

		findViewById(R.id.listitem_appointmentTime).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectAppointmentTime();
			}
		});
		updateAppointmentTimeView();
	}

	void updateAppointmentTimeView() {
		if (TextUtils.isEmpty(appointmentTime)) {
			textView_appointmentTime.setText("请选择预约配送时间");
		} else {
			textView_appointmentTime.setText("预约配送时间:" + appointmentTime);
		}
	}

	void selectAppointmentTime() {
		final CharSequence items[] = new CharSequence[9];
		for (int i = 0, clock = 9; i < items.length; i++) {
			items[i] = (String.format("%02d:00-%02d:00", i + clock, i + clock + 1));
		}
		new AlertDialog.Builder(getContext()).setTitle("选择预约配送时间段")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						appointmentTime = items[which].toString();
						updateAppointmentTimeView();
					}
				}).show();
	}

	public String getRemark() {
		return editText_remark.getText().toString();
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public MallConfirmOrderBean getConfirmOrder() {
		return confirmOrder;
	}

	public void updateForConfirmOrder(MallConfirmOrderBean order) {
		textView_title.setText(order.getShopName());
		confirmOrder = order;
		productGroupView.removeAllViews();
		updateShippingCost();
		if (HGUtils.isListEmpty(order.getOrderProducts())) {
			return;
		}
		for (int count = order.getOrderProducts().size(), i = 0; i < count; i++) {
			MallOrderProductBean product = order.getOrderProducts().get(i);
			MallProductOrderCardHolder handler = new MallProductOrderCardHolder(getContext(), null);
			handler.updateForEntity(product);
			int padding = HGUtils.dip2px(getContext(), 8);
			handler.getView().setPadding(padding, padding, padding, padding);
			productGroupView.addView(handler.detach());
			if (i < count - 1) {
				View.inflate(getContext(), R.layout.mall_sep_h, productGroupView);
			}
		}
	}
	
	public void updateShippingCost() {
		textView_shippingCost.setText(String.format("运费¥%.2f", confirmOrder.getShippingCost()));
	}

}

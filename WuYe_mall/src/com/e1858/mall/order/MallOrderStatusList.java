package com.e1858.mall.order;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e1858.mall.protocol.bean.MallOrderStatusBean;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;

public class MallOrderStatusList extends LinearLayout {

	public MallOrderStatusList(Context context) {
		super(context);
	}

	public MallOrderStatusList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void updateForOrderStatusList(List<MallOrderStatusBean> statusList) {
		removeAllViews();
		if (HGUtils.isListEmpty(statusList)) {
			return;
		}
		for (int i = 0, count = statusList.size(); i < count; i++) {
			MallOrderStatusBean status = statusList.get(i);
			View view = View.inflate(getContext(), R.layout.mall_view_orderstatus, null);
			TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
			String name = status.getName();
			if (!TextUtils.isEmpty(status.getEdescription())) {
				name = name + "\n" + status.getEdescription();
			}
			textView_name.setText(name);
			TextView textView_date = (TextView) view.findViewById(R.id.textView_date);
			textView_date.setText(status.getCreateTime());
			ImageView imageView = (ImageView) view.findViewById(R.id.imageView_icon);
			if (i == 0) {
				view.findViewById(R.id.sepTop).setVisibility(View.INVISIBLE);
				textView_name.setTextColor(Color.GREEN);
				textView_date.setTextColor(Color.GREEN);
				imageView.setImageResource(R.drawable.mall_order_status_green);
			} else {
				textView_name.setTextColor(Color.BLACK);
				textView_date.setTextColor(Color.LTGRAY);
				imageView.setImageResource(R.drawable.mall_order_status_gray);
			}

			if (i == count - 1) {
				view.findViewById(R.id.sepBottom_30).setVisibility(View.GONE);
			} else {
				view.findViewById(R.id.sepBottom_0).setVisibility(View.GONE);
			}
			addView(view);
		}
	}

}

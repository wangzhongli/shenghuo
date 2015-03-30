package com.e1858.mall.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.product.MallProductDetailActivity;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.wuye.mall.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallProductOrderCardHolder extends MallViewBaseHolder {

	private MallOrderProductBean	productBean;

	public ImageView				imageView_product;
//	public TextView			textView_introduce;

	public TextView					textView_amount;
	public TextView					textView_title;
//	public TextView			textView_tags;
	public TextView					textView_quantity;
	public TextView					textView_price;

	public TextView					button_refund;

	public MallProductOrderCardHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_product_order);
	}

	@Override
	protected void initSubviews() {
		imageView_product = (ImageView) findViewById(R.id.imageView_product);
		textView_quantity = (TextView) findViewById(R.id.textView_quantity);
		textView_title = (TextView) findViewById(R.id.textView_title);
		textView_price = (TextView) findViewById(R.id.textView_price);
		textView_amount = (TextView) findViewById(R.id.textView_amount);
		button_refund = (TextView) findViewById(R.id.button_refund);

		getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), MallProductDetailActivity.class);
				intent.putExtra(MallProductDetailActivity.IntentKey_ProductID, productBean.getProductID());
				getContext().startActivity(intent);
			}
		});
	}

	public void updateForEntity(MallOrderProductBean productBean) {
		this.productBean = productBean;

		ImageLoader.getInstance().displayImage(productBean.getThumbUrl(), imageView_product,
				BaseApplication.displayImageOptions());
		textView_title.setText(productBean.getName());
		textView_price.setText(String.format("价格:¥%.2f", productBean.getPrice()));
		textView_amount.setText(String.format("¥%.2f", productBean.getPrice() * productBean.getQuantity()));
		textView_quantity.setText("数量:" + productBean.getQuantity());
	}

	public MallOrderProductBean getProductBean() {
		return productBean;
	}

}

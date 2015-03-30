package com.e1858.mall.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.product.MallProductDetailActivity;
import com.e1858.mall.protocol.bean.MallProductBean;
import com.e1858.mall.protocol.bean.MallProductTagBean;
import com.e1858.mall.utils.MallRequestHelper;
import com.e1858.wuye.mall.R;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallProductNormalCardHolder extends MallViewBaseHolder {

	private MallProductBean	productBean;

	public ImageView		imageView_product;
	public TextView			textView_introduce;
	public TextView			textView_title;
	public TextView			textView_tags;
	public TextView			textView_price;
	public TextView			textView_priceBase;

	public MallProductNormalCardHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_product_normal);
	}

	@Override
	protected void initSubviews() {
		imageView_product = (ImageView) findViewById(R.id.imageView_product);
		textView_introduce = (TextView) findViewById(R.id.textView_introduce);
		textView_title = (TextView) findViewById(R.id.textView_title);
		textView_tags = (TextView) findViewById(R.id.textView_tags);
		textView_price = (TextView) findViewById(R.id.textView_price);
		textView_priceBase = (TextView) findViewById(R.id.textView_priceBase);
		textView_priceBase.getPaint().setFlags(textView_priceBase.getPaint().getFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

		findViewById(R.id.button_cart).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MallRequestHelper.add2cart(productBean, (Activity) getContext());
			}
		});

		getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), MallProductDetailActivity.class);
				intent.putExtra(MallProductDetailActivity.IntentKey_ProductID, productBean.getID());
				getContext().startActivity(intent);
			}
		});
	}

	public void updateForEntity(CacheEntityWithSpecifiedId< MallProductBean> entity) {
		MallProductBean bean = entity.getBean();
		this.productBean = bean;

		ImageLoader.getInstance().displayImage(bean.getThumbUrl(), imageView_product,
				BaseApplication.displayImageOptions());
		textView_introduce.setText(bean.getIntroduce());
		textView_title.setText(bean.getName());
		textView_price.setText(String.format("¥%.2f", bean.getPrice()));
		textView_priceBase.setText(String.format("¥%.2f", bean.getPriceBase()));
		StringBuilder tags = new StringBuilder();
		if (bean.getTags() != null) {
			for (MallProductTagBean tag : bean.getTags()) {
				tags.append(tag.getName());
				tags.append(" ");
			}
			if (tags.length() > 0) {
				tags.deleteCharAt(tags.length() - 1);
			}
		}
		textView_tags.setText(tags);

		textView_tags.setVisibility(tags.length() > 0 ? View.VISIBLE : View.GONE);
		textView_introduce.setVisibility(textView_introduce.getText().length() > 0 ? View.VISIBLE : View.INVISIBLE);
	}
}

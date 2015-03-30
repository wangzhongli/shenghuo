package com.e1858.mall.widget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.bean.MallProductReviewBean;
import com.e1858.wuye.mall.R;
import com.hg.android.widget.ImagesGridEditView;
import com.hg.android.widget.ImagesGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallAddProductReviewCardHolder extends MallViewBaseHolder {
	public MallOrderProductBean	productBean;

	//商品
	public ImageView			imageView_product;
	public TextView				textView_title;
	public TextView				textView_price;
	public TextView				textView_quantity;

	//除此评价
	public TextView				textView_firstReviewContent;
	public TextView				textView_firstReviewTime;
	public ImagesGridView		imagesGridView_firstReview;
	public RatingBar			ratingBar_firstReview;

	//进行评价
	public EditText				editText_content;
	public RatingBar			ratingBar_description, ratingBar_quality, ratingBar_speed;
	public ImagesGridEditView	imagesGridEditView;

	public View					ratingBars;
	private View				firstReviewContainer;

	public MallAddProductReviewCardHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_addproductreview);
	}

	@Override
	protected void initSubviews() {
		ratingBars = findViewById(R.id.ratingBars);
		firstReviewContainer = findViewById(R.id.firstReviewContainer);

		imageView_product = (ImageView) findViewById(R.id.imageView_product);
		textView_title = (TextView) findViewById(R.id.textView_title);
		textView_price = (TextView) findViewById(R.id.textView_price);
		textView_quantity = (TextView) findViewById(R.id.textView_quantity);

		textView_firstReviewContent = (TextView) findViewById(R.id.textView_firstReviewContent);
		textView_firstReviewTime = (TextView) findViewById(R.id.textView_firstReviewTime);
		imagesGridView_firstReview = (ImagesGridView) findViewById(R.id.imagesGridView_firstReview);
		ratingBar_firstReview = (RatingBar) findViewById(R.id.ratingBar_firstReview);

		editText_content = (EditText) findViewById(R.id.editText_content);
		ratingBar_description = (RatingBar) findViewById(R.id.ratingBar_description);
		ratingBar_quality = (RatingBar) findViewById(R.id.ratingBar_quality);
		ratingBar_speed = (RatingBar) findViewById(R.id.ratingBar_speed);
		imagesGridEditView = (ImagesGridEditView) findViewById(R.id.imagesGridEditView);
	}

	public void updateForBean(MallOrderProductBean bean) {
		this.productBean = bean;

		ImageLoader.getInstance().displayImage(productBean.getThumbUrl(), imageView_product,
				BaseApplication.displayImageOptions());
		textView_title.setText(productBean.getName());
		textView_price.setText(String.format("价格:¥%.2f", productBean.getPrice()));
		textView_quantity.setText("数量:" + productBean.getQuantity());

		MallProductReviewBean review = productBean.getReview();
		if (review != null) {
			editText_content.setHint("商品如何?可以追加评价哦~");
			ratingBars.setVisibility(View.GONE);
			firstReviewContainer.setVisibility(View.VISIBLE);
			textView_firstReviewContent.setText(review.getContent());
			textView_firstReviewTime.setText(review.getCreateTime());
			ratingBar_firstReview.setRating(review.getRating());
			imagesGridView_firstReview.setImageUrls(review.getPictureUrls());
		} else {
			editText_content.setHint("亲,您的评价对他们有很大的帮助哦!");
			ratingBars.setVisibility(View.VISIBLE);
			firstReviewContainer.setVisibility(View.GONE);
		}
	}
}

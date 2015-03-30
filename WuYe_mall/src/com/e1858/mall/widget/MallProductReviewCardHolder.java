package com.e1858.mall.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.e1858.mall.entity.MallProductReviewEntity;
import com.e1858.mall.protocol.bean.MallProductReviewBean;
import com.e1858.wuye.mall.R;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.hg.android.widget.ImagesGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallProductReviewCardHolder extends MallViewBaseHolder {
	static DisplayImageOptions		options	= new DisplayImageOptions.Builder()
													.showImageOnLoading(R.drawable.head_portrait_icon)
													.showImageForEmptyUri(R.drawable.head_portrait_icon)
													.showImageOnFail(R.drawable.head_portrait_icon).cacheInMemory(true)
													.cacheOnDisk(true).considerExifParams(true)
													.bitmapConfig(Bitmap.Config.RGB_565).build();

	SimpleDateFormat				sdf		= new SimpleDateFormat(HttpDefine.DateFormat);

	private MallProductReviewBean	reviewBean;

	public ImageView				imageView_head;
	public TextView					textView_nickname;
	public TextView					textView_createTime;
	public RatingBar				ratingBar;
	public TextView					textView_content;
	public ImagesGridView			imagesGridView;
	public View						appendContainer;
	public TextView					textView_appendContent;
	public ImagesGridView			imagesGridView_append;

	public MallProductReviewCardHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_productreview);
	}

	@Override
	protected void initSubviews() {
		imageView_head = (ImageView) findViewById(R.id.imageView_head);
		textView_nickname = (TextView) findViewById(R.id.textView_nickname);
		textView_createTime = (TextView) findViewById(R.id.textView_createTime);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		textView_content = (TextView) findViewById(R.id.textView_content);

		imagesGridView = (ImagesGridView) findViewById(R.id.imagesGridView);
		appendContainer = findViewById(R.id.appendContainer);
		textView_appendContent = (TextView) findViewById(R.id.textView_appendContent);
		imagesGridView_append = (ImagesGridView) findViewById(R.id.imagesGridView_append);
	}

	public void updateForEntity(MallProductReviewEntity entity) {
		updateForBean(entity.getBean());
	}

	public void updateForBean(MallProductReviewBean bean) {
		this.reviewBean = bean;

		if (bean.getCreatorID() < 0) {
			textView_nickname.setText("匿名");
			ImageLoader.getInstance().displayImage("", imageView_head, options);
		} else {
			ImageLoader.getInstance().displayImage(bean.getCreatorHeadPortrait(), imageView_head, options);
			textView_nickname.setText(bean.getCreatorName());
		}
		textView_createTime.setText(bean.getCreateTime());
		imagesGridView.setImageUrls(bean.getPictureUrls());
		textView_content.setText(bean.getContent());
		ratingBar.setRating(bean.getRating());

		if (bean.getAppendReview() != null) {
			appendContainer.setVisibility(View.VISIBLE);
			textView_appendContent.setText("[" + formatAppendTime(reviewBean) + "追加]: "
					+ bean.getAppendReview().getContent());
			imagesGridView_append.setImageUrls(bean.getAppendReview().getPictureUrls());
		} else {
			appendContainer.setVisibility(View.GONE);
		}
	}

	String formatAppendTime(MallProductReviewBean bean) {
		try {
			Date firstDate = sdf.parse(reviewBean.getCreateTime());
			Date appendDate = sdf.parse(bean.getAppendReview().getCreateTime());
			long mill = appendDate.getTime() - firstDate.getTime();
			if (mill < DateUtils.MINUTE_IN_MILLIS) {
				return (mill / DateUtils.SECOND_IN_MILLIS) + "秒后";
			} else if (mill < DateUtils.HOUR_IN_MILLIS) {
				return (mill / DateUtils.MINUTE_IN_MILLIS) + "分钟后";
			} else if (mill < DateUtils.DAY_IN_MILLIS) {
				return (mill / DateUtils.HOUR_IN_MILLIS) + "小时后";
			} else {
				return (mill / DateUtils.DAY_IN_MILLIS) + "天后";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}

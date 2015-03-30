package com.e1858.mall.review;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.widget.RatingBar;
import android.widget.TextView;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.wuye.mall.R;

/**
 * 日期: 2015年2月4日 上午10:00:59
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class MallProductReviewSuccessActivity extends BaseActionBarActivity {
	public static final String	IntentKey_Points		= "IntentKey_Points";		//int
	public static final String	IntentKey_Rating		= "IntentKey_Rating";		//float
	public static final String	IntentKey_ProductName	= "IntentKey_ProductName";	//float

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent() == null) {
			finish();
			return;
		}
		int points = getIntent().getIntExtra(IntentKey_Points, 0);
		float rating = getIntent().getFloatExtra(IntentKey_Rating, 0);

		setContentView(R.layout.mall_activity_productreviewsuccess);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		TextView textView_points = (TextView) findViewById(R.id.textView_points);
		TextView textView_productName = (TextView) findViewById(R.id.textView_productName);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

		String text = "评价成功,获得<font color='#f39800'>" + points + "</font>积分";
		textView_points.setText(Html.fromHtml(text));

		textView_productName.setText(getIntent().getStringExtra(IntentKey_ProductName));
		ratingBar.setRating(rating);
	}
}

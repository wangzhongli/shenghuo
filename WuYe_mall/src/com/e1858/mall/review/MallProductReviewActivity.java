package com.e1858.mall.review;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.wuye.mall.R;

/**
 * 日期: 2015年2月4日 上午10:00:59
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class MallProductReviewActivity extends BaseActionBarActivity {
	public static final String	IntentKey_ProductID	= "IntentKey_ProductID";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent() == null) {
			finish();
			return;
		}
		String productID = getIntent().getStringExtra(IntentKey_ProductID);
		if (TextUtils.isEmpty(productID)) {
			finish();
			return;
		}
		setContentView(R.layout.mall_activity_fragment);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		MallProductReviewListFragment fragment = new MallProductReviewListFragment();
		Bundle b = new Bundle();
		b.putString(MallProductReviewListFragment.IntentKey_ProductID, productID);
		fragment.setArguments(b);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment)
				.commitAllowingStateLoss();
	}
}

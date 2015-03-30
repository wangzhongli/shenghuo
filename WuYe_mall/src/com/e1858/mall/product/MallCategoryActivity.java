package com.e1858.mall.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.BaseApplication;
import com.e1858.mall.cart.MallCartActivity;
import com.e1858.mall.protocol.bean.MallProductCategoryBean;
import com.e1858.mall.utils.MallCartBadgeManager;
import com.e1858.wuye.mall.R;

public class MallCategoryActivity extends BaseActionBarActivity {
	public static final String	IntentKey_Category	= "IntentKey_Category";
	MallCartBadgeManager		cartBadgeManager	= new MallCartBadgeManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_activity_category);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews();
	}

	@Override
	public void onDestroy() {
		cartBadgeManager.unregister();
		super.onDestroy();
	}

	void initViews() {
		MallCategoryMajorFragment fragment_major = (MallCategoryMajorFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_major);

		fragment_major.setOnCategoryChangedListener(new OnCategoryChangedListener() {

			@Override
			public void onChanged(MallProductCategoryBean category) {
				MallCategoryMinorFragment fragment_minor = new MallCategoryMinorFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(MallCategoryMinorFragment.IntentKey_Category, category);
				fragment_minor.setArguments(bundle);
				getSupportFragmentManager().beginTransaction().replace(R.id.minorContainer, fragment_minor)
						.commitAllowingStateLoss();
				fragment_minor.setOnCategoryChangedListener(new OnCategoryChangedListener() {
					@Override
					public void onChanged(MallProductCategoryBean category) {
						Intent intent = new Intent(getActivity(), MallProductActivity.class);
						intent.putExtra(MallProductActivity.IntentKey_Category, category);
						getActivity().startActivity(intent);
					}
				});
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_main, menu);
		cartBadgeManager.register(menu.findItem(R.id.mall_action_cart), getActivity());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_cart) {
			if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(getActivity())) {
				startActivity(new Intent(getActivity(), MallCartActivity.class));
			}
			return true;
		}
		if (item.getItemId() == R.id.mall_action_search) {
			startActivity(new Intent(getActivity(), MallProductSearchActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

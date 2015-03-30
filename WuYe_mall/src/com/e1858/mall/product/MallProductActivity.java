package com.e1858.mall.product;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.BaseApplication;
import com.e1858.mall.cart.MallCartActivity;
import com.e1858.mall.protocol.bean.MallProductCategoryBean;
import com.e1858.mall.protocol.packet.MallGetProductsRequest;
import com.e1858.mall.utils.MallCartBadgeManager;
import com.e1858.wuye.mall.R;

public class MallProductActivity extends BaseActionBarActivity {
	public static final String	IntentKey_Category	= "IntentKey_Category";
	public static final String	IntentKey_Keyword	= "IntentKey_Keyword";
	public static final String	IntentKey_TagID		= "IntentKey_TagID";
	public static final String	IntentKey_Title		= "IntentKey_Title";

	MallProductCategoryBean		category;
	String						keyword;
	String						tagID;

	MallCartBadgeManager		cartBadgeManager	= new MallCartBadgeManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_activity_fragment);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		doForIntent(getIntent());
		initTabs();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		doForIntent(intent);
	}

	private void doForIntent(Intent intent) {
		String title = "";
		if (intent != null) {
			category = (MallProductCategoryBean) intent.getSerializableExtra(IntentKey_Category);
			keyword = intent.getStringExtra(IntentKey_Keyword);
			tagID = intent.getStringExtra(IntentKey_TagID);
			title = intent.getStringExtra(IntentKey_Title);
		}
		if (!TextUtils.isEmpty(title)) {
			setTitle(title);
		} else if (!TextUtils.isEmpty(keyword)) {
			setTitle(keyword);
		} else if (category != null) {
			setTitle(category.getName());
		} else {
			setTitle("所有商品");
		}
	}

	void initTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.removeAllTabs();
		List<Pair<String, String>> titles = new ArrayList<Pair<String, String>>();
		final String asc = "asc", desc = "desc", sep = "|";
		titles.add(new Pair<String, String>("saleCount", "销量"));
		titles.add(new Pair<String, String>("price", "价格"));
		titles.add(new Pair<String, String>("createTime", "新品"));

		for (final Pair<String, String> pair : titles) {
			View customView = View.inflate(getActivity(), R.layout.mall_product_tab, null);
			TabHolder holder = new TabHolder();
			holder.imageView = (ImageView) customView.findViewById(R.id.imageView);
			holder.textView = ((TextView) customView.findViewById(R.id.textView));
			holder.textView.setText(pair.second);
			holder.isUp = pair.first.equals("price");//价格从低到高
			holder.imageView.setImageResource(holder.isUp ? R.drawable.mall_arrow_up_red
					: R.drawable.mall_arrow_down_red);
			customView.setTag(holder);

			Tab tab = actionBar.newTab().setCustomView(customView).setTabListener(new ActionBar.TabListener() {
				String	orderBy	= pair.first;

				@Override
				public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
					TabHolder holder = (TabHolder) tab.getCustomView().getTag();
					holder.isUp = !holder.isUp;
					holder.imageView.setImageResource(holder.isUp ? R.drawable.mall_arrow_up_red
							: R.drawable.mall_arrow_down_red);
					onTabSelected(tab, fragmentTransaction);
				}

				@Override
				public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
					TabHolder holder = (TabHolder) tab.getCustomView().getTag();
					MallProductListFragment frag = new MallProductListFragment();
					Bundle b = new Bundle();
					MallGetProductsRequest request = new MallGetProductsRequest();
					request.setTagID(tagID);
					request.setKeyword(keyword);
					request.setOrderby(orderBy + sep + (holder.isUp ? asc : desc));
					if (category != null) {
						request.setCategoryID(category.getID());
					}
					b.putSerializable(MallProductListFragment.IntentKey_Request, request);
					frag.setArguments(b);
					fragmentTransaction.replace(R.id.fragmentContainer, frag);
				}

				@Override
				public void onTabUnselected(Tab arg0, FragmentTransaction fragmentTransaction) {

				}
			});

			actionBar.addTab(tab);
		}

	}

	@Override
	public void onDestroy() {
		cartBadgeManager.unregister();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_product, menu);
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
		return super.onOptionsItemSelected(item);
	}

	class TabHolder {
		ImageView	imageView;
		TextView	textView;
		boolean		isUp;
	}
}

package com.e1858.mall.order;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.mall.MallConstant.MallOrderStatus;
import com.e1858.wuye.mall.R;

public class MallOrderActivity extends BaseActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_activity_fragment);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		initTabs();
	}

	void initTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.removeAllTabs();
		List<Pair<Integer, String>> titles = new ArrayList<Pair<Integer, String>>();
		titles.add(new Pair<Integer, String>(MallOrderStatus.All, "全部"));
		titles.add(new Pair<Integer, String>(MallOrderStatus.WaitingPay, "待付款"));
		titles.add(new Pair<Integer, String>(MallOrderStatus.Paid, "待发货"));
		titles.add(new Pair<Integer, String>(MallOrderStatus.WaitingReceive, "待收货"));
		titles.add(new Pair<Integer, String>(MallOrderStatus.WaitingReview, "待评价"));

		for (final Pair<Integer, String> pair : titles) {
			actionBar.addTab(actionBar.newTab().setText(pair.second).setTabListener(new ActionBar.TabListener() {
				MallOrderListFragment	frag;
				int						status	= pair.first;

				@Override
				public void onTabReselected(Tab arg0, FragmentTransaction fragmentTransaction) {

				}

				@Override
				public void onTabSelected(Tab arg0, FragmentTransaction fragmentTransaction) {
					if (frag == null) {
						frag = new MallOrderListFragment();
						Bundle b = new Bundle();
						b.putInt(MallOrderListFragment.IntentKey_Status, status);
						frag.setArguments(b);
					}
					fragmentTransaction.replace(R.id.fragmentContainer, frag);
				}

				@Override
				public void onTabUnselected(Tab arg0, FragmentTransaction fragmentTransaction) {

				}
			}));
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	void initViews() {

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}

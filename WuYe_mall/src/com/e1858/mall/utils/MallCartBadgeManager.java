package com.e1858.mall.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.mall.MallConstant.PrefKey;
import com.e1858.wuye.mall.R;

public class MallCartBadgeManager {
	OnSharedPreferenceChangeListener	onSharedPreferenceChangeListener;
	TextView							badgeView;

	@SuppressLint("NewApi")
	public void register(final MenuItem menuItem, final Object menuOwner) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return;
		}
		Activity activity = null;
		if (Activity.class.isInstance(menuOwner)) {
			activity = (Activity) menuOwner;
		} else if (Fragment.class.isInstance(menuOwner)) {
			activity = ((Fragment) menuOwner).getActivity();
		}

		menuItem.setActionView(R.layout.hg_menu_badge);
		menuItem.getActionView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Activity.class.isInstance(menuOwner)) {
					((Activity) menuOwner).onOptionsItemSelected(menuItem);
				} else if (Fragment.class.isInstance(menuOwner)) {
					((Fragment) menuOwner).onOptionsItemSelected(menuItem);
				}
			}
		});
		ImageView imageView = (ImageView) menuItem.getActionView().findViewById(R.id.hg_menu_badge_icon);
		imageView.setImageDrawable(menuItem.getIcon());
		badgeView = (TextView) menuItem.getActionView().findViewById(R.id.hg_menu_badge_text);
		badgeView.setBackgroundResource(R.drawable.mall_badge_icon);
		badgeView.setTextColor(activity.getResources().getColor(R.color.wuye_app_bg));
		MallDataPersister.getSharedPreferences().registerOnSharedPreferenceChangeListener(
				getSharedPreferenceChangeListener());
		MallRequestHelper.requestCartProductCount();
		updateBadgeView();
	}

	public void unregister() {
		MallDataPersister.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
				getSharedPreferenceChangeListener());
	}

	OnSharedPreferenceChangeListener getSharedPreferenceChangeListener() {
		if (onSharedPreferenceChangeListener == null) {
			onSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

				@Override
				public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
					if (PrefKey.CartProductCount.equals(key)) {
						updateBadgeView();
					}
				}
			};
		}
		return onSharedPreferenceChangeListener;
	}

	void updateBadgeView() {
		if (badgeView == null) {
			return;
		}
		int count = MallDataPersister.getSharedPreferences().getInt(PrefKey.CartProductCount, 0);
		if (count > 0) {
			badgeView.setText("" + count);
			badgeView.setVisibility(View.VISIBLE);
		} else {
			badgeView.setVisibility(View.GONE);
		}
	}
}

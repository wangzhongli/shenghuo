package com.e1858.wuye.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.e1858.common.Constant;
import com.e1858.wuye.R;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * 引导页
 * 
 * @author lhg
 */
public class SplashActivity extends BaseActivity {
	private ViewPager			mViewPager;
	private CirclePageIndicator	indicator;
	private Button				jump_btn, next_btn;
	private SharedPreferences	sp;
	private int					vercode;
	private MyAdapter			myAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_page);
		judgeVersion();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(myAdapter = new MyAdapter());
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);
		jump_btn = (Button) findViewById(R.id.jump_btn);
		next_btn = (Button) findViewById(R.id.startBtn);
		MyOnPageChangeListener changeListener = new MyOnPageChangeListener();
		indicator.setOnPageChangeListener(changeListener);
		mViewPager.setCurrentItem(0);
		changeListener.onPageSelected(0);
	}

	class MyAdapter extends PagerAdapter {
		int[]	images	= { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4,
								R.drawable.guide_5, R.drawable.guide_6 };

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageView view = (ImageView) object;
			container.removeView(view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(SplashActivity.this);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setImageResource(images[position]);
			container.addView(imageView);
			return imageView;
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			if (position == myAdapter.getCount() - 1) {
				jump_btn.setVisibility(View.GONE);
				next_btn.setVisibility(View.VISIBLE);
			} else {
				jump_btn.setVisibility(View.VISIBLE);
				next_btn.setVisibility(View.GONE);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

	public void startbutton(View v) {
		Editor editor = sp.edit();
		editor.putInt(Constant.PIC_REMARK, vercode);
		editor.commit();
		Intent intent = new Intent();
		intent.setClass(SplashActivity.this, LocationCommunityActivity.class);
		startActivity(intent);
		this.finish();
	}

	private void judgeVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			vercode = info.versionCode;
			sp = getSharedPreferences("guide", Context.MODE_PRIVATE);
			if (sp.getInt(Constant.PIC_REMARK, 0) == vercode) {
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, LocationCommunityActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				initView();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}

package com.e1858.fragment;

import java.lang.reflect.Field;
import com.e1858.common.Constant;
import com.e1858.fragment.ConvenienceFragment;
import com.e1858.widget.PagerStrip;
import com.e1858.wuye.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 左右滑动管理ConvenienceFragment
 * @author jiajia 2014年8月22日
 *
 */
public class PagerStripFragment extends Fragment {

	public static final String TAG = PagerStripFragment.class
			.getSimpleName();
	public int currentposition=0;
	
	public static PagerStripFragment newInstance() {
		return new PagerStripFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setRetainInstance(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.convenience_pager, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		PagerStrip tabs = (PagerStrip) view
				.findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		pager.setCurrentItem(currentposition);
		tabs.setViewPager(pager);

	}

	private ConvenienceFragment one;
	private ConvenienceFragment two;
	private ConvenienceFragment three;

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		private final String[] TITLES = {Constant.CONVENIENCE_TYPE.ONE_TITLE,Constant.CONVENIENCE_TYPE.TWO_TITLE,Constant.CONVENIENCE_TYPE.THREE_TITLE};

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fm = null;
			switch (position) {
			case 0:
				if (one == null) {
					one = ConvenienceFragment.newInstance(Constant.CONVENIENCE_TYPE.ONE);
				}
				fm = one;
				break;
			case 1:
				if (two == null) {
					two = ConvenienceFragment.newInstance(Constant.CONVENIENCE_TYPE.TWO);
				}
				fm = two;
				break;
			case 2:
				if (three == null) {
					three = ConvenienceFragment.newInstance(Constant.CONVENIENCE_TYPE.THREE);
				}
				fm = three;
				break;
			default:
				break;
			}
			return fm;
		}

	}

	public int getCurrentposition() {
		return currentposition;
	}

	public void setCurrentposition(int currentposition) {
		this.currentposition = currentposition;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}

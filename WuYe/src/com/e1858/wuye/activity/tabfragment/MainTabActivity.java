package com.e1858.wuye.activity.tabfragment;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.e1858.common.app.AppManager;
import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.mall.main.MallMainFragment;
import com.e1858.utils.Exit;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 底部导航
 */
public class MainTabActivity extends BaseActionBarActivity {

	public static final String			IntentKey_TabButtonID	= "IntentKey_TabButtonID";			//int [0,4]

	private Exit						exit					= new Exit();

	private RadioGroup					radioGroup;

//	private DrawerLayout				mDrawerLayout;
//	private ActionBarDrawerToggle		mDrawerToggle;
//	private MallCategoryFragment		mallLeftDrawerFragment;
	private Fragment					contentFragment;

	private HashMap<Integer, Fragment>	fragmentMap				= new HashMap<Integer, Fragment>();

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MobclickAgent.updateOnlineConfig(this);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();

		setContentView(R.layout.main_tab_fragment);
		initView(savedInstanceState);
		onNewIntent(getIntent());
	}

	@Override
	public void onNewIntent(Intent intent) {
		if (intent == null) {
			return;
		}
		int tabButtonID = intent.getIntExtra(IntentKey_TabButtonID, R.id.radio_button_home);
//		if (tabButtonID == R.id.radio_button_home) {
//			CheckVersionDialogManager.checkVersion(this, handler);
//		}
		radioGroup.check(tabButtonID);
	}

//	private void initDrawerForMall(boolean show) {
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(show);
//		actionBar.setHomeButtonEnabled(show);
//
//		if (mDrawerLayout == null) {
//			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
//			mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
//			mDrawerLayout, /* DrawerLayout object */
//			R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
//			R.string.drawer_open, /* "open drawer" description for accessibility */
//			R.string.drawer_close /* "close drawer" description for accessibility */
//			) {
//				@Override
//				public void onDrawerClosed(View view) {
//					MainTabActivity.this.setTitle("商城");
//				}
//
//				@Override
//				public void onDrawerOpened(View drawerView) {
//					MainTabActivity.this.setTitle("商品分类");
//				}
//			};
//			mDrawerLayout.setDrawerListener(mDrawerToggle);
//			mDrawerToggle.syncState();
//		}
//
//		if (!show) {
//			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//			if (mallLeftDrawerFragment != null && mallLeftDrawerFragment.isAdded()) {
//				getSupportFragmentManager().beginTransaction().remove(mallLeftDrawerFragment).commitAllowingStateLoss();
//			}
//			return;
//		}
//		if (mallLeftDrawerFragment == null) {
//			mallLeftDrawerFragment = new MallCategoryFragment();
//			mallLeftDrawerFragment.setOnCategoryChangedListener(new OnCategoryChangedListener() {
//				@Override
//				public void onChanged(MallProductCategoryBean category) {
//					mDrawerLayout.closeDrawer(GravityCompat.START);
//				}
//			});
//		}
//		mallLeftDrawerFragment.loadCategories();
//		FragmentManager manager = getSupportFragmentManager();
//		if (manager.findFragmentByTag("MallCategoryFragment") == null) {
//			manager.beginTransaction()
//					.replace(R.id.leftDrawerContainer, mallLeftDrawerFragment, "MallCategoryFragment")
//					.commitAllowingStateLoss();
//		}
//		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
		return false;
	}

	@Override
	public void onBackPressed() {
		if (radioGroup.getCheckedRadioButtonId() == R.id.radio_button_home) {
			if (exit.isExit()) {
				AppManager.getAppManager().AppExit(getApplicationContext());
			} else {
				ToastUtil.show(getApplicationContext(), "再按一次返回键回到桌面");
				exit.doExitInOneSecond();
			}
		} else {
			exit.setExit(false);
			radioGroup.check(R.id.radio_button_home);
		}
	}

	void setTitle(String title) {
		super.setTitle(title);
		getSupportActionBar().setTitle(title);
	}

	/**
	 * 初始化组件
	 */
	private void initView(Bundle savedInstanceState) {
		//实例化布局对象  
		radioGroup = (RadioGroup) findViewById(R.id.main_radio);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				Class<? extends Fragment> fragmentClass = null;
				switch (id) {
				case R.id.radio_button_home:
					setTitle(PreferencesUtils.getCommunity().getName());
					fragmentClass = HomeActivity.class;
					break;
				case R.id.radio_button_wuye:
					setTitle("物业服务");
					fragmentClass = WuYeServiceActivity.class;
					break;
//				case R.id.radio_button_service:
//					setTitle("便民服务");
//					fragmentClass = ConvenienceActivity.class;
//					break;
				case R.id.radio_button_mall:
					setTitle("百姓特供");
					fragmentClass = MallMainFragment.class;
					break;
				case R.id.radio_button_personal:
					setTitle("个人中心");
					fragmentClass = PersonalActivity.class;
					break;
				}
				if (contentFragment == null || !fragmentClass.isInstance(contentFragment)) {
					Fragment fragment = fragmentMap.get(id);
					if (fragment == null) {
						try {
							fragment = fragmentClass.newInstance();
							if (id == R.id.radio_button_home || id == R.id.radio_button_mall) {//暂时只缓存首页吧
								fragmentMap.put(id, fragment);
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					contentFragment = fragment;
					getSupportFragmentManager().beginTransaction().replace(R.id.realtabcontent, contentFragment)
							.commitAllowingStateLoss();
//					initDrawerForMall(id == R.id.radio_button_mall);
				}

			}
		});
	}
}

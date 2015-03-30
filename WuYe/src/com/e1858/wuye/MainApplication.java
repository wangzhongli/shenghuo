package com.e1858.wuye;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.widget.Toast;

import com.e1858.common.Constant;
import com.e1858.common.Constant.BannerHost;
import com.e1858.common.DBManager;
import com.e1858.common.app.BaseApplication;
import com.e1858.common.app.WebViewActivity;
import com.e1858.entity.PointRecordEntity;
import com.e1858.mall.entity.MallCartShopEntity;
import com.e1858.mall.entity.MallManagedAddressEntity;
import com.e1858.mall.entity.MallMyRecommendEntity;
import com.e1858.mall.entity.MallOrderEntity;
import com.e1858.mall.entity.MallPersonRecommendEntity;
import com.e1858.mall.entity.MallProductEntity;
import com.e1858.mall.entity.MallProductReviewEntity;
import com.e1858.mall.product.MallProductDetailActivity;
import com.e1858.mall.recommend.MallPersonRecommActivity;
import com.e1858.monitor.NetworkMonitor;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.SDCardUtil;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.wuye.activity.LoginActivity;
import com.e1858.wuye.activity.tabfragment.MainTabActivity;
import com.e1858.wuye.protocol.http.BannerBean;
import com.e1858.wuye.protocol.http.ConvenientType;
import com.e1858.wuye.protocol.http.Notice;
import com.hg.android.entitycache.EntityCacheHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainApplication extends BaseApplication {
	@SuppressWarnings("unused")
	private static final String		TAG					= "MainApplication";
	private NetworkMonitor			networkMonitor;
	private Handler					currentHandler;

//	private String key="";//是否登录验证
//	private String token="";//验证token
	private boolean					networkAvailable	= true;
	private boolean					loginOK				= false;
	private Notice					notice;
	private DBManager				dbManager;
	private List<String>			bitmaps;
	private static MainApplication	mInstance			= null;
	private List<ConvenientType>	convenientTypes;
	private String					cityName			= "";
	private Boolean					isCommunityChanged	= false;
	private Boolean					isConvienceChanged	= false;
	private Boolean					isRefresh			= false;

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		EntityCacheHelper.initInstance(this, SqliteOpenHelper.class);

		SqliteOpenHelper.registEntity(MallProductEntity.class);
		SqliteOpenHelper.registEntity(MallCartShopEntity.class);
		SqliteOpenHelper.registEntity(MallManagedAddressEntity.class);
		SqliteOpenHelper.registEntity(MallOrderEntity.class);
		SqliteOpenHelper.registEntity(MallMyRecommendEntity.class);
		SqliteOpenHelper.registEntity(MallPersonRecommendEntity.class);
		SqliteOpenHelper.registEntity(PointRecordEntity.class);
		SqliteOpenHelper.registEntity(MallProductReviewEntity.class);

		//Util.saveStatus(this, getSharedPreferences(Constant.USER_INFO, Context.MODE_PRIVATE));
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		super.onCreate();

		HttpPacketClient.getInstaince().setApiServer(Constant.BASE_URL);
		HttpPacketClient.getInstaince().setFileServer(Constant.UPLOAD_URL);

		mInstance = this;
		init();
		if (null == networkMonitor) {
			networkMonitor = new NetworkMonitor(this);
			this.getApplicationContext().registerReceiver(networkMonitor,
					new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		}
		if (null == dbManager) {
			dbManager = new DBManager(this);
		}

		initImageLoader(getApplicationContext());
		catchException();
	}

	/**
	 * 获取应用实例
	 * 
	 * @return
	 */
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO) // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

	public static MainApplication getInstance() {
		return mInstance;
	}

	private void init() {
		if (SDCardUtil.externalMemoryAvailable()) {
			Constant.ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
					+ Constant.ROOT_NAME + File.separator;
			Constant.CRASH_PATH = Constant.ROOT_PATH + "error/";
			Constant.DATABASE_PATH = Constant.ROOT_PATH + "database/";
			Constant.CACHE_PATH = Constant.ROOT_PATH + "cache/";
		}
	}

	/**
	 * 捕获异常
	 */
	private void catchException() {
		//使用友盟的错误统计
//		CrashHandlerException crashHandler = CrashHandlerException.getInstance();
//		crashHandler.init(getApplicationContext());
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		if (null != networkMonitor) {
			this.getApplicationContext().unregisterReceiver(networkMonitor);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public Handler getCurrentHandler() {
		return currentHandler;
	}

	public void setCurrentHandler(Handler handler) {
		this.currentHandler = handler;
	}

	@Override
	public boolean isNetworkAvailable() {
		return networkAvailable;
	}

	public void setNetworkAvailable(boolean networkAvailable) {
		this.networkAvailable = networkAvailable;
	}

	public boolean isLoginOK() {
		return loginOK;
	}

	public void setLoginOK(boolean loginOK) {
		this.loginOK = loginOK;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public DBManager getDbManager() {
		return dbManager;
	}

	public void setDbManager(DBManager dbManager) {
		this.dbManager = dbManager;
	}

	public List<String> getBitmaps() {
		return bitmaps;
	}

	public void setBitmaps(List<String> bitmaps) {
		this.bitmaps = bitmaps;
	}

	public List<ConvenientType> getConvenientTypes() {
		return convenientTypes;
	}

	public void setConvenientTypes(List<ConvenientType> convenientTypes) {
		this.convenientTypes = convenientTypes;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Boolean getIsCommunityChanged() {
		return isCommunityChanged;
	}

	public void setIsCommunityChanged(Boolean isCommunityChanged) {
		this.isCommunityChanged = isCommunityChanged;
	}

	public Boolean getIsConvienceChanged() {
		return isConvienceChanged;
	}

	public void setIsConvienceChanged(Boolean isConvienceChanged) {
		this.isConvienceChanged = isConvienceChanged;
	}

	public Boolean getIsRefresh() {
		return isRefresh;
	}

	public void setIsRefresh(Boolean isRefresh) {
		this.isRefresh = isRefresh;
	}

	@Override
	public boolean verifyLoggedInAndGoToLogin(Activity activity) {
		if (PreferencesUtils.getUserID() > 0) {
			return true;
		} else {
			Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			activity.startActivity(intent);
			return false;
		}
	}

	@Override
	public boolean judgeForBannerUrl(Activity activity, BannerBean banner) {
		if (banner == null || TextUtils.isEmpty(banner.getUrl())) {
			return false;
		}
		try {
			URI uri = new URI(banner.getUrl());
			String scheme = uri.getScheme();
			if (scheme.equals("app")) {
				String host = uri.getHost();
				if (host.startsWith(BannerHost.Mall)) {
					if (TextUtils.isEmpty(uri.getPath())) {
						Intent intent = new Intent(activity, MainTabActivity.class);
						intent.putExtra(MainTabActivity.IntentKey_TabButtonID, R.id.radio_button_mall);
						activity.startActivity(intent);
					} else {
						Intent intent = new Intent(activity, MallProductDetailActivity.class);
						intent.putExtra(MallProductDetailActivity.IntentKey_ProductID, uri.getPath().substring(1));
						activity.startActivity(intent);
					}
				} else if (host.startsWith(BannerHost.Recommend)) {
					activity.startActivity(new Intent(activity, MallPersonRecommActivity.class));
				}
				return true;
			} else if (scheme.equals("http") || scheme.equals("https")) {
				Intent intent = new Intent(activity, WebViewActivity.class);
				intent.putExtra(WebViewActivity.IntentKey_Title, banner.getName());
				intent.putExtra(WebViewActivity.IntentKey_URL, banner.getUrl());
				activity.startActivity(intent);
				return true;
			}
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}
}

package com.e1858.mall.recommend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.BaseApplication;
import com.e1858.common.widget.CircularImage;
import com.e1858.mall.MallConstant;
import com.e1858.mall.entity.MallMyRecommendEntity;
import com.e1858.mall.protocol.packet.MallGetMyRecommendRequest;
import com.e1858.mall.protocol.packet.MallGetMyRecommendResp;
import com.e1858.mall.widget.MallMyRecommedHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.wuye.mall.R;
import com.e1858.wuye.protocol.http.UserInfo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallMyRecommendActivity extends BaseActionBarActivity {
	PullToRefreshListView			listView;
	private Context					context;
	protected BaseApplication		application;
	private MyAdapter				adapter;
	MallGetMyRecommendResp			mResponse;
	protected DisplayImageOptions	options;
	private TextView				mall_recommend_head_tv_heartnum;
	private TextView				mall_recommend_head_tv_commnum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_pulltorefresh_listview);
		context = getActivity();
		application = BaseApplication.getBaseInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.head_portrait_icon)
				.showImageForEmptyUri(R.drawable.head_portrait_icon).showImageOnFail(R.drawable.head_portrait_icon)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		initViews();
	}

	public void initViews() {
		View headView = initHeadView();
		listView = (PullToRefreshListView) findViewById(R.id.listView);
		listView.setMode(Mode.BOTH);
		listView.getRefreshableView().addHeaderView(headView);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadRecommeds(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadRecommeds(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		adapter = new MyAdapter(context);
		listView.setAdapter(adapter);
		adapter.load(getSupportLoaderManager(), SqliteOpenHelper.class, MallMyRecommendEntity.class);
	}

	/**
	 * 初始化headview
	 * 
	 * @return
	 */
	private View initHeadView() {
		View headView = View.inflate(context, R.layout.mall_recommend_head_item, null);
		CircularImage head_portrait = (CircularImage) headView.findViewById(R.id.head_portrait);
		TextView mall_recommend_head_tv_nickname = (TextView) headView
				.findViewById(R.id.mall_recommend_head_tv_nickname);
		TextView mall_recommend_head_tv_sign = (TextView) headView.findViewById(R.id.mall_recommend_head_tv_sign);
		mall_recommend_head_tv_heartnum = (TextView) headView.findViewById(R.id.mall_recommend_head_tv_heartnum);
		mall_recommend_head_tv_commnum = (TextView) headView.findViewById(R.id.mall_recommend_head_tv_commnum);

		UserInfo userInfo = PreferencesUtils.getUserInfo();
		if (userInfo != null) {
			ImageLoader.getInstance().displayImage(userInfo.getHeadPortrait(), head_portrait, options);
			mall_recommend_head_tv_nickname.setText(userInfo.getNickname() == null ? "" : userInfo.getNickname());

//		if (null != userHouse) {
////			mall_recommend_head_tv_nickname.setText(userInfo.getNickname() + "/" + "暂无楼号信息");
//			mall_recommend_head_tv_nickname.setText(userInfo.getNickname());
//		} else {
////			mall_recommend_head_tv_nickname
////			.setText(userInfo.getNickname() + "/" + userHouse.getHouse().getName() == null ? "暂无楼号信息"
////					: userHouse.getHouse().getName());
//		}
			//TODO 这里要显示签名，暂时没有签名，就显示名字
			mall_recommend_head_tv_sign.setText(userInfo.getName());
		}
		return headView;
	}

	@Override
	public void onResume() {
		loadRecommeds(0);
		super.onResume();
	}

	protected void loadRecommeds(final int offset) {

		ResponseHandler<MallGetMyRecommendResp> responseHandler = new ResponseHandler<MallGetMyRecommendResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetMyRecommendResp response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					mResponse = response;
					mall_recommend_head_tv_heartnum.setText(mResponse.getHeartNumToMe() + "");
					mall_recommend_head_tv_commnum.setText(mResponse.getComNumToMe() + "");
					EntityCacheHelper.sharedInstance().saveCacheEntities(MallMyRecommendEntity.class,
							response.getRecommends(), offset);
				}
			}
		};

		MallGetMyRecommendRequest request = new MallGetMyRecommendRequest();
		request.setOffset(offset);
		request.setCount(MallConstant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, MallGetMyRecommendResp.class, responseHandler, true);
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		super.onDestroy();
	}

	@Override
	public BaseActionBarActivity getActivity() {
		return super.getActivity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_address, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_new) {
			Intent intent = new Intent(context, MallAddNewRecommActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class MyAdapter extends OrmLiteIteratorAdapterExt<MallMyRecommendEntity> {

		public MyAdapter(Context context) {
			super(context);

		}

		@Override
		public void buildQueryBuilder(QueryBuilder<MallMyRecommendEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@Override
		public View newView(Context context, MallMyRecommendEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.mall_recommend_item, null);
			MallMyRecommedHolder holder = new MallMyRecommedHolder(context, view);
			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, MallMyRecommendEntity entity, int position) {
			MallMyRecommedHolder holder = (MallMyRecommedHolder) view.getTag();
			holder.updateView(entity.getBean());
		}

	}

}

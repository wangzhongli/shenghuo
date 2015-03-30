package com.e1858.mall.recommend;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.BaseApplication;
import com.e1858.mall.MallConstant;
import com.e1858.mall.entity.MallPersonRecommendEntity;
import com.e1858.mall.protocol.packet.MallGetPersonRecommendRequest;
import com.e1858.mall.protocol.packet.MallGetPersonRecommendResp;
import com.e1858.mall.widget.MallPersonRecommedHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.wuye.mall.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;

public class MallPersonRecommActivity extends BaseActionBarActivity {
	PullToRefreshListView		listView;
	private Context				context;
	private static MyAdapter	adapter;
	public List<String>			hearteds;

	public static void action(Context context) {
		Intent intent = new Intent(context, MallPersonRecommActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		setContentView(R.layout.mall_pulltorefresh_listview);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		hearteds = new ArrayList<String>();
		initViews();
	}

	@Override
	public void onResume() {
		loadRecommeds(0);
		super.onResume();
	}

	public void initViews() {

		listView = (PullToRefreshListView) findViewById(R.id.listView);
		listView.setMode(Mode.BOTH);
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
		adapter.load(getSupportLoaderManager(), SqliteOpenHelper.class, MallPersonRecommendEntity.class);
	}

	protected void loadRecommeds(final int offset) {

		ResponseHandler<MallGetPersonRecommendResp> responseHandler = new ResponseHandler<MallGetPersonRecommendResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetPersonRecommendResp response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(MallPersonRecommendEntity.class,
							response.getMallRecommends(), offset);
					hearteds.addAll(response.getMyClickHearts());
				}
			}
		};

		MallGetPersonRecommendRequest request = new MallGetPersonRecommendRequest();
		request.setOffset(offset);
		request.setCount(MallConstant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, MallGetPersonRecommendResp.class, responseHandler, true);
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		hearteds.clear();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_new) {
			if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(this)) {
				Intent intent = new Intent(context, MallAddNewRecommActivity.class);
				startActivity(intent);
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean hasDestroyed() {
		return super.hasDestroyed();
	}

	@Override
	public void finish() {
		super.finish();
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

	class MyAdapter extends OrmLiteIteratorAdapterExt<MallPersonRecommendEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<MallPersonRecommendEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}

		@Override
		public View newView(Context context, MallPersonRecommendEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.person_recomm_item, null);
			MallPersonRecommedHolder holder = new MallPersonRecommedHolder(context, view);
			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, MallPersonRecommendEntity entity, int position) {
			MallPersonRecommedHolder holder = (MallPersonRecommedHolder) view.getTag();
			holder.updateView(entity.getBean(), hearteds, MallPersonRecommActivity.this);
		}

	}

	public static void notifi() {
		adapter.notifyDataSetChanged();
	}

}

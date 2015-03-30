package com.e1858.mall.review;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.e1858.mall.MallConstant;
import com.e1858.mall.MallPullRefreshListFragment;
import com.e1858.mall.entity.MallProductReviewEntity;
import com.e1858.mall.protocol.packet.MallGetProductReviewsRequest;
import com.e1858.mall.protocol.packet.MallGetProductReviewsResponse;
import com.e1858.mall.widget.MallProductReviewCardHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.wuye.mall.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.hg.android.utils.HGUtils;
import com.j256.ormlite.stmt.QueryBuilder;

public class MallProductReviewListFragment extends MallPullRefreshListFragment {
	public static final String	IntentKey_ProductID	= "IntentKey_ProductID";

	MyAdapter					adapter;
	String						productID;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getArguments() != null) {
			productID = getArguments().getString(IntentKey_ProductID);
		}

		initViews();
		loadProductReviews(0);
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		EntityCacheHelper.sharedInstance().deleteAllEntities(MallProductReviewEntity.class);
		super.onDestroy();
	}

	void initViews() {
		listView.setBackgroundColor(Color.WHITE);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadProductReviews(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadProductReviews(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.setAdapter(adapter = new MyAdapter(getActivity()));
		adapter.load(getLoaderManager(), SqliteOpenHelper.class, MallProductReviewEntity.class);

		setEmptyText("该商品暂无评价");
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	protected void loadProductReviews(final int offset) {

		ResponseHandler<MallGetProductReviewsResponse> responseHandler = new ResponseHandler<MallGetProductReviewsResponse>() {
			@Override
			public void onStart() {
				updateEmptyStatus(true, false);
			}

			@Override
			public void onFinish(MallGetProductReviewsResponse response, String error) {
				updateEmptyStatus(false, response == null || HGUtils.isListEmpty(response.getReviews()));
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(MallProductReviewEntity.class,
							response.getReviews(), offset);
					Activity activity = getActivity();
					if (activity != null) {
						activity.setTitle("商品评价(" + response.getTotalCount() + ")");
					}
				}
			}
		};

		MallGetProductReviewsRequest request = new MallGetProductReviewsRequest();
		request.setOffset(offset);
		request.setCount(MallConstant.FetchCount);
		request.setProductID(productID);
		HttpPacketClient.postPacketAsynchronous(request, MallGetProductReviewsResponse.class, responseHandler, true);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<MallProductReviewEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, MallProductReviewEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.mall_listitem_productreview, null);
			view.setTag(new MallProductReviewCardHolder(context, view.findViewById(R.id.productReviewCardView)));
			return view;
		}

		@Override
		public void bindView(View view, Context context, MallProductReviewEntity entity, int position) {
			MallProductReviewCardHolder handler = (MallProductReviewCardHolder) view.getTag();
			handler.updateForEntity(entity);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<MallProductReviewEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}
	}
}

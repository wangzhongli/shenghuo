package com.e1858.mall.product;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.e1858.mall.MallConstant;
import com.e1858.mall.MallPullRefreshListFragment;
import com.e1858.mall.entity.MallProductEntity;
import com.e1858.mall.protocol.packet.MallGetProductsRequest;
import com.e1858.mall.protocol.packet.MallGetProductsResponse;
import com.e1858.mall.widget.MallProductNormalCardHolder;
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

public class MallProductListFragment extends MallPullRefreshListFragment {
	public static final String	IntentKey_Request	= "IntentKey_Request";

	MyAdapter					adapter;
	MallGetProductsRequest		getProductsRequest;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getArguments() != null) {
			getProductsRequest = (MallGetProductsRequest) getArguments().getSerializable(IntentKey_Request);
		}
		EntityCacheHelper.sharedInstance().deleteAllEntities(MallProductEntity.class);
		initViews();
		loadProducts(0);
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		EntityCacheHelper.sharedInstance().deleteAllEntities(MallProductEntity.class);
		super.onDestroy();
	}

	void initViews() {
		listView.getRefreshableView().setDividerHeight(0);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadProducts(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadProducts(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.setAdapter(adapter = new MyAdapter(getActivity()));
		adapter.load(getLoaderManager(), SqliteOpenHelper.class, MallProductEntity.class);

		setEmptyText("没有商品");
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	protected void loadProducts(final int offset) {

		ResponseHandler<MallGetProductsResponse> responseHandler = new ResponseHandler<MallGetProductsResponse>() {
			@Override
			public void onStart() {
				updateEmptyStatus(true, false);
			}

			@Override
			public void onFinish(MallGetProductsResponse response, String error) {
				updateEmptyStatus(false, response == null || HGUtils.isListEmpty(response.getProducts()));
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(MallProductEntity.class,
							response.getProducts(), offset);
				}
			}
		};

		getProductsRequest.setOffset(offset);
		getProductsRequest.setCount(MallConstant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(getProductsRequest, MallGetProductsResponse.class, responseHandler,
				true);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<MallProductEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, MallProductEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.mall_listitem_product, null);
			view.setTag(new MallProductNormalCardHolder(context, view.findViewById(R.id.normalProductCardView)));
			return view;
		}

		@Override
		public void bindView(View view, Context context, MallProductEntity entity, int position) {
			MallProductNormalCardHolder handler = (MallProductNormalCardHolder) view.getTag();
			handler.updateForEntity(entity);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<MallProductEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}
	}
}

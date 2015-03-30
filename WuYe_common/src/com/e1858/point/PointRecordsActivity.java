package com.e1858.point;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.common.R;
import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.entity.PointRecordEntity;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.wuye.protocol.http.PointGetRecordsRequest;
import com.e1858.wuye.protocol.http.PointGetRecordsResp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;

public class PointRecordsActivity extends BaseActionBarActivity {

	MyAdapter				adapter;
	PullToRefreshListView	listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_point_records);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews();
		loadRecords(0);
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		super.onDestroy();
	}

	void initViews() {
		listView = (PullToRefreshListView) findViewById(R.id.listView);
		listView.setMode(Mode.BOTH);
		listView.getRefreshableView().setDividerHeight(0);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadRecords(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadRecords(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.setAdapter(adapter = new MyAdapter(this));
		adapter.load(getSupportLoaderManager(), SqliteOpenHelper.class, PointRecordEntity.class);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	protected void loadRecords(final int offset) {

		ResponseHandler<PointGetRecordsResp> responseHandler = new ResponseHandler<PointGetRecordsResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(PointGetRecordsResp response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(PointRecordEntity.class,
							response.getRecords(), offset);
				}
			}
		};

		PointGetRecordsRequest request = new PointGetRecordsRequest();
		request.setOffset(offset);
		request.setCount(10);
		HttpPacketClient.postPacketAsynchronous(request, PointGetRecordsResp.class, responseHandler, true);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<PointRecordEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, PointRecordEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.listitem_point_records, null);
			return view;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public void bindView(View view, Context context, PointRecordEntity entity, int position) {
			TextView textView_date = (TextView) view.findViewById(R.id.textView_date);
			TextView textView_title = (TextView) view.findViewById(R.id.textView_title);
			TextView textView_content = (TextView) view.findViewById(R.id.textView_content);
			TextView textView_points = (TextView) view.findViewById(R.id.textView_points);
			textView_date.setText(entity.getBean().getCreateTime().substring(0, 10));
			textView_title.setText(entity.getBean().getTitle());
			textView_content.setText(entity.getBean().getContent());
			if (entity.getBean().getPoints() >= 0) {
				textView_points.setTextColor(0xffeb6100);
				textView_points.setText("+" + entity.getBean().getPoints());
			} else {
				textView_points.setTextColor(0xff22ac38);
				textView_points.setText("" + entity.getBean().getPoints());
			}
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<PointRecordEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}
	}

}

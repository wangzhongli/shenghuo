package com.e1858.mall.recommend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.mall.MallConstant;
import com.e1858.mall.protocol.bean.MallRCProduct;
import com.e1858.mall.protocol.packet.MallGetProductAlreadyRequest;
import com.e1858.mall.protocol.packet.MallGetProductAlreadyResp;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.wuye.mall.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.hg.android.utils.HGUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallBuyedActivity extends BaseActionBarActivity {
	private Context					context;
	private PullToRefreshGridView	mall_buyed_gv;
	private List<MallRCProduct>		mallRCProducts		= new ArrayList<MallRCProduct>();
	protected DisplayImageOptions	options;
	private MyAdapter				adapter;
	public static final String		SELECT_BUYED_PRO	= "select_buyed_pro";

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		setContentView(R.layout.mall_buyed_activity);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.mall_recommend_def)
				.showImageForEmptyUri(R.drawable.mall_recommend_def).showImageOnFail(R.drawable.mall_recommend_def)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		initViews();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initViews() {
		mall_buyed_gv = (PullToRefreshGridView) this.findViewById(R.id.mall_buyed_gv);
		adapter = new MyAdapter();
		mall_buyed_gv.setAdapter(adapter);
		loadBuyedProduct(0);
		mall_buyed_gv.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				loadBuyedProduct(adapter == null ? 0 : adapter.getCount());
			}

		});
		mall_buyed_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent data = new Intent();
				data.putExtra(SELECT_BUYED_PRO, (Serializable) adapter.getItem(position));
				setResult(RESULT_OK, data);
				finish();
			}
		});

	}

	protected void loadBuyedProduct(final int offset) {
		ResponseHandler<MallGetProductAlreadyResp> responseHandler = new ResponseHandler<MallGetProductAlreadyResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetProductAlreadyResp response, String error) {
				mall_buyed_gv.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
//					//加载完成以后
					mallRCProducts.addAll(response.getMallRCProducts());
					adapter.notifyDataSetChanged();
				}
			}
		};

		MallGetProductAlreadyRequest request = new MallGetProductAlreadyRequest();
		request.setOffset(offset);
		request.setCount(MallConstant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, MallGetProductAlreadyResp.class, responseHandler, true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return HGUtils.isListEmpty(mallRCProducts) ? 0 : mallRCProducts.size();
		}

		@Override
		public MallRCProduct getItem(int position) {
			return mallRCProducts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = View.inflate(context, R.layout.mall_buyed_item, null);
				viewHolder = new ViewHolder();
				viewHolder.mall_buyed_item_iv = (ImageView) convertView.findViewById(R.id.mall_buyed_item_iv);
				viewHolder.mall_buyed_item_tv = (TextView) convertView.findViewById(R.id.mall_buyed_item_tv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MallRCProduct mallRCProduct = mallRCProducts.get(position);
			ImageLoader.getInstance().displayImage(mallRCProduct.getThumbUrl(), viewHolder.mall_buyed_item_iv, options);
			viewHolder.mall_buyed_item_tv.setText(mallRCProduct.getProductName() == null ? "" : mallRCProduct
					.getProductName());

			return convertView;
		}
	}

	class ViewHolder {
		ImageView	mall_buyed_item_iv;
		TextView	mall_buyed_item_tv;
	}
}

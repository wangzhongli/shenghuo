package com.e1858.mall.cart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.BaseApplication;
import com.e1858.mall.entity.MallCartShopEntity;
import com.e1858.mall.order.MallConfirmOrderActivity;
import com.e1858.mall.protocol.bean.MallCartProductBean;
import com.e1858.mall.protocol.bean.MallCartShopBean;
import com.e1858.mall.protocol.bean.MallConfirmOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.packet.MallDeleteCartProductRequest;
import com.e1858.mall.protocol.packet.MallEditCartProductResponse;
import com.e1858.mall.protocol.packet.MallGetCartProductsRequest;
import com.e1858.mall.protocol.packet.MallGetCartProductsResponse;
import com.e1858.mall.utils.MallRequestHelper;
import com.e1858.mall.widget.MallShopCartCardHolder;
import com.e1858.mall.widget.MallShopCartCardHolder.OnProductCheckedChangeListener;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.wuye.mall.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.hg.android.ormlite.extra.OrmLiteNotificationCenter;
import com.hg.android.utils.HGUtils;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class MallCartActivity extends BaseActionBarActivity {
	MyAdapter							adapter;
	PullToRefreshListView				listView;
	TextView							submitButton;

	Map<String, MallCartProductBean>	checkedMap	= new HashMap<String, MallCartProductBean>();

	SqliteOpenHelper					openHelper;
	Dao<MallCartShopEntity, String>		dao;
	TextView							textView_amount;
	CheckBox							checkBox_selectAll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_activity_cart);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		try {
			openHelper = OpenHelperManager.getHelper(BaseApplication.getBaseInstance(), SqliteOpenHelper.class);
			dao = openHelper.getDao(MallCartShopEntity.class);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		EntityCacheHelper.sharedInstance().deleteAllEntities(MallCartShopEntity.class);
		initViews();
		updateBottomBar();
	}

	void initViews() {
		textView_amount = (TextView) findViewById(R.id.textView_amount);
		submitButton = (TextView) findViewById(R.id.button_submit);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onSubmit();
			}
		});

		checkBox_selectAll = (CheckBox) findViewById(R.id.checkBox_selectAll);
		checkBox_selectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkBox_selectAll.isChecked()) {
					checkAll();
				} else {
					uncheckAll();
				}
			}
		});

		listView = (PullToRefreshListView) findViewById(R.id.listView);
		listView.setMode(Mode.BOTH);
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.getRefreshableView().setDividerHeight(HGUtils.dip2px(getActivity(), 8));
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
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.setAdapter(adapter = new MyAdapter(this));
		adapter.load(getSupportLoaderManager(), SqliteOpenHelper.class, MallCartShopEntity.class);
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		if (openHelper != null) {
			OpenHelperManager.releaseHelper();
			openHelper = null;
		}
		super.onDestroy();
	}

	MallOrderProductBean transform(MallCartProductBean cartProduct) {
		MallOrderProductBean orderProduct = new MallOrderProductBean();
		orderProduct.setID(null);
		orderProduct.setProductID(cartProduct.getProductID());
		orderProduct.setCartProductID(cartProduct.getID());
		orderProduct.setThumbUrl(cartProduct.getThumbUrl());
		orderProduct.setPrice(cartProduct.getPrice());
		orderProduct.setQuantity(cartProduct.getQuantity());
		orderProduct.setShopID(cartProduct.getShopID());
		orderProduct.setName(cartProduct.getName());
		return orderProduct;
	}

	ArrayList<MallConfirmOrderBean> createConfirmOrders() {
		if (checkedMap.size() == 0) {
			return null;
		}

		ArrayList<MallConfirmOrderBean> confirmOrders = new ArrayList<MallConfirmOrderBean>();
		for (int i = adapter.getCount() - 1; i >= 0; i--) {
			MallCartShopBean shop = adapter.getItem(i).getBean();
			if (shop.getCartProducts() == null) {
				continue;
			}
			MallConfirmOrderBean confirmOrder = new MallConfirmOrderBean();
			confirmOrder.setSortIndex(adapter.getItem(i).getSortIndex());
			confirmOrder.setShopID(shop.getShopID());
			confirmOrder.setShopName(shop.getShopName());
			List<MallOrderProductBean> orderProducts = new ArrayList<MallOrderProductBean>();
			for (MallCartProductBean product : shop.getCartProducts()) {
				if (checkedMap.containsKey(product.getID())) {
					orderProducts.add(transform(product));
				}
			}
			if (!HGUtils.isListEmpty(orderProducts)) {
				confirmOrder.setOrderProducts(orderProducts);
				confirmOrders.add(confirmOrder);
			}
		}
		Collections.sort(confirmOrders, new Comparator<MallConfirmOrderBean>() {
			@Override
			public int compare(MallConfirmOrderBean arg0, MallConfirmOrderBean arg1) {
				return arg0.getSortIndex() - arg1.getSortIndex();
			}
		});
		return confirmOrders;
	}

	protected void onSubmit() {
		ArrayList<MallConfirmOrderBean> confirmOrders = createConfirmOrders();
		if (HGUtils.isListEmpty(confirmOrders)) {
			Toast.makeText(getActivity(), "没有选中任何商品", Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(this, MallConfirmOrderActivity.class);
		intent.putExtra(MallConfirmOrderActivity.IntentKey_ConfirmOrders, confirmOrders);
		startActivity(intent);
	}

	void updateBottomBar() {
		int count = checkedMap.size();
		if (count == 0) {
			textView_amount.setText("");
			submitButton.setText("结算");
		} else {
			submitButton.setText("结算(" + count + ")");
			float amount = 0;
			for (Entry<String, MallCartProductBean> entity : checkedMap.entrySet()) {
				MallCartProductBean product = entity.getValue();
				amount += product.getPrice() * product.getQuantity();
			}
			textView_amount.setText(String.format("合计:¥%.2f", amount));
		}
	}

	void checkAll() {
		checkedMap.clear();
		try {
			List<MallCartShopEntity> shops = dao.queryForAll();
			for (MallCartShopEntity entity : shops) {
				if (entity.getBean().getCartProducts() == null) {
					continue;
				}
				for (MallCartProductBean product : entity.getBean().getCartProducts()) {
					checkedMap.put(product.getID(), product);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	void uncheckAll() {
		checkedMap.clear();
		adapter.notifyDataSetChanged();
	}

	void clearCheckedMapThatNotExist() {
		List<String> IDs = checkedIDs();
		if (!HGUtils.isListEmpty(IDs)) {
			try {
				List<String> existIDs = new ArrayList<String>();
				for (int i = adapter.getCount() - 1; i >= 0; i--) {
					MallCartShopBean shop = adapter.getItem(i).getBean();
					if (shop.getCartProducts() == null) {
						continue;
					}
					for (MallCartProductBean product : shop.getCartProducts()) {
						String productID = product.getID();
						existIDs.add(productID);
						if (checkedMap.containsKey(productID)) {
							checkedMap.put(productID, product);
						}
					}
				}
				HGUtils.removeFromMapThatKeyNotExistInList(checkedMap, existIDs);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		updateBottomBar();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadProducts(0);
	}

	protected void loadProducts(final int offset) {

		ResponseHandler<MallGetCartProductsResponse> responseHandler = new ResponseHandler<MallGetCartProductsResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetCartProductsResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(MallCartShopEntity.class,
							response.getCartShops(), offset);
				}
			}
		};

		MallGetCartProductsRequest request = new MallGetCartProductsRequest();
		request.setOffset(offset);
		request.setCount(5);
		HttpPacketClient.postPacketAsynchronous(request, MallGetCartProductsResponse.class, responseHandler, true);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<MallCartShopEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, MallCartShopEntity entity, ViewGroup parent) {
			final View view = View.inflate(context, R.layout.mall_card_shop_cart, null);
			MallShopCartCardHolder holder = new MallShopCartCardHolder(context, view);
			holder.setOnCheckChangedListener(onCheckChangedListener);
			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, MallCartShopEntity entity, int position) {
			MallShopCartCardHolder handler = (MallShopCartCardHolder) view.getTag();
			handler.updateForShop(entity.getBean(), checkedMap);
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			clearCheckedMapThatNotExist();
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<MallCartShopEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}
	}

	List<String> checkedIDs() {
		return new ArrayList<String>(checkedMap.keySet());
	}

	void deleteProducts() {
		if (checkedMap.size() == 0) {
			Toast.makeText(this, "没有选中任何商品", Toast.LENGTH_LONG).show();
			return;
		}
		final MallDeleteCartProductRequest request = new MallDeleteCartProductRequest();
		request.setIDs(checkedIDs());
		ResponseHandler<MallEditCartProductResponse> responseHandler = new ResponseHandler<MallEditCartProductResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在删除中");
			}

			@Override
			public void onFinish(MallEditCartProductResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					SqliteOpenHelper openHelper = OpenHelperManager.getHelper(BaseApplication.getBaseInstance(),
							SqliteOpenHelper.class);
					try {
						ArrayList<MallConfirmOrderBean> orders = createConfirmOrders();
						final Dao<MallCartShopEntity, String> dao = openHelper.getDao(MallCartShopEntity.class);
						for (MallConfirmOrderBean order : orders) {
							MallCartShopEntity shopEntity = dao.queryForId(order.getShopID());
							for (MallOrderProductBean product : order.getOrderProducts()) {
								shopEntity.getBean().removeProductByID(product.getCartProductID());
							}
							shopEntity.setBean(shopEntity.getBean());
							dao.createOrUpdate(shopEntity);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					OpenHelperManager.releaseHelper();
					OrmLiteNotificationCenter.sharedInstance().notifyChange(MallCartShopEntity.class);
					loadProducts(0);
					MallRequestHelper.requestCartProductCount();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallEditCartProductResponse.class, responseHandler, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_cart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_trash) {
			HGUtils.showConfirmDialog(getActivity(), "确定删除选中项?", "删除之后将无法恢复", new Runnable() {
				@Override
				public void run() {
					deleteProducts();
				}
			});
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	OnProductCheckedChangeListener	onCheckChangedListener	= new OnProductCheckedChangeListener() {

																@Override
																public void onProductChecked(
																		MallCartProductBean product, boolean checked) {
																	if (checked) {
																		checkedMap.put(product.getID(), product);
																	} else {
																		checkBox_selectAll.setChecked(false);
																		checkedMap.remove(product.getID());
																	}
																	updateBottomBar();
																}
															};
}

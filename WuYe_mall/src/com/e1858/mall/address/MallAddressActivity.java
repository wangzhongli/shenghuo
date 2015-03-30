package com.e1858.mall.address;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.BaseApplication;
import com.e1858.mall.MallConstant;
import com.e1858.mall.entity.MallManagedAddressEntity;
import com.e1858.mall.protocol.bean.MallManagedAddressBean;
import com.e1858.mall.protocol.packet.MallDeleteManagedAddressRequest;
import com.e1858.mall.protocol.packet.MallDeleteManagedAddressResponse;
import com.e1858.mall.protocol.packet.MallGetManagedAddressesRequest;
import com.e1858.mall.protocol.packet.MallGetManagedAddressesResponse;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.wuye.mall.R;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.UserHouse;
import com.e1858.wuye.protocol.http.UserInfo;
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

public class MallAddressActivity extends BaseActionBarActivity {

	public static final String	BroadcastAction_Delete	= "MallAddressActivity.BroadcastAction_Delete";

	public static final String	IntentKey_isPick		= "IntentKey_isPick";							//boolean 
	public static final String	IntentKey_PickedAddress	= "IntentKey_PickedAddress";
	public static final String	IntentKey_AddressID		= "IntentKey_AddressID";

	MyAdapter					adapter;
	PullToRefreshListView		listView;
	Loader<?>					ormIteratorLoader;

	boolean						isPick					= false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_pulltorefresh_listview);
		if (getIntent() != null) {
			isPick = getIntent().getBooleanExtra(IntentKey_isPick, false);
		}

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews();
	}

	void initViews() {
		listView = (PullToRefreshListView) findViewById(R.id.listView);
		listView.setMode(Mode.BOTH);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadAddresses(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadAddresses(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.getRefreshableView().setDividerHeight(HGUtils.dip2px(getActivity(), 8));
		listView.setAdapter(adapter = new MyAdapter(this));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		adapter.load(getSupportLoaderManager(), SqliteOpenHelper.class, MallManagedAddressEntity.class);
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadAddresses(0);
	}

	void selectedItem(MallManagedAddressEntity entity) {
		if (isPick) {
			Intent intent = new Intent();
			intent.putExtra(IntentKey_PickedAddress, entity.getBean());
			setResult(RESULT_OK, intent);
			finish();
		} else {
			editAddress(entity);
		}
	}

	protected void loadAddresses(final int offset) {

		ResponseHandler<MallGetManagedAddressesResponse> responseHandler = new ResponseHandler<MallGetManagedAddressesResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetManagedAddressesResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(MallManagedAddressEntity.class,
							response.getAddresses(), offset);
				}
			}
		};

		MallGetManagedAddressesRequest request = new MallGetManagedAddressesRequest();
		request.setOffset(offset);
		request.setCount(MallConstant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, MallGetManagedAddressesResponse.class, responseHandler, true);
	}

	protected void deleteAddress(MallManagedAddressEntity entity) {
		final String addressID = entity.getBean().getID();
		MallDeleteManagedAddressRequest request = new MallDeleteManagedAddressRequest();
		request.setID(addressID);
		ResponseHandler<MallDeleteManagedAddressResponse> responseHandler = new ResponseHandler<MallDeleteManagedAddressResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在删除");
			}

			@Override
			public void onFinish(MallDeleteManagedAddressResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					Intent intent = new Intent(BroadcastAction_Delete);
					intent.putExtra(IntentKey_AddressID, addressID);
					sendBroadcast(intent);

					Toast.makeText(getActivity(), "已删除", Toast.LENGTH_LONG).show();
					SqliteOpenHelper openHelper = OpenHelperManager.getHelper(BaseApplication.getBaseInstance(),
							SqliteOpenHelper.class);
					try {
						Dao<MallManagedAddressEntity, String> dao = openHelper.getDao(MallManagedAddressEntity.class);
						dao.deleteById(addressID);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					OpenHelperManager.releaseHelper();
					OrmLiteNotificationCenter.sharedInstance().notifyChange(MallManagedAddressEntity.class);

					loadAddresses(0);
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallDeleteManagedAddressResponse.class, responseHandler, true);
	}

	protected void editAddress(MallManagedAddressEntity entity) {
		Intent intent = new Intent(getActivity(), MallAddressEditActivity.class);
		intent.putExtra(MallAddressEditActivity.IntentKey_Address, entity.getBean());
		startActivity(intent);
	}

	protected void newAddress() {
		Intent intent = new Intent(getActivity(), MallAddressEditActivity.class);
		if (adapter.getCount() == 0) {
			Community community = PreferencesUtils.getCommunity();
			UserHouse userHouse = PreferencesUtils.getUserHouse();
			UserInfo userInfo = PreferencesUtils.getUserInfo();
			MallManagedAddressBean addressBean = new MallManagedAddressBean();

			StringBuilder addressBuilder = new StringBuilder();
			if (community != null) {
				addressBean.setProvince(community.getProvince());
				addressBean.setCity(community.getCity());
				addressBean.setDistrict(community.getDistrict());
				if (!TextUtils.isEmpty(community.getAddress())) {
					addressBuilder.append(community.getAddress() + community.getName());
				}
			}
			if (userHouse != null) {
				if (userHouse.getHouse() != null && userHouse.getHouse().getName() != null) {
					addressBuilder.append(userHouse.getHouse().getName());
				}
				if (userHouse.getHouseUnit() != null && userHouse.getHouseUnit().getName() != null) {
					addressBuilder.append(userHouse.getHouseUnit().getName());
				}
				if (userHouse.getHouseFloor() != null && userHouse.getHouseFloor().getName() != null) {
					addressBuilder.append(userHouse.getHouseFloor().getName());
				}
				if (userHouse.getHouseRoom() != null && userHouse.getHouseRoom().getName() != null) {
					addressBuilder.append(userHouse.getHouseRoom().getName());
				}
			}
			addressBean.setAddress(addressBuilder.toString());
			if (userInfo != null) {
				addressBean.setName(userInfo.getName());
				addressBean.setPhone(userInfo.getMobile());
			}
			intent.putExtra(MallAddressEditActivity.IntentKey_Address, addressBean);
		}
		startActivity(intent);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<MallManagedAddressEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, MallManagedAddressEntity entity, ViewGroup parent) {
			final View view = View.inflate(context, R.layout.mall_listitem_managedaddress, null);
			view.findViewById(R.id.button_delete).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(getActivity()).setTitle("提醒").setMessage("确认删除这个地址吗？")
							.setNeutralButton("取消", null)
							.setNegativeButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									deleteAddress((MallManagedAddressEntity) view.getTag());
								}
							}).show();
				}
			});
			view.findViewById(R.id.button_edit).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					editAddress((MallManagedAddressEntity) view.getTag());
				}
			});
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selectedItem((MallManagedAddressEntity) view.getTag());
				}
			});
			return view;
		}

		@Override
		public void bindView(View view, Context context, MallManagedAddressEntity entity, int position) {
			TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
			TextView textView_phone = (TextView) view.findViewById(R.id.textView_phone);
			TextView textView_address = (TextView) view.findViewById(R.id.textView_address);
			view.setTag(entity);
			view.setTag(entity);
			textView_name.setText("姓名：" + entity.getBean().getName());
			textView_phone.setText("手机：" + entity.getBean().getPhone());
			textView_address.setText("详细地址：" + entity.getBean().provinceCityDistrict() + entity.getBean().getAddress());
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<MallManagedAddressEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_address, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_new) {
			newAddress();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

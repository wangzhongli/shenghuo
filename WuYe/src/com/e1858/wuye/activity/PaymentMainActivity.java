package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.FeeType;
import com.e1858.wuye.protocol.http.GetFeeType;
import com.e1858.wuye.protocol.http.GetFeeTypeResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.UserHouse;
import com.hg.android.widget.MyGridView;

/**
 * 缴费
 * 
 * @author jiajia 2014年8月22日
 */
public class PaymentMainActivity extends BaseActivity {
	private TextView		currentCity;
	private MyGridView		payment_type;
	private List<FeeType>	types			= new ArrayList<FeeType>();
	private List<FeeType>	loadTypes		= new ArrayList<FeeType>();
	private String			object_key;
	private int				currentPosition	= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_main);
		initView();
		object_key = PreferencesUtils.getCommunity().getID() + "_" + PreferencesUtils.getCommunity().getCity();
		loadData(true);
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(R.string.text_bar_payment));
		currentCity = (TextView) findViewById(R.id.currentCity);
		payment_type = (MyGridView) findViewById(R.id.payment_type);
		currentCity.setText(PreferencesUtils.getCommunity().getCity());
		initData();
	}

	private void loadData(Boolean localCache) {
		if (localCache) {
			GetFeeTypeResp resp = (GetFeeTypeResp) DataFileUtils.readObject(object_key);
			if (null != resp) {
				loadTypes.addAll(resp.getFeeTypes());
			}
		}
		if (application.isNetworkAvailable()) {
			GetFeeType getFeeType = new GetFeeType();
			getFeeType.setCommunityID(PreferencesUtils.getCommunity().getID());
			getFeeType.setKey(PreferencesUtils.getLoginKey());
			getFeeType.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getFeeType, handler, HttpDefine.GET_FEETYPE_RESP);
		} else {
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}

	private void initData() {
		for (int i = 0; i < 4; i++) {
			FeeType bean = new FeeType();
			bean.setID(i + 1);
			bean.setName("");
			types.add(bean);
		}
		payment_type.setAdapter(new TypeAdapter(types));
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			return true;
		}
		switch (msg.what) {
		case HttpDefine.GET_FEETYPE_RESP:
			if (null != (String) msg.obj) {
				GetFeeTypeResp resp = JsonUtil.fromJson((String) msg.obj, GetFeeTypeResp.class);
				if (null == resp) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					if (resp.getFeeTypes() != null && resp.getFeeTypes().size() > 0) {
						loadTypes.addAll(resp.getFeeTypes());
					} else {
						break;
					}
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	class TypeAdapter extends BaseAdapter {

		private List<FeeType>	lists;

		public TypeAdapter(List<FeeType> lists) {
			this.lists = lists;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(PaymentMainActivity.this).inflate(R.layout.payment_type_item, null);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.type_icon);
				viewHolder.name = (TextView) convertView.findViewById(R.id.type_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			switch (lists.get(position).getID()) {
			case Constant.PAYMENT_TYPE.WUYE_PAYMENT:
				viewHolder.name.setText("物业费");
				viewHolder.icon.setBackgroundResource(R.drawable.property_icon);
				break;
			case Constant.PAYMENT_TYPE.WATER_PAYMENT:
				viewHolder.name.setText("水费");
				viewHolder.icon.setBackgroundResource(R.drawable.water_icon);
				break;
			case Constant.PAYMENT_TYPE.ELEC_PAYMENT:
				viewHolder.name.setText("电费");
				viewHolder.icon.setBackgroundResource(R.drawable.electric_icon);
				break;
			case Constant.PAYMENT_TYPE.GAS_PAYMENT:
				viewHolder.name.setText("燃气费");
				viewHolder.icon.setBackgroundResource(R.drawable.gas_icon);
				break;
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					if (true) {
//						// TODO 
//						ToastUtil.show(PaymentMainActivity.this, "当前小区暂未开通电子缴费渠道");
//						return;
//					}
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					UserHouse userHouse = PreferencesUtils.getUserHouse();
					switch (lists.get(position).getID()) {
					case Constant.PAYMENT_TYPE.WUYE_PAYMENT:
						if (isHave(Constant.PAYMENT_TYPE.WUYE_PAYMENT)) {
							if (null == userHouse) {
								ToastUtil.show(PaymentMainActivity.this, "请先选择房屋信息", 1000);
								intent.setClass(PaymentMainActivity.this, SwitchResidentAddressActivity.class);
							} else {
								intent.setClass(PaymentMainActivity.this, PropertyPaymentActivity.class);
							}
							startActivity(intent);
						} else {
							ToastUtil.show(PaymentMainActivity.this, "当前小区暂未开通缴物业费功能");
						}
						break;
					case Constant.PAYMENT_TYPE.WATER_PAYMENT:
						if (isHave(Constant.PAYMENT_TYPE.WATER_PAYMENT)) {
							if (null == userHouse) {
								ToastUtil.show(PaymentMainActivity.this, "请先选择房屋信息", 1000);
								intent.setClass(PaymentMainActivity.this, SwitchResidentAddressActivity.class);
							} else {
								intent.setClass(PaymentMainActivity.this, PaymentWEGActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("feeInfo", loadTypes.get(currentPosition));
								intent.putExtras(bundle);
							}
							startActivity(intent);
						} else {
							ToastUtil.show(PaymentMainActivity.this, "当前城市暂未开通缴水费功能");
						}
						break;
					case Constant.PAYMENT_TYPE.ELEC_PAYMENT:
						if (isHave(Constant.PAYMENT_TYPE.ELEC_PAYMENT)) {
							if (null == userHouse) {
								ToastUtil.show(PaymentMainActivity.this, "请先选择房屋信息", 1000);
								intent.setClass(PaymentMainActivity.this, SwitchResidentAddressActivity.class);
							} else {
								intent.setClass(PaymentMainActivity.this, PaymentWEGActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("feeInfo", loadTypes.get(currentPosition));
								intent.putExtras(bundle);
							}
							startActivity(intent);
						} else {
							ToastUtil.show(PaymentMainActivity.this, "当前城市暂未开通缴电费功能");
						}
						break;
					case Constant.PAYMENT_TYPE.GAS_PAYMENT:
						if (isHave(Constant.PAYMENT_TYPE.GAS_PAYMENT)) {
							if (null == userHouse) {
								ToastUtil.show(PaymentMainActivity.this, "请先选择房屋信息", 1000);
								intent.setClass(PaymentMainActivity.this, SwitchResidentAddressActivity.class);
							} else {
								intent.setClass(PaymentMainActivity.this, PaymentWEGActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("feeInfo", loadTypes.get(currentPosition));
								intent.putExtras(bundle);
							}
							startActivity(intent);
						} else {
							ToastUtil.show(PaymentMainActivity.this, "当前城市暂未开通缴燃气费功能");
						}
						break;
					}
				}
			});
			return convertView;
		}

	}

	class ViewHolder {
		private TextView	name;
		private ImageView	icon;
	}

	private boolean isHave(int type) {
		for (int i = 0; i < loadTypes.size(); i++) {
			if (loadTypes.get(i).getID() == type) {
				currentPosition = i;
				return true;
			}
		}
		return false;
	}
}

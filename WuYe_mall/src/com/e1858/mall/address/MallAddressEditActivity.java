package com.e1858.mall.address;

import java.util.regex.Pattern;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.Constant;
import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.mall.protocol.bean.MallManagedAddressBean;
import com.e1858.mall.protocol.packet.MallEditManagedAddressRequest;
import com.e1858.mall.protocol.packet.MallEditManagedAddressResponse;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.mall.R;
import com.hg.android.widget.CityDBManager.AreaEntity;
import com.hg.android.widget.CityPickDialog;
import com.hg.android.widget.CityPickDialog.OnPickedListener;

public class MallAddressEditActivity extends BaseActionBarActivity {
	public static final String	IntentKey_Address	= "IntentKey_Address";
	TextView					textView_city;
	EditText					editText_address;
	EditText					editText_name;
	EditText					editText_zipcode;
	EditText					editText_phone;

	MallManagedAddressBean		addressBean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_activity_addressedit);

		if (getIntent() != null) {
			addressBean = (MallManagedAddressBean) getIntent().getSerializableExtra(IntentKey_Address);
		}
		if (addressBean == null) {
			addressBean = new MallManagedAddressBean();
		}
		if (addressBean.getID() == null) {
			setTitle("新增收货信息");
		} else {
			setTitle("修改收货信息");
		}

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews();
	}

	void initViews() {
		textView_city = (TextView) findViewById(R.id.textView_city);
		editText_address = (EditText) findViewById(R.id.editText_address);
		editText_name = (EditText) findViewById(R.id.editText_name);
		editText_phone = (EditText) findViewById(R.id.editText_phone);
		editText_zipcode = (EditText) findViewById(R.id.editText_zipcode);
		textView_city.setText(addressBean.provinceCityDistrict());
		editText_address.setText(addressBean.getAddress());
		editText_zipcode.setText(addressBean.getZipCode());
		editText_name.setText(addressBean.getName());
		editText_phone.setText(addressBean.getPhone());

		findViewById(R.id.listitem_city).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				CityPickDialog dialog = new CityPickDialog(getActivity(), new OnPickedListener() {
					@Override
					public void onPicked(AreaEntity province, AreaEntity city, AreaEntity district, String text) {
						addressBean.setProvince(province != null ? province.getName() : null);
						addressBean.setCity(city != null ? city.getName() : null);
						addressBean.setDistrict(district != null ? district.getName() : null);
						textView_city.setText(addressBean.provinceCityDistrict());
					}
				});
				dialog.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_addressedit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_accept) {
			saveAddress();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean verifyInput() {
		if (TextUtils.isEmpty(addressBean.getProvince()) && TextUtils.isEmpty(addressBean.getCity())
				&& TextUtils.isEmpty(addressBean.getDistrict())) {
			Toast.makeText(getActivity(), "省市区不能为空", Toast.LENGTH_LONG).show();
			return false;
		}

		if (TextUtils.isEmpty(addressBean.getAddress())) {
			Toast.makeText(getActivity(), "详细地址不能为空", Toast.LENGTH_LONG).show();
			return false;
		}

		if (TextUtils.isEmpty(addressBean.getName())) {
			Toast.makeText(getActivity(), "收货人姓名不能为空", Toast.LENGTH_LONG).show();
			return false;
		}

		if (TextUtils.isEmpty(addressBean.getPhone())) {
			Toast.makeText(getActivity(), "收货人手机不能为空", Toast.LENGTH_LONG).show();
			return false;
		}

		if (!Pattern.matches(Constant.MOBILE_REGP, addressBean.getPhone())) {
			Toast.makeText(getActivity(), "收货人手机格式错误", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private void saveAddress() {
		addressBean.setName(editText_name.getText().toString());
		addressBean.setZipCode(editText_zipcode.getText().toString());
		addressBean.setPhone(editText_phone.getText().toString());
		addressBean.setAddress(editText_address.getText().toString());
		if (!verifyInput()) {
			return;
		}

		MallEditManagedAddressRequest request = new MallEditManagedAddressRequest();
		request.setAddress(addressBean);
		ResponseHandler<MallEditManagedAddressResponse> responseHandler = new ResponseHandler<MallEditManagedAddressResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在处理中");
			}

			@Override
			public void onFinish(MallEditManagedAddressResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (addressBean.getID() == null) {
						Toast.makeText(getActivity(), "地址添加成功", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getActivity(), "地址修改成功", Toast.LENGTH_LONG).show();
					}
					finish();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallEditManagedAddressResponse.class, responseHandler, true);
	}
}

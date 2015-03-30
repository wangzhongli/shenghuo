package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.e1858.common.AddSuccessDialogManager;
import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DateUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.SysAreaAdapter;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.KangelGenerateOrder;
import com.e1858.wuye.protocol.http.KangelGenerateOrderResp;
import com.e1858.wuye.protocol.http.KangelGetAddress;
import com.e1858.wuye.protocol.http.KangelGetAddressResp;
import com.e1858.wuye.protocol.http.KangelGetPlatBaseRegion;
import com.e1858.wuye.protocol.http.KangelGetPlatBaseRegionResp;
import com.e1858.wuye.protocol.http.KangelPlatBaseRegion;
import com.e1858.wuye.protocol.http.KangelPlatBaseRegionArea;
import com.e1858.wuye.protocol.http.UserHouse;
import com.e1858.wuye.protocol.http.UserInfo;
import com.hg.android.widget.CityDBManager;
import com.hg.android.widget.CityDBManager.AreaEntity;

/**
 * 新增我要洗衣
 * 
 * @author jiajia
 */
public class AddKangelOrderActivity extends BaseActivity implements OnClickListener {
	private TextView						appointmentTime;														// 预约时间 弹出日期选择框
	private EditText						membername;															// 预约人姓名
	private TextView						plate_range;															// 片范围
	private EditText						mobilephone;															// 电话
	private Spinner							provice_spinner;														// 省
	private Spinner							city_spinner;															// 市
	private Spinner							area_spinner;															// 区
	private Spinner							plateRegin_spinner;													// 片
	private EditText						address;																// 详细地址
	private EditText						description;															// 备注
	private Button							ok_btn;																// 确定按钮
	private int								year;																	// 年
	private int								month;																	// 月
	private int								day;																	// 日
	private Dialog							timeDialog;															// 日期弹出框
	// 设置默认值~根据名称查找到ID所在项，设置ID值（spinner初始化）未处理
	// 查看详情的时候需要KangelOrder 组装KangelOrder未处理
	private List<AreaEntity>				provinces				= new ArrayList<AreaEntity>();					// 省
	private List<AreaEntity>				citys					= new ArrayList<AreaEntity>();					// 市
	private List<AreaEntity>				areas					= new ArrayList<AreaEntity>();					// 区
	private List<AreaEntity>				plat_Areas				= new ArrayList<AreaEntity>();					// 区

	private int								province_SelectPosition	= 0;
	private int								city_SelectPosition		= 0;
	private int								areas_SelectPosition	= 0;

	private Calendar						calendar				= Calendar.getInstance(Locale.CHINA);
	private KangelPlatBaseRegion			baseRegion				= new KangelPlatBaseRegion();

	private List<KangelPlatBaseRegionArea>	platAreas				= new ArrayList<KangelPlatBaseRegionArea>();	// 片区

	KangelGetAddressResp					kangelGetAddressResp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_kangel_order);
		CityDBManager.sharedInstance(this).openDatabase();
		initView();
		initData();
		loadLastAddress();
	}

	void loadLastAddress() {
		KangelGetAddress request = new KangelGetAddress();
		ResponseHandler<KangelGetAddressResp> responseHandler = new ResponseHandler<KangelGetAddressResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(KangelGetAddressResp response, String error) {
				if (isDestroyedApi8()) {
					return;
				}
				if (response != null && response.isSuccess() && provice_spinner != null) {
					kangelGetAddressResp = response;
					address.setText(response.getTakeDetailAddress());
					mobilephone.setText(response.getMobilephone());
					if (!StringUtils.isEmpty(response.getProvince())) {
						queryProvince(response.getProvince());
						if (!StringUtils.isEmpty(response.getCity())) {
							queryCity(response.getCity());
						}
						if (!StringUtils.isEmpty(response.getDistrict())) {
							queryArea(response.getDistrict());
							getPlatRegion();
						}
					}
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, KangelGetAddressResp.class, responseHandler);
	}

	@Override
	public void onDestroy() {
		provice_spinner = null;
		CityDBManager.sharedInstance(this).closeDatabase();
		super.onDestroy();
	}

	private void initproviceSpinner() {
		provinces = CityDBManager.sharedInstance(this).allProvinces();
		// 设置spinner
		provice_spinner.setAdapter(new SysAreaAdapter(this, provinces));
		provice_spinner.setSelection(15, true);
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText("快速下单");
		appointmentTime = (TextView) findViewById(R.id.appointmentTime);
		membername = (EditText) findViewById(R.id.membername);
		mobilephone = (EditText) findViewById(R.id.mobilephone);
		provice_spinner = (Spinner) findViewById(R.id.provice_spinner);
		city_spinner = (Spinner) findViewById(R.id.city_spinner);
		area_spinner = (Spinner) findViewById(R.id.area_spinner);
		plateRegin_spinner = (Spinner) findViewById(R.id.plateRegin_spinner);
		address = (EditText) findViewById(R.id.address);
		description = (EditText) findViewById(R.id.description);
		plate_range = (TextView) findViewById(R.id.plate_range);
		ok_btn = (Button) findViewById(R.id.ok_btn);
		appointmentTime.setOnClickListener(this);
		ok_btn.setOnClickListener(this);
		provice_spinner.setPrompt("选择省份");
		city_spinner.setPrompt("选择市");
		area_spinner.setPrompt("选择区");
		plateRegin_spinner.setPrompt("选择片区");
		Date myDate = new Date(System.currentTimeMillis());
		calendar.setTime(myDate);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		provice_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				citys = CityDBManager.sharedInstance(AddKangelOrderActivity.this).citiesForProvince(
						((AreaEntity) provice_spinner.getSelectedItem()).getCode());
				// 设置spinner
				city_spinner.setAdapter(new SysAreaAdapter(getApplicationContext(), citys));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				areas = CityDBManager.sharedInstance(AddKangelOrderActivity.this).citiesForProvince(
						((AreaEntity) city_spinner.getSelectedItem()).getCode());

				// 设置spinner
				if (areas.size() == 0) {
					area_spinner.setEnabled(false);
				} else {
					area_spinner.setAdapter(new SysAreaAdapter(getApplicationContext(), areas));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		area_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				getPlatRegion();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		plateRegin_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (platAreas != null && platAreas.size() > 0) {
					plate_range.setText(platAreas.get(position).getEdescription());
				} else {
					plate_range.setText("范围:暂无");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void queryProvince(String province) {
		provinces = CityDBManager.sharedInstance(this).allProvinces();

		for (int i = 0; i < provinces.size(); i++) {
			if (StringUtils.isEquals(province, provinces.get(i).getName())) {
				province_SelectPosition = i;
				break;
			}
		}
		provice_spinner.setAdapter(new SysAreaAdapter(this, provinces));
		provice_spinner.setSelection(province_SelectPosition, true);
	}

	private void queryCity(String city) {
		citys = CityDBManager.sharedInstance(this).citiesForProvince(
				((AreaEntity) provice_spinner.getSelectedItem()).getCode());

		for (int i = 0; i < citys.size(); i++) {
			if (StringUtils.isEquals(city, citys.get(i).getName())) {
				city_SelectPosition = i;
				break;
			}
		}
		city_spinner.setAdapter(new SysAreaAdapter(this, citys));
		city_spinner.setSelection(city_SelectPosition, true);
	}

	private void queryArea(String city) {
		areas = CityDBManager.sharedInstance(AddKangelOrderActivity.this).citiesForProvince(
				((AreaEntity) city_spinner.getSelectedItem()).getCode());
		for (int i = 0; i < areas.size(); i++) {
			if (StringUtils.isEquals(city, areas.get(i).getName())) {
				areas_SelectPosition = i;
				break;
			}
		}
		area_spinner.setAdapter(new SysAreaAdapter(this, areas));
		area_spinner.setSelection(areas_SelectPosition, true);
	}

	private void initData() {
		UserInfo userInfo = PreferencesUtils.getUserInfo();
		UserHouse userHouse = PreferencesUtils.getUserHouse();
		if (userInfo != null) {
			if (!StringUtils.isEmpty(userInfo.getName())) {
				membername.setText(userInfo.getName());
			}
			mobilephone.setText(userInfo.getMobile());
		}
		if (null != userHouse) {
			String province = PreferencesUtils.getCommunity().getProvince();
			String city = PreferencesUtils.getCommunity().getCity();
			address.setText((province == null ? "" : province) + (city == null ? "" : city)
					+ Util.getHouseInfo(true));
		}

		if (!StringUtils.isEmpty(PreferencesUtils.getCommunity().getProvince())) {
			queryProvince(PreferencesUtils.getCommunity().getProvince());
			if (!StringUtils.isEmpty(PreferencesUtils.getCommunity().getCity())) {
				queryCity(PreferencesUtils.getCommunity().getCity());
			}
			if (!StringUtils.isEmpty(PreferencesUtils.getCommunity().getDistrict())) {
				queryArea(PreferencesUtils.getCommunity().getDistrict());
			}
		} else {
			initproviceSpinner();
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			closeProgressDialog();
			return true;
		}
		switch (msg.what) {
		case HttpDefine.KANGEL_GENERATE_ORDER_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj) {
				KangelGenerateOrderResp resp = JsonUtil.fromJson((String) msg.obj, KangelGenerateOrderResp.class);
				if (null == resp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					// 弹出成功和查看详情
					AddSuccessDialogManager
							.createCallDialog(this, Constant.ADDSUCCESS_MODULE.KANGEL, 0, resp.getUuid());
				} else {
					ok_btn.setEnabled(true);
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.KANGEL_GET_PLATBASE_REGIN_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj) {
				KangelGetPlatBaseRegionResp resp = JsonUtil.fromJson((String) msg.obj,
						KangelGetPlatBaseRegionResp.class);
				if (null == resp) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					if (null != resp.getPlatBaseRegionAreas() && resp.getPlatBaseRegionAreas().size() > 0) {
						platAreas.clear();
						plat_Areas.clear();
						baseRegion = resp.getPlatBaseRegion();
						platAreas.addAll(resp.getPlatBaseRegionAreas());
						int selctedIndex = 0;
						for (int i = 0; i < resp.getPlatBaseRegionAreas().size(); i++) {
							AreaEntity sysArea = new AreaEntity();
							sysArea.setCode(resp.getPlatBaseRegionAreas().get(i).getAreaID() + "");
							sysArea.setName(resp.getPlatBaseRegionAreas().get(i).getAreaName());
							plat_Areas.add(sysArea);
							if (kangelGetAddressResp != null
									&& resp.getPlatBaseRegionAreas().get(i).getAreaID() == kangelGetAddressResp
											.getTakePlatBaseRegionAreaID()) {
								selctedIndex = i;
								kangelGetAddressResp = null;
							}
						}
						plateRegin_spinner.setAdapter(new SysAreaAdapter(getApplicationContext(), plat_Areas));
						plateRegin_spinner.setSelection(selctedIndex);
					} else {
						platAreas.clear();
						plat_Areas.clear();
						AreaEntity sysArea = new AreaEntity();
						sysArea.setCode("-1");
						sysArea.setName("暂无片区类型");
						plat_Areas.add(sysArea);
						plateRegin_spinner.setAdapter(new SysAreaAdapter(getApplicationContext(), plat_Areas));
						plate_range.setText("范围:暂无");
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ok_btn:
			// 提交
			if (StringUtils.isBlank(appointmentTime.getText().toString())) {
				ToastUtil.show(this, "预约取件时间不能为空");
			} else if (StringUtils.isBlank(membername.getText().toString())) {
				ToastUtil.show(this, "预约人不能为空");
			} else if (StringUtils.isBlank(mobilephone.getText().toString())) {
				ToastUtil.show(this, "联系方式不能为空");
			} else if (!StringUtils.isBlank(mobilephone.getText().toString())
					&& !Pattern.matches(Constant.MOBILE_REGP, mobilephone.getText().toString())) {
				ToastUtil.show(this, Constant.ToastMessage.MOBILE_REGP_ERROR);
			} else if (platAreas.size() == 0 || platAreas == null) {
				ToastUtil.show(this, "该区域不在服务范围内");
			} else if (StringUtils.isBlank(address.getText().toString())) {
				ToastUtil.show(this, "详细地址为空");
			} else {
				generateData();
			}
			break;
		case R.id.appointmentTime:
			// 选择日期时间
			createDateandTimeDialog();
			break;
		}
	}

	private void createDateandTimeDialog() {
		timeDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		timeDialog.setContentView(R.layout.kangel_datetime_dialog);
		Window window = timeDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.8f;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.pic_dialog_style);
		DatePicker datePicker = (DatePicker) timeDialog.findViewById(R.id.service_date);
		// 时间区间
		final Spinner timeRange = (Spinner) timeDialog.findViewById(R.id.service_time);
		timeRange.setPrompt("请选择时间段");
		String[] cItems = getResources().getStringArray(R.array.time_range);
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cItems);
		timeRange.setAdapter(_Adapter);

		Button ok_btn = (Button) timeDialog.findViewById(R.id.ok_btn);
		Button cancleBtn = (Button) timeDialog.findViewById(R.id.cancel_btn);
		// ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))
		// .getChildAt(0).setVisibility(View.GONE);
		// ;
		// ((ViewGroup) ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0))
		// .getChildAt(0)).getChildAt(1)).getChildAt(2).setEnabled(false);
		// ((ViewGroup) ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0))
		// .getChildAt(0)).getChildAt(2)).getChildAt(2).setEnabled(false);
		datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int yearofyear, int monthOfYear, int dayOfMonth) {

				year = yearofyear;
				month = monthOfYear;
				day = dayOfMonth;

			}
		});
		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timeDialog.dismiss();
			}
		});
		ok_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String month_Str = "" + (month + 1);
				String day_Str = "" + day;
				String hour_Str = timeRange.getSelectedItem().toString();
				if (month < 9) {
					month_Str = "0" + (month + 1);
				}
				if (day < 10) {
					day_Str = "0" + day;
				}
				updateTime("" + year, month_Str, day_Str, hour_Str);
				timeDialog.dismiss();
			}
		});

		timeDialog.show();
	}

	private void updateTime(String year, String month, String day, String hour) {
		String time = new StringBuilder().append(year).append(Constant.DATE_SEPARATOR).append(month)
				.append(Constant.DATE_SEPARATOR).append(day).append(" ").append(hour).toString();
		try {
			if (DateUtil.KangelDateCompare(time)) {
				appointmentTime.setText(time);
			} else {
				ToastUtil.show(this, "预约时间不能小于当前所在时间段");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateData() {
		if (application.isNetworkAvailable()) {
			openProgressDialog("提交中...");
			ok_btn.setEnabled(false);
			// 处理数据提交请求
			KangelGenerateOrder generateOrder = new KangelGenerateOrder();
			generateOrder.setCommunityID(PreferencesUtils.getCommunity().getID());
			generateOrder.setKey(PreferencesUtils.getLoginKey());
			generateOrder.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
			generateOrder.setTakePlatBaseRegionID(baseRegion.getRegionID());
			generateOrder.setTakePlatBaseRegionAreaID(Integer.parseInt(((AreaEntity) plateRegin_spinner
					.getSelectedItem()).getCode()));
			generateOrder.setTakeDetailAddress(address.getText().toString());
			generateOrder.setAppointmentTime(appointmentTime.getText().toString());
			generateOrder.setEdescription(description.getText().toString());
			generateOrder.setMembername(membername.getText().toString());
			generateOrder.setMobilephone(mobilephone.getText().toString());
			NetUtil.post(Constant.BASE_URL, generateOrder, handler, HttpDefine.KANGEL_GENERATE_ORDER_RESP);
		} else {
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}

	public void getPlatRegion() {
		if (application.isNetworkAvailable()) {
			openProgressDialog("请求中...");
			// 请求片信息
			KangelGetPlatBaseRegion baseRegion = new KangelGetPlatBaseRegion();
			baseRegion.setKey(PreferencesUtils.getLoginKey());
			baseRegion.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
			baseRegion.setProvince(((AreaEntity) provice_spinner.getSelectedItem()).getName());
			baseRegion.setCommunityID(PreferencesUtils.getCommunity().getID());
			baseRegion.setCity(((AreaEntity) city_spinner.getSelectedItem()).getName());
			baseRegion.setDistrict(((AreaEntity) area_spinner.getSelectedItem()).getName());
			NetUtil.post(Constant.BASE_URL, baseRegion, handler, HttpDefine.KANGEL_GET_PLATBASE_REGIN_RESP);
		} else {
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}
}

package com.e1858.wuye.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.common.app.AppManager;
import com.e1858.utils.Encrypt;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.ChangeHouse;
import com.e1858.wuye.protocol.http.ChangeHouseResp;
import com.e1858.wuye.protocol.http.GetHouseFloors;
import com.e1858.wuye.protocol.http.GetHouseFloorsResp;
import com.e1858.wuye.protocol.http.GetHouseUnits;
import com.e1858.wuye.protocol.http.GetHouseUnitsResp;
import com.e1858.wuye.protocol.http.GetHouses;
import com.e1858.wuye.protocol.http.GetHousesResp;
import com.e1858.wuye.protocol.http.HouseRoom;
import com.e1858.wuye.protocol.http.UserHouse;
import com.hg.android.utils.HGUtils;

/**
 * 选择居住地址
 * 
 * @author jiajia 2014年7月22日
 */
public class SwitchResidentAddressActivity extends BaseActivity implements OnClickListener {

	private TextView			mbuildTextView;
	private TextView			munitTextView;
	private TextView			mfloorTextView;
	private EditText			mhouseNumberEditText;
	private EditText			mVerify;

	UserHouse					selectedUserHouse;

	private GetHousesResp		getHousesResp		= new GetHousesResp();
	private GetHouseUnitsResp	getHouseUnitsResp	= new GetHouseUnitsResp();
	private GetHouseFloorsResp	getHouseFloorsResp	= new GetHouseFloorsResp();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resident_address_select);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
		loadHouse();
	}

	private void initView() {
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(R.string.text_bar_switchResident));
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		mbuildTextView = (TextView) findViewById(R.id.building_spinner);
		munitTextView = (TextView) findViewById(R.id.unit_spinner);
		mfloorTextView = (TextView) findViewById(R.id.floor_spinner);
		mhouseNumberEditText = (EditText) findViewById(R.id.input_houseNumber);
		mVerify = (EditText) findViewById(R.id.input_verify);
		findViewById(R.id.ok_btn).setOnClickListener(this);

		selectedUserHouse = PreferencesUtils.getUserHouse();
		if (selectedUserHouse == null) {
			selectedUserHouse = new UserHouse();
			selectedUserHouse.setHouse(null);
			selectedUserHouse.setHouseFloor(null);
			selectedUserHouse.setHouseUnit(null);
			selectedUserHouse.setHouseRoom(null);
		}
		if (null != selectedUserHouse.getHouse()) {
			mbuildTextView.setText(selectedUserHouse.getHouse().getName());
			loadHouseUnit();
		}
		if (null != selectedUserHouse.getHouseUnit()) {
			munitTextView.setText(selectedUserHouse.getHouseUnit().getName());
			loadHouseFloor();
		}
		if (null != selectedUserHouse.getHouseFloor()) {
			mfloorTextView.setText(selectedUserHouse.getHouseFloor().getName());
		}
		if (null != selectedUserHouse.getHouseRoom()) {
			mhouseNumberEditText.setText(selectedUserHouse.getHouseRoom().getName());
		}

		mbuildTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getHousesResp == null || HGUtils.isListEmpty(getHousesResp.getHouses())) {
					ToastUtil.show(SwitchResidentAddressActivity.this, "该小区无楼栋信息,请选择其他小区");
				} else {
					CharSequence[] items = new CharSequence[getHousesResp.getHouses().size()];
					for (int i = 0; i < items.length; i++) {
						items[i] = getHousesResp.getHouses().get(i).getName();
					}
					new AlertDialog.Builder(SwitchResidentAddressActivity.this)
							.setItems(items, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									selectedUserHouse.setHouse(getHousesResp.getHouses().get(which));
									mbuildTextView.setText(selectedUserHouse.getHouse().getName());
									selectedUserHouse.setHouseUnit(null);
									selectedUserHouse.setHouseFloor(null);
									loadHouseUnit();
								}
							}).setTitle("选择楼栋").show();
				}
			}
		});
		munitTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (getHousesResp == null || HGUtils.isListEmpty(getHousesResp.getHouses())) {
					ToastUtil.show(SwitchResidentAddressActivity.this, "该小区无楼栋信息,请选择其他小区");
				} else {
					CharSequence[] items = new CharSequence[getHouseUnitsResp.getHouseUnits().size()];
					for (int i = 0; i < items.length; i++) {
						items[i] = getHouseUnitsResp.getHouseUnits().get(i).getName();
					}
					new AlertDialog.Builder(SwitchResidentAddressActivity.this)
							.setItems(items, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									selectedUserHouse.setHouseUnit(getHouseUnitsResp.getHouseUnits().get(which));
									munitTextView.setText(selectedUserHouse.getHouseUnit().getName());
									selectedUserHouse.setHouseFloor(null);
									loadHouseFloor();
								}
							}).setTitle("选择单元").show();
				}
			}
		});
		mfloorTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CharSequence[] items = new CharSequence[getHouseFloorsResp.getHouseFloors().size()];
				for (int i = 0; i < items.length; i++) {
					items[i] = getHouseFloorsResp.getHouseFloors().get(i).getName();
				}
				new AlertDialog.Builder(SwitchResidentAddressActivity.this)
						.setItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								selectedUserHouse.setHouseFloor(getHouseFloorsResp.getHouseFloors().get(which));
								mfloorTextView.setText(selectedUserHouse.getHouseFloor().getName());
							}
						}).setTitle("选择楼层").show();
			}
		});
		bar_leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				SwitchResidentAddressActivity.this.setResult(Constant.HOUSE_INFO_RESULT_CODE, data);
				AppManager.getAppManager().finishActivity();

			}
		});
	}

	private void loadHouse() {
		GetHouses getHouses = new GetHouses();
		getHouses.setKey(PreferencesUtils.getLoginKey());
		getHouses.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
		getHouses.setCommunityID(PreferencesUtils.getCommunity().getID());

		ResponseHandler<GetHousesResp> responseHandler = new ResponseHandler<GetHousesResp>() {

			@Override
			public void onStart() {
				showProgressDialog(SwitchResidentAddressActivity.this, "");
			}

			@Override
			public void onFinish(GetHousesResp response, String error) {
				hideProgressDialog();
				getHousesResp = response;
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (HGUtils.isListEmpty(response.getHouses())) {
						munitTextView.setText("该小区无楼栋信息");
						selectedUserHouse = new UserHouse();
						munitTextView.setEnabled(false);
						mfloorTextView.setEnabled(false);
					}
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(getHouses, GetHousesResp.class, responseHandler);
	}

	private void loadHouseUnit() {
		GetHouseUnits getHouseUnits = new GetHouseUnits();
		getHouseUnits.setKey(PreferencesUtils.getLoginKey());
		getHouseUnits.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
		getHouseUnits.setHouseID(selectedUserHouse.getHouse().getID());
		getHouseUnits.setCommunityID(PreferencesUtils.getCommunity().getID());

		ResponseHandler<GetHouseUnitsResp> responseHandler = new ResponseHandler<GetHouseUnitsResp>() {

			@Override
			public void onStart() {
				showProgressDialog(SwitchResidentAddressActivity.this, "");
			}

			@Override
			public void onFinish(GetHouseUnitsResp response, String error) {
				hideProgressDialog();
				getHouseUnitsResp = response;
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (HGUtils.isListEmpty(response.getHouseUnits())) {
						selectedUserHouse.setHouseUnit(null);
						munitTextView.setEnabled(false);
						munitTextView.setText("该楼栋无单元信息");
					} else {
						selectedUserHouse.setHouseUnit(response.getHouseUnits().get(0));
						munitTextView.setEnabled(true);
						munitTextView.setText(selectedUserHouse.getHouseUnit().getName());
					}
					loadHouseFloor();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(getHouseUnits, GetHouseUnitsResp.class, responseHandler);

	}

	private void loadHouseFloor() {
		GetHouseFloors getHouseFloors = new GetHouseFloors();
		getHouseFloors.setCommunityID(PreferencesUtils.getCommunity().getID());
		getHouseFloors.setKey(PreferencesUtils.getLoginKey());
		getHouseFloors.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
		getHouseFloors.setHouseID(selectedUserHouse.getHouse().getID());
		if (selectedUserHouse.getHouseUnit() != null) {
			getHouseFloors.setHouseUnitID(selectedUserHouse.getHouseUnit().getID());
		}

		ResponseHandler<GetHouseFloorsResp> responseHandler = new ResponseHandler<GetHouseFloorsResp>() {

			@Override
			public void onStart() {
				showProgressDialog(SwitchResidentAddressActivity.this, "");
			}

			@Override
			public void onFinish(GetHouseFloorsResp response, String error) {
				hideProgressDialog();
				getHouseFloorsResp = response;
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (HGUtils.isListEmpty(response.getHouseFloors())) {
						selectedUserHouse.setHouseFloor(null);
						mfloorTextView.setEnabled(false);
						mfloorTextView.setText("该单元无楼层信息");
					} else {
						mfloorTextView.setEnabled(true);
						selectedUserHouse.setHouseFloor(response.getHouseFloors().get(0));
						mfloorTextView.setText(selectedUserHouse.getHouseFloor().getName());
					}
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(getHouseFloors, GetHouseFloorsResp.class, responseHandler);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ok_btn:
			if (selectedUserHouse.getHouse() == null) {
				ToastUtil.show(this, "请输入楼栋信息");
			} else if (StringUtils.isEmpty(mhouseNumberEditText.getText().toString())) {
				ToastUtil.show(this, "请输入门牌号");
//			} else if (StringUtils.isEmpty(mVerify.getText().toString())) {
//				ToastUtil.show(this, "请输入验证码");
			} else {
				ChangeHouse changeHouse = new ChangeHouse();
				changeHouse.setKey(PreferencesUtils.getLoginKey());
				changeHouse.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
				changeHouse.setHouseID(selectedUserHouse.getHouse().getID());
				if (selectedUserHouse.getHouseUnit() != null) {
					changeHouse.setHouseUnitID(selectedUserHouse.getHouseUnit().getID());
				}
				if (selectedUserHouse.getHouseFloor() != null) {
					changeHouse.setHouseFloorID(selectedUserHouse.getHouseFloor().getID());
				}

				changeHouse.setRoom(mhouseNumberEditText.getText().toString());
				changeHouse.setVerify(mVerify.getText().toString());
				changeHouse.setCommunityID(PreferencesUtils.getCommunity().getID());

				ResponseHandler<ChangeHouseResp> responseHandler = new ResponseHandler<ChangeHouseResp>() {
					@Override
					public void onStart() {
						showProgressDialog(SwitchResidentAddressActivity.this, "提交中");
					}

					@Override
					public void onFinish(ChangeHouseResp response, String error) {
						hideProgressDialog();
						if (ResponseUtils.checkResponseAndToastError(response, error)) {
							ToastUtil.show(SwitchResidentAddressActivity.this, "验证通过!");
							HouseRoom houseRoom = new HouseRoom();
							houseRoom.setID(response.getRoomID());
							houseRoom.setName(mhouseNumberEditText.getText().toString());
							selectedUserHouse.setHouseRoom(houseRoom);
							PreferencesUtils.setUserHouse(selectedUserHouse);

							if (!isDestroyedApi8()) {
								Intent data = new Intent();
								setResult(RESULT_OK, data);
								AppManager.getAppManager().finishActivity();
							}
						}
					}
				};
				HttpPacketClient.postPacketAsynchronous(changeHouse, ChangeHouseResp.class, responseHandler);
			}
			break;
		}
	}
}

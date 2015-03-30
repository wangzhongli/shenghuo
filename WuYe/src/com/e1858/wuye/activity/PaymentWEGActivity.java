package com.e1858.wuye.activity;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.Fee;
import com.e1858.wuye.protocol.http.FeeType;
import com.e1858.wuye.protocol.http.GetFees;
import com.e1858.wuye.protocol.http.GetFeesResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 缴水，电，燃气费
 * @author jiajia
 *
 */
public class PaymentWEGActivity extends BaseActivity implements OnClickListener {

	private FeeType feeType;
	private TextView currentCity;// 当前城市
	private TextView publicInstructions;// 公共机构
	private TextView userNumber;// 相关户号说明 需设置
	private EditText userNumber_input;// 相关户号输入 需设置hint
	private EditText amount_input;// 相关缴费金额
	private RelativeLayout payment_amount_rel;// 只有燃气费的时候显示 以及处理相关缴费金额
	private Button ok_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_all);
		feeType = (FeeType) getIntent().getExtras().get("feeInfo");
		initView();
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		currentCity = (TextView) findViewById(R.id.currentCity);
		publicInstructions = (TextView) findViewById(R.id.publicInstructions);
		userNumber = (TextView) findViewById(R.id.userNumber);
		userNumber_input = (EditText) findViewById(R.id.userNumber_input);
		amount_input = (EditText) findViewById(R.id.amount_input);
		payment_amount_rel = (RelativeLayout) findViewById(R.id.payment_amount_rel);
		ok_btn = (Button) findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(this);
		initData();
	}

	private void initData() {
		switch (feeType.getID()) {
		case Constant.PAYMENT_TYPE.WATER_PAYMENT:
			bar_title.setText("缴水费");
			userNumber.setText("用水户号:");
			userNumber_input.setHint("请输入您的用水户号");
			break;
		case Constant.PAYMENT_TYPE.ELEC_PAYMENT:
			bar_title.setText("缴电费");
			userNumber.setText("用电户号:");
			userNumber_input.setHint("请输入您的用电户号");
			break;
		case Constant.PAYMENT_TYPE.GAS_PAYMENT:
			bar_title.setText("缴燃气费");
			userNumber.setText("燃气户号:");
			userNumber_input.setHint("请输入您的燃气户号");
			payment_amount_rel.setVisibility(View.VISIBLE);
			amount_input.setHint("请输入您的缴费金额");
			break;
		}
		publicInstructions.setText(feeType.getInstitution());
		currentCity.setText(PreferencesUtils.getCommunity().getCity());

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
		case HttpDefine.GET_FEES_RESP:
			if(null!=(String)msg.obj){
				GetFeesResp resp=JsonUtil.fromJson((String)msg.obj, GetFeesResp.class);
				if(null==resp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
					if(null!=resp.getFees()&&resp.getFees().size()>0){
						Fee fee=resp.getFees().get(0);
						if(feeType.getID()==Constant.PAYMENT_TYPE.GAS_PAYMENT){
							fee.setAmount(Float.parseFloat(amount_input.getText().toString()));
						}
						Intent intent=new Intent(this,PaymentWEGInfoActivity.class);
						Bundle bundle=new Bundle();
						bundle.putSerializable("fee", fee);
						bundle.putSerializable("feeType", feeType);
						intent.putExtras(bundle);
						startActivity(intent);
					
					}else{
						ToastUtil.show(this, "当前户号暂无待缴信息");
					}
					
				}else{
					ToastUtil.show(this, resp.getError());
				}
			}
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ok_btn:
			switch (feeType.getID()) {
			case Constant.PAYMENT_TYPE.WATER_PAYMENT:
				if(StringUtils.isEmpty(userNumber_input.getText().toString())){
					ToastUtil.show(this, "请输入您的用水户号");
				}else{
					openProgressDialog("请求中...");
					GetFees getFees=new GetFees();
					getFees.setCommunityID(PreferencesUtils.getCommunity().getID());
					getFees.setFeeTypeID(Constant.PAYMENT_TYPE.WATER_PAYMENT);
					getFees.setKey(PreferencesUtils.getLoginKey());
					getFees.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					getFees.setWaterNumber(userNumber_input.getText().toString());
					NetUtil.post(Constant.BASE_URL, getFees, handler, HttpDefine.GET_FEES_RESP);
				}
				break;
			case Constant.PAYMENT_TYPE.ELEC_PAYMENT:
				if(StringUtils.isEmpty(userNumber_input.getText().toString())){
					ToastUtil.show(this, "请输入您的用电户号");
				}else{
					openProgressDialog("请求中...");
					GetFees getFees=new GetFees();
					getFees.setCommunityID(PreferencesUtils.getCommunity().getID());
					getFees.setFeeTypeID(Constant.PAYMENT_TYPE.WATER_PAYMENT);
					getFees.setKey(PreferencesUtils.getLoginKey());
					getFees.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					getFees.setElectricNumber(userNumber_input.getText().toString());
					NetUtil.post(Constant.BASE_URL, getFees, handler, HttpDefine.GET_FEES_RESP);
				}
				break;
			case Constant.PAYMENT_TYPE.GAS_PAYMENT:
				if(StringUtils.isEmpty(userNumber_input.getText().toString())){
					ToastUtil.show(this, "请输入您的燃气户号");
				}else if(StringUtils.isEmpty(amount_input.getText().toString())){
					ToastUtil.show(this, "请输入您的缴费金额");
				}else{
					openProgressDialog("请求中...");
					GetFees getFees=new GetFees();
					getFees.setCommunityID(PreferencesUtils.getCommunity().getID());
					getFees.setFeeTypeID(Constant.PAYMENT_TYPE.WATER_PAYMENT);
					getFees.setKey(PreferencesUtils.getLoginKey());
					getFees.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					getFees.setGasNumber(userNumber_input.getText().toString());
					NetUtil.post(Constant.BASE_URL, getFees, handler, HttpDefine.GET_FEES_RESP);
				}
				break;
			}
			break;
		}
	}

}

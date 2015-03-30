package com.e1858.wuye.activity;

import com.e1858.common.Constant;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.Fee;
import com.e1858.wuye.protocol.http.FeeType;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 缴水电燃气费下一步
 * @author jiajia
 *
 */
public class PaymentWEGInfoActivity extends BaseActivity implements OnClickListener{

	private TextView publicInstructions;// 公共机构
	private TextView username;//户主昵称 
	private TextView shouldPaidMoney;//应缴金额 燃气费不显示
	private TextView amount_input;//缴费金额 燃气费不可编辑
	private RelativeLayout shouldPaid_rel;//应缴金额 燃气费不显示
	private Button ok_btn;
	private Fee fee;
	private FeeType feeType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_all_info);
		fee=(Fee) getIntent().getExtras().get("fee");
		feeType=(FeeType)getIntent().getExtras().get("feeInfo");
		initView();
	}
	private void initView(){
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		publicInstructions = (TextView) findViewById(R.id.publicInstructions);
		username = (TextView) findViewById(R.id.username);
		shouldPaidMoney = (TextView) findViewById(R.id.shouldPaidMoney);
		amount_input = (EditText) findViewById(R.id.amount_input);
		shouldPaid_rel = (RelativeLayout) findViewById(R.id.shouldPaid_rel);
		ok_btn = (Button) findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(this);
		initData();
	}
	private void initData(){
		switch(feeType.getID()){
		case Constant.PAYMENT_TYPE.WATER_PAYMENT:
			bar_title.setText("缴水费");
			break;
		case Constant.PAYMENT_TYPE.ELEC_PAYMENT:
			bar_title.setText("缴电费");
			break;
		case Constant.PAYMENT_TYPE.GAS_PAYMENT:
			bar_title.setText("缴燃气费");
			amount_input.setText(fee.getAmount()+"");
			amount_input.setEnabled(false);
			shouldPaid_rel.setVisibility(View.GONE);
			break;
		}
		amount_input.setHint("请输入您的缴费金额");
		shouldPaidMoney.setText("￥"+fee.getAmount());
		publicInstructions.setText(feeType.getInstitution());
		username.setText(fee.getUsername());
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return super.handleMessage(msg);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ok_btn:
			switch(feeType.getID()){
			case Constant.PAYMENT_TYPE.WATER_PAYMENT:
				if(StringUtils.isEmpty(amount_input.getText().toString())){
					ToastUtil.show(this, "请输入您的缴费金额");
				}else{
					//立即缴费
				}
				break;
			case Constant.PAYMENT_TYPE.ELEC_PAYMENT:
				if(StringUtils.isEmpty(amount_input.getText().toString())){
					ToastUtil.show(this, "请输入您的缴费金额");
				}else{
					//立即缴费
				}
				break;
			case Constant.PAYMENT_TYPE.GAS_PAYMENT:
				//处理立即缴费
				break;
			}
			break;
		}
	}

}

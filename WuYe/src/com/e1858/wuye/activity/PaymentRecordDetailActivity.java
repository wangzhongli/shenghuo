package com.e1858.wuye.activity;

import com.e1858.common.Constant;
import com.e1858.common.app.PaymentMethodActivity;
import com.e1858.network.NetUtil;
import com.e1858.utils.DateUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.DeleteFeeBill;
import com.e1858.wuye.protocol.http.DeleteFeeBillResp;
import com.e1858.wuye.protocol.http.FeeBill;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * 缴费记录详细
 * @author jiajia
 *
 */
public class PaymentRecordDetailActivity extends BaseActivity implements OnClickListener{

	private FeeBill feeBill;
	private TextView paymentType;//缴费类型
	private TextView houseInfo_TextView;//房屋信息或者用电户号相对应的修改
	private TextView houseInfo;//房屋信息或者用电户号等等
	private TextView paymentTime;//时间
	private TextView paymentMoney;//缴费金额
	private TextView paymentMethod;//缴费方式
	private TextView paymentState;//缴费状态
	
	private Button ok_btn;//未支付---立即支付  支付的话不显示
	private TextView	paymentDescription;
	private BroadcastReceiver	payReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_record_detail);
		feeBill=(FeeBill) getIntent().getExtras().getSerializable("feeBill");
		initView();
		initData();
		
		payReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				String ID = arg1.getStringExtra(PaymentMethodActivity.IntentKey_TAG);
				if (feeBill.getID().equals(ID)) {
					finish();
				}
			}
		};
		registerReceiver(payReceiver, new IntentFilter(PaymentMethodActivity.BroadcastAction_PaySuccess));
	}
	
	public void onDestroy() {
		unregisterReceiver(payReceiver);
		super.onDestroy();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.obj==null){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
		case HttpDefine.DELETE_FEEBILL_RESP:
			if(null!=(String)msg.obj){
				DeleteFeeBillResp resp=JsonUtil.fromJson((String)msg.obj, DeleteFeeBillResp.class);
				if(null==resp){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
					ToastUtil.show(this,"删除成功");
					this.finish();
				}else{
					ToastUtil.show(this, resp.getError());
				}
				
			}
			msg.obj=null;
			break;
		}
		return super.handleMessage(msg);
	}

	private void initView(){
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText("缴费详情");
		bar_rightBtn=(Button)findViewById(R.id.bar_right_btn);
		bar_rightBtn.setBackgroundResource(R.drawable.delete_pic_bar_btn_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		houseInfo_TextView=(TextView)findViewById(R.id.houseInfo_TextView);
		paymentType=(TextView)findViewById(R.id.paymentType);
		houseInfo=(TextView)findViewById(R.id.houseInfo);
		paymentTime=(TextView)findViewById(R.id.paymentTime);
		paymentMethod=(TextView)findViewById(R.id.paymentMethod);
		paymentMoney=(TextView)findViewById(R.id.paymentMoney);
		paymentState=(TextView)findViewById(R.id.paymentState);
		paymentDescription=(TextView)findViewById(R.id.paymentDescription);
		ok_btn=(Button)findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(this);
		bar_rightBtn.setOnClickListener(this);
	}
	
	private void initData(){
		switch(feeBill.getFeeType().getID()){
		case Constant.PAYMENT_TYPE.WUYE_PAYMENT:
			houseInfo_TextView.setText("房屋信息:");
			break;
		case Constant.PAYMENT_TYPE.WATER_PAYMENT:
			houseInfo_TextView.setText("用水户号:");
			break;
		case Constant.PAYMENT_TYPE.ELEC_PAYMENT:
			houseInfo_TextView.setText("用电户号:");
			break;
		case Constant.PAYMENT_TYPE.GAS_PAYMENT:
			houseInfo_TextView.setText("燃气户号:");
			break;
		}
		paymentType.setText(feeBill.getFeeType().getName());
		paymentMethod.setText(feeBill.getPaymentType());
		paymentMoney.setText("￥"+feeBill.getAmount());
		paymentTime.setText(DateUtil.dateToZh(feeBill.getCreateTime()));
		houseInfo.setText(feeBill.getHouseRoom());
		paymentDescription.setText(feeBill.getEdescription());
		switch(feeBill.getState()){
		case 1:
			paymentState.setText("已支付");
			ok_btn.setVisibility(View.GONE);
			break;
		case 2:
			bar_rightBtn.setVisibility(View.VISIBLE);
			ok_btn.setVisibility(View.VISIBLE);
			ok_btn.setText("立即支付");
			paymentState.setText("未支付");
			break;
		case 3:
			paymentState.setText("已关闭");
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ok_btn:
			Intent intent=new Intent(this,PaymentMethodActivity.class);
			intent.putExtra(PaymentMethodActivity.IntentKey_TAG, feeBill.getID());
			intent.putExtra(PaymentMethodActivity.IntentKey_Amount, feeBill.getAmount());
			intent.putExtra(PaymentMethodActivity.IntentKey_PaymentParam, feeBill.getPaymentParam());
			startActivity(intent);
			break;
		case R.id.bar_right_btn:
			createDeleteDialog();
			break;
		}
	}
	
	private void createDeleteDialog() {
		final Dialog deleteDialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		deleteDialog.setContentView(R.layout.delete_pic_dialog);
		// 设置样式
		Window window = deleteDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.8f;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.pic_dialog_style);
		TextView contentTextView = (TextView) deleteDialog
				.findViewById(R.id.contentTextView);
		Button deleteBtn = (Button) deleteDialog.findViewById(R.id.delete_btn);
		Button cancleBtn = (Button) deleteDialog.findViewById(R.id.cancel_btn);
		contentTextView.setText("要删除此次记录吗?");
		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteDialog.dismiss();
			}
		});
		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteDialog.dismiss();
				if(application.isNetworkAvailable()){
					openProgressDialog("删除中...");
					DeleteFeeBill deleteFeeBill=new DeleteFeeBill();
					deleteFeeBill.setCommunityID(PreferencesUtils.getCommunity().getID());
					deleteFeeBill.setKey(PreferencesUtils.getLoginKey());
					deleteFeeBill.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					deleteFeeBill.setID(feeBill.getID());
					NetUtil.post(Constant.BASE_URL, deleteFeeBill, handler, HttpDefine.DELETE_FEEBILL_RESP);
				}else{
					ToastUtil.show(PaymentRecordDetailActivity.this, getResources().getString(R.string.network_fail));
				}
				
			}
		});
		deleteDialog.show();

	}
	
}

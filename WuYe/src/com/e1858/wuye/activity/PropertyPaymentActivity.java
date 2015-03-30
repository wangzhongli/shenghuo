package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.common.app.PaymentMethodActivity;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.Fee;
import com.e1858.wuye.protocol.http.FeeBill;
import com.e1858.wuye.protocol.http.GenerateFeeBill;
import com.e1858.wuye.protocol.http.GenerateFeeBillResp;
import com.e1858.wuye.protocol.http.GetFees;
import com.e1858.wuye.protocol.http.GetFeesResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.PointGetMyProfileRequest;
import com.e1858.wuye.protocol.http.PointGetMyProfileResp;
import com.e1858.wuye.protocol.http.PointProfileBean;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
public class PropertyPaymentActivity extends BaseActivity implements OnClickListener{
	private TextView house_info;//房屋信息
//	private TextView shouldPaid;//应缴费金额
	private LinearLayout hasData_lin;//根据是否有缴费信息  判断其是否显示
	private ListView payListView;//待缴费列表
	private List<Fee> fees=new ArrayList<Fee>();
	private PayAdapter adapter=null;
	private CheckBox all_checked;
	private CheckBox checkBox_points;
//	private CheckBox  availableBalance_checked;
//	private TextView  availableBalance;
	private TextView summaryMoney;
	private Button  okBtn;
	private LinearLayout bottom_Rel;//底部立即缴费布局
	private float summaryPaidMoney=0;
	private int pointsPaid=0;
	protected PointProfileBean	pointProfile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_wuye);
		initView();
		//loadData();
		loadPoints();
		updateCheckBoxPoints();
	}

	private void initView(){
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText("缴物业费");
		house_info=(TextView)findViewById(R.id.house_info);
//		shouldPaid=(TextView)findViewById(R.id.shouldPaid);
		hasData_lin=(LinearLayout)findViewById(R.id.hasData_lin);
		payListView=(ListView)findViewById(R.id.listview);
		all_checked=(CheckBox)findViewById(R.id.all_checked);
		checkBox_points = (CheckBox)findViewById(R.id.checkBox_points);
		checkBox_points.setEnabled(false);
//		availableBalance_checked=(CheckBox)findViewById(R.id.availableBalance_checked);
//		availableBalance=(TextView)findViewById(R.id.availableBalance);
		okBtn=(Button)findViewById(R.id.ok_btn);
		bottom_Rel=(LinearLayout)findViewById(R.id.rl_bottom);
		summaryMoney=(TextView)findViewById(R.id.summaryMoney);
		if (null != PreferencesUtils.getUserHouse())
		{
			house_info.setText(Util.getHouseInfo(true));
		}

		house_info.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SwitchResidentAddressActivity.class);
				startActivityForResult(intent, Constant.HOUSE_INFO_RESULT_CODE);
			}
		});
		all_checked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					for(int i=0;i<fees.size();i++){
						fees.get(i).setIsChecked(1);
					}
					all_checked.setButtonDrawable(R.drawable.checkbox_pressed);
				}else{
					for(int i=0;i<fees.size();i++){
						fees.get(i).setIsChecked(0);
					}
					all_checked.setButtonDrawable(R.drawable.checkbox_normal);
				}
				changeSummaryFee();
			}
		});
		okBtn.setOnClickListener(this);
	}
	private void updateCheckBoxPoints() {
		pointsPaid = 0;
		if (summaryPaidMoney < 0.01f || pointProfile == null || pointProfile.getExchangeRate() <= 0 
				|| pointProfile.getPoints() < pointProfile.getExchangeRate()/100) {
			checkBox_points.setText("积分不可用");
			checkBox_points.setEnabled(false);
			checkBox_points.setChecked(false);
			return;
		}
		checkBox_points.setEnabled(true);
		int rate = (int) pointProfile.getExchangeRate();
		int points = Math.min((int)(summaryPaidMoney*rate), pointProfile.getPoints());
		points = points * 100 / rate * rate /100;//将可用积分修正为一分钱的倍数
		float pointsPaidMoney = 1f * points / rate;
		pointsPaid = points;
		checkBox_points.setText(String.format("使用积分%d抵扣%.2f元", points, pointsPaidMoney));
	}
	
	private void loadPoints() {
		ResponseHandler<PointGetMyProfileResp> responseHandler = new ResponseHandler<PointGetMyProfileResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(PointGetMyProfileResp response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					pointProfile = response.getMyProfile();
					updateCheckBoxPoints();
				}
			}
		};

		PointGetMyProfileRequest request = new PointGetMyProfileRequest();
		HttpPacketClient.postPacketAsynchronous(request, PointGetMyProfileResp.class, responseHandler, true);
	}
	private void loadData(){
		if(application.isNetworkAvailable()){
			openProgressDialog("加载中...");
			GetFees getFees=new GetFees();
			getFees.setCommunityID(PreferencesUtils.getCommunity().getID());
			getFees.setFeeTypeID(Constant.PAYMENT_TYPE.WUYE_PAYMENT);
			getFees.setKey(PreferencesUtils.getLoginKey());
			getFees.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getFees, handler, HttpDefine.GET_FEES_RESP);
		}else{
			hasData_lin.setVisibility(View.GONE);
			bottom_Rel.setVisibility(View.GONE);
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}
	
	
	public void onResume() {
		super.onResume();
		loadData();
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
		case HttpDefine.GET_FEES_RESP:
			closeProgressDialog();
			if(null!=(String)msg.obj){
				GetFeesResp resp=JsonUtil.fromJson((String)msg.obj,GetFeesResp.class);
				if(null==resp){
					break;
				}
				fees.clear();
				if(resp.getRet()==HttpDefine.RESPONSE_SUCCESS){
					if(resp.getFees()!=null&&resp.getFees().size()>0){
						hasData_lin.setVisibility(View.VISIBLE);
						bottom_Rel.setVisibility(View.VISIBLE);
						fees.addAll(resp.getFees());
						initData(fees);
					}else{
						bottom_Rel.setVisibility(View.GONE);
						hasData_lin.setVisibility(View.GONE);
						ToastUtil.show(this, "当前房屋暂无待缴信息");
					}
				}else{
					bottom_Rel.setVisibility(View.GONE);
					hasData_lin.setVisibility(View.GONE);
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj=null;
			break;
		case Constant.FAIL_CODE:
			closeProgressDialog();
			hasData_lin.setVisibility(View.GONE);
			bottom_Rel.setVisibility(View.GONE);
			if (msg.obj != null) {
				ToastUtil.show(this, msg.obj.toString());
			}
			msg.obj=null;
			break;
		case Constant.UPDATE_TEXT:
			if(null!=msg.obj){
				//解决textview 不显示问题
				summaryMoney.setText(String.format("合计:￥%.2f", summaryPaidMoney));
				Log.i("summaryText", "===="+summaryMoney.getText().toString());
				updateCheckBoxPoints();
			}
			msg.obj=null;
			break;
//		case HttpDefine.GENERATE_FEEBILL_RESP:
//			closeProgressDialog();
//			if((String)msg.obj!=null){
//				GenerateFeeBillResp resp=JsonUtil.fromJson((String)msg.obj, GenerateFeeBillResp.class);
//				if(null==resp){
//					break;
//				}
//				if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
//					FeeBill feeBill=resp.getFeeBill();
//					Intent intent=new Intent(this,PaymentMethodActivity.class);
//					intent.putExtra(PaymentMethodActivity.IntentKey_Amount, feeBill.getAmount());
//					intent.putExtra(PaymentMethodActivity.IntentKey_PaymentParam, feeBill.getPaymentParam());
//					startActivity(intent);
//				}else{
//					ToastUtil.show(this, resp.getError());
//				}
//			}
//			msg.obj=null;
//			break;
			
		}
		
		return super.handleMessage(msg);
	}
	private void initData(List<Fee> list){
		summaryMoney.setText("合计: ￥0.0");
		adapter=new PayAdapter();
		payListView.setAdapter(adapter);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case Constant.HOUSE_INFO_RESULT_CODE:
			if (PreferencesUtils.getUserHouse() != null)
			{
				house_info.setText(Util.getHouseInfo(true));
				loadData();
			}

			break;
		}
	}

	class PayAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fees.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return fees.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder=null;
			if(null==convertView){
				viewHolder=new ViewHolder();
				convertView=LayoutInflater.from(PropertyPaymentActivity.this).inflate(R.layout.payment_wuye_item, null);
				viewHolder.toPaidMonth=(TextView)convertView.findViewById(R.id.toPaidMonth);
				viewHolder.toPaidMonth_checked=(CheckBox)convertView.findViewById(R.id.toPaidMonth_checked);
				viewHolder.toPaidMoney=(TextView)convertView.findViewById(R.id.toPaidMoney);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder)convertView.getTag();
			}
			viewHolder.toPaidMonth.setText(fees.get(position).getEdescription());
			viewHolder.toPaidMoney.setText("￥"+fees.get(position).getAmount());
			if(fees.get(position).getIsChecked()==1){
				viewHolder.toPaidMonth_checked.setButtonDrawable(R.drawable.checkbox_pressed);
		     }else{
				viewHolder.toPaidMonth_checked.setButtonDrawable(R.drawable.checkbox_normal);
		     }
		    
			viewHolder.toPaidMonth_checked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						fees.get(position).setIsChecked(1);
					}else{    
						fees.get(position).setIsChecked(0);
					}
					if(!judgeIsCheced()){
						all_checked.setButtonDrawable(R.drawable.checkbox_normal);
					}
					changeSummaryFee();
				}
			});
			return convertView;
		}
		
	}
	class ViewHolder{
		private TextView toPaidMonth;
		private CheckBox toPaidMonth_checked;
		private TextView toPaidMoney;
	}
	private void changeSummaryFee(){
		adapter.notifyDataSetChanged();
		summaryPaidMoney=0;
		for(int i=0;i<fees.size();i++){
			if(fees.get(i).getIsChecked()==1){
				summaryPaidMoney+=fees.get(i).getAmount();
			}else{
				summaryPaidMoney+=0;
			}
		}
		Message message=handler.obtainMessage(Constant.UPDATE_TEXT, summaryPaidMoney);
		handler.sendMessage(message);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ok_btn:
			if(judgeIsCheced()){
				//有被选择项  处理立即缴费
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<fees.size();i++){
					if(fees.get(i).getIsChecked()==1){
						sb.append(fees.get(i).getID()).append(",");
					}
				}
				sb.deleteCharAt(sb.length()-1);

				final GenerateFeeBill generateFeeBill = new GenerateFeeBill();
				generateFeeBill.setAmount(summaryPaidMoney);
				generateFeeBill.setKey(PreferencesUtils.getLoginKey());
				generateFeeBill.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
				generateFeeBill.setFeeTypeID(Constant.PAYMENT_TYPE.WUYE_PAYMENT);
				generateFeeBill.setCommunityID(PreferencesUtils.getCommunity().getID());
				generateFeeBill.setFeeIDs(Arrays.asList(sb.toString().split(",")));
				generateFeeBill.setPoints(checkBox_points.isChecked() ? pointsPaid : 0);
				ResponseHandler<GenerateFeeBillResp> responseHandler = new ResponseHandler<GenerateFeeBillResp>() {

					@Override
					public void onStart() {
						openProgressDialog("订单提交中...");
					}

					@Override
					public void onFinish(GenerateFeeBillResp response, String error) {
						closeProgressDialog();
						if (ResponseUtils.checkResponseAndToastError(response, error)) {
							if (response.isPointsPaidOff()) {
								ToastUtil.show(getApplication(), "已使用积分结清订单");
								loadData();
								return;
							}
							FeeBill feeBill = response.getFeeBill();
							Intent intent = new Intent(PropertyPaymentActivity.this, PaymentMethodActivity.class);
							intent.putExtra(PaymentMethodActivity.IntentKey_Amount, feeBill.getAmount());
							intent.putExtra(PaymentMethodActivity.IntentKey_PaymentParam, feeBill.getPaymentParam());
							int points = generateFeeBill.getPoints() ;
							if (points> 0) {
								intent.putExtra(PaymentMethodActivity.IntentKey_Description,checkBox_points.getText().toString());
							}
							startActivity(intent);
						}
					}
				};
				HttpPacketClient.postPacketAsynchronous(generateFeeBill, GenerateFeeBillResp.class, responseHandler,
						true);
				
			}else{
				ToastUtil.show(this, "请选择您的待缴月份");
			}
			break;
		}
	}

	public boolean judgeIsCheced(){
		for(int i=0;i<fees.size();i++){
			if(fees.get(i).getIsChecked()==1){
				return true;
			}
		}
		return false;
	}
}

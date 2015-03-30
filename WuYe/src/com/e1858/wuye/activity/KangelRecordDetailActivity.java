package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.KangelCancelOrder;
import com.e1858.wuye.protocol.http.KangelCancelOrderResp;
import com.e1858.wuye.protocol.http.KangelDeleteOrder;
import com.e1858.wuye.protocol.http.KangelDeleteOrderResp;
import com.e1858.wuye.protocol.http.KangelGetOrder;
import com.e1858.wuye.protocol.http.KangelGetOrderResp;
import com.e1858.wuye.protocol.http.KangelOrder;
import com.e1858.wuye.protocol.http.KangelOrderStatus;
import com.hg.android.widget.MyListView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 康洁洗衣订单详情
 * @author jiajia
 *
 */
public class KangelRecordDetailActivity extends BaseActivity implements
		OnClickListener {
	private String  uuid;
	private TextView memberName;
	private TextView mobilephone;
	private TextView orderSn;
	private TextView createTime;
	private TextView address;
	private MyListView listview;
	private TextView description;
	private TextView bar_right_text;
	private KangelOrder kangelOrder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kangel_order_detail);
		uuid = getIntent().getExtras().getString("uuid");
		initView();
		loadData();
	}
	private void loadData(){
		//加载数据
		if(application.isNetworkAvailable()){
			openProgressDialog("加载中...");
			KangelGetOrder getOrder=new KangelGetOrder();
			getOrder.setKey(PreferencesUtils.getLoginKey());
			getOrder.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			getOrder.setCommunityID(PreferencesUtils.getCommunity().getID());
			getOrder.setUuid(uuid);
			NetUtil.post(Constant.BASE_URL, getOrder, handler, HttpDefine.KANGEL_GET_ORDER_RESP);
		}else{
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}
	
	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_rightBtn = (Button) findViewById(R.id.bar_right_btn);
		bar_right_text=(TextView)findViewById(R.id.bar_right_text);
		bar_title.setText("订单详情");
		memberName = (TextView) findViewById(R.id.memberName);
		orderSn = (TextView) findViewById(R.id.orderSn);
		createTime = (TextView) findViewById(R.id.createTime);
		mobilephone = (TextView) findViewById(R.id.mobilephone);
		address = (TextView) findViewById(R.id.address);
		description = (TextView) findViewById(R.id.description);
		listview = (MyListView) findViewById(R.id.listview);
		
	}

	private void initData() {
		if (kangelOrder.getOrderStatus().get(0).getStatus() == Constant.ORDER_STATUS.WAIT) {
			bar_right_text.setText("取消");
			bar_right_text.setVisibility(View.VISIBLE);
			bar_right_text.setOnClickListener(this);
		} else if (kangelOrder.getOrderStatus().get(0).getStatus()== Constant.ORDER_STATUS.SIGN||kangelOrder.getOrderStatus().get(0).getStatus()==Constant.ORDER_STATUS.CANCLE) {
			bar_rightBtn
					.setBackgroundResource(R.drawable.delete_pic_bar_btn_background);
			bar_rightBtn.setVisibility(View.VISIBLE);
			bar_rightBtn.setOnClickListener(this);
		}
		orderSn.setText(kangelOrder.getOrderSn());
		createTime.setText(kangelOrder.getAppointmentTime());
		memberName.setText(kangelOrder.getPlatMemberName());
		mobilephone.setText(kangelOrder.getMobilephone());
		address.setText(kangelOrder.getTakeDetailAddress());
		if(StringUtils.isEmpty(kangelOrder.getEdescription())){
			description.setText("无");
		}else{
			description.setText(kangelOrder.getEdescription());
		}
		
		// 处理跟踪记录
		List<KangelOrderStatus> orderStatus=new ArrayList<KangelOrderStatus>();
		if(kangelOrder.getOrderStatus()!=null){
			orderStatus.addAll(kangelOrder.getOrderStatus());
		}
		/*OrderInfo orderInfo_one=new OrderInfo();
		OrderInfo orderInfo_two=new OrderInfo();
		OrderInfo orderInfo_three=new OrderInfo();
		OrderInfo orderInfo_four=new OrderInfo();
		switch(kangelOrder.getOrderStatus()){
		//待接单
		case 1:
			orderInfo_one.setTitle("待接单");
			orderInfo_one.setCreateTime(DateUtil.dateToZh(kangelOrder.getCreateDate()));
			orderInfos.addFirst(orderInfo_one);
			break;
		case 2:
			orderInfo_one.setTitle("待接单");
			orderInfo_one.setCreateTime(DateUtil.dateToZh(kangelOrder.getCreateDate()));
			orderInfos.addFirst(orderInfo_one);
			if(StringUtils.isEmpty(kangelOrder.getPlatSalesMan())){
				orderInfo_two.setTitle("收件中");
			}else{
				orderInfo_two.setTitle("收件中  取件人："+kangelOrder.getPlatSalesMan());
			}
			orderInfo_two.setCreateTime(DateUtil.dateToZh(kangelOrder.getCreateDate()));
			orderInfos.addFirst(orderInfo_two);
			break;
		case 3:
			orderInfo_one.setTitle("待接单");
			orderInfo_one.setCreateTime(DateUtil.dateToZh(kangelOrder.getCreateDate()));
			orderInfos.addFirst(orderInfo_one);
			if(StringUtils.isEmpty(kangelOrder.getPlatSalesMan())){
				orderInfo_two.setTitle("收件中");
			}else{
				orderInfo_two.setTitle("收件中  取件人："+kangelOrder.getPlatSalesMan());
			}
			
			orderInfo_two.setCreateTime(DateUtil.dateToZh(kangelOrder.getTakeTime()));
			orderInfos.addFirst(orderInfo_two);
			orderInfo_three.setTitle("洗涤中");
			orderInfo_three.setCreateTime(DateUtil.dateToZh(kangelOrder.getWashTime()));
			orderInfos.addFirst(orderInfo_three);
			break;
		case 4:
			orderInfo_one.setTitle("待接单");
			orderInfo_one.setCreateTime(DateUtil.dateToZh(kangelOrder.getCreateDate()));
			orderInfos.addFirst(orderInfo_one);
			if(StringUtils.isEmpty(kangelOrder.getPlatSalesMan())){
				orderInfo_two.setTitle("收件中");
			}else{
				orderInfo_two.setTitle("收件中  取件人："+kangelOrder.getPlatSalesMan());
			}
			orderInfo_two.setCreateTime(DateUtil.dateToZh(kangelOrder.getTakeTime()));
			orderInfos.addFirst(orderInfo_two);
			orderInfo_three.setTitle("洗涤中");
			orderInfo_three.setCreateTime(DateUtil.dateToZh(kangelOrder.getWashTime()));
			orderInfos.addFirst(orderInfo_three);
			orderInfo_four.setTitle("已签收");
			orderInfo_four.setCreateTime(DateUtil.dateToZh(kangelOrder.getReceiveTime()));
			orderInfos.addFirst(orderInfo_four);
			break;
		case 5://已取消  对应内容取消成功
			break;
		case 6://取消中  
			break;
		case 7://提交失败 对应内容 交易关闭
			break;
		}*/
		listview.setAdapter(new OrderStatusAdapter(orderStatus));

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			return true;
		}
		switch (msg.what) {
		case HttpDefine.KANGEL_GET_ORDER_RESP:
			closeProgressDialog();
			if(null!=(String)msg.obj){
				KangelGetOrderResp resp=JsonUtil.fromJson((String) msg.obj, KangelGetOrderResp.class);
				if(resp==null){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
					kangelOrder=resp.getOrder();
					initData();
				}else{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj=null;
			
			
			break;
		case HttpDefine.KANGEL_CANCLE_ORDER_RESP:
			if(null!=(String)msg.obj){
				KangelCancelOrderResp resp=JsonUtil.fromJson((String) msg.obj, KangelCancelOrderResp.class);
				if(resp==null){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
					ToastUtil.show(this, "取消订单成功");
					this.finish();
				}else{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj=null;
			break;
		case HttpDefine.KANGEL_DELETE_ORDER_RESP:
			if (null != (String) msg.obj) {
				KangelDeleteOrderResp resp=JsonUtil.fromJson((String) msg.obj, KangelDeleteOrderResp.class);
				if(resp==null){
					break;
				}
				if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
					ToastUtil.show(this, "删除订单成功");
					this.finish();
				}else{
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
		case R.id.bar_right_btn:
			// 删除，取消操作
			createDeleteDialog();
			break;
		case R.id.bar_right_text:
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
		if (kangelOrder.getOrderStatus().get(0).getStatus()== Constant.ORDER_STATUS.WAIT) {
			contentTextView.setText("要取消当前订单吗?");
		} else if (kangelOrder.getOrderStatus().get(0).getStatus() == Constant.ORDER_STATUS.SIGN||kangelOrder.getOrderStatus().get(0).getStatus() == Constant.ORDER_STATUS.CANCLE) {
			contentTextView.setText("要删除当前订单吗?");
		}
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
				if (kangelOrder.getOrderStatus().get(0).getStatus() == Constant.ORDER_STATUS.WAIT) {
					KangelCancelOrder cancelOrder = new KangelCancelOrder();
					cancelOrder.setCommunityID(PreferencesUtils.getCommunity()
							.getID());
					cancelOrder.setKey(PreferencesUtils.getLoginKey());
					cancelOrder.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
					cancelOrder.setUuid(kangelOrder.getUuid());
					NetUtil.post(Constant.BASE_URL, cancelOrder, handler,
							HttpDefine.KANGEL_CANCLE_ORDER_RESP);
				} else if (kangelOrder.getOrderStatus().get(0).getStatus()== Constant.ORDER_STATUS.SIGN||kangelOrder.getOrderStatus().get(0).getStatus() == Constant.ORDER_STATUS.CANCLE) {
					KangelDeleteOrder deleteOrder = new KangelDeleteOrder();
					deleteOrder.setCommunityID(PreferencesUtils.getCommunity()
							.getID());
					deleteOrder.setKey(PreferencesUtils.getLoginKey());
					deleteOrder.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()	+ Constant.TokenSalt));
					deleteOrder.setUuid(kangelOrder.getUuid());
					NetUtil.post(Constant.BASE_URL, deleteOrder, handler,
							HttpDefine.KANGEL_DELETE_ORDER_RESP);
				}
			}
		});
		deleteDialog.show();

	}
	
	class OrderStatusAdapter extends BaseAdapter{
		private List<KangelOrderStatus> list;
		public OrderStatusAdapter(List<KangelOrderStatus> list){
			this.list=list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder=null;
			if(null==convertView){
				viewHolder=new ViewHolder();
				convertView=LayoutInflater.from(KangelRecordDetailActivity.this).inflate(R.layout.view_logistics_detail, null);
				viewHolder.viewOne=(View)convertView.findViewById(R.id.viewOne);
				viewHolder.dividerLine=(View)convertView.findViewById(R.id.dividerLine);
				viewHolder.title=(TextView)convertView.findViewById(R.id.title);
				viewHolder.date=(TextView)convertView.findViewById(R.id.date);
				viewHolder.icon=(ImageView)convertView.findViewById(R.id.icon);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder)convertView.getTag();
			}
			if(position==0){
				viewHolder.title.setTextColor(getResources().getColor(R.color.green));
				viewHolder.date.setTextColor(getResources().getColor(R.color.green));
				viewHolder.viewOne.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				viewHolder.icon.getLayoutParams().height=(int) getResources().getDimension(R.dimen.dimen_20);
				viewHolder.icon.getLayoutParams().width=(int) getResources().getDimension(R.dimen.dimen_20);
				viewHolder.icon.setBackgroundResource(R.drawable.order_state_now_icon);
			}else{
				viewHolder.title.setTextColor(getResources().getColor(R.color.title_textColor));
				viewHolder.date.setTextColor(getResources().getColor(R.color.notes_textColor));
				viewHolder.viewOne.getLayoutParams().height=(int) getResources().getDimension(R.dimen.dimen_15);
				viewHolder.icon.getLayoutParams().height=(int) getResources().getDimension(R.dimen.dimen_15);
				viewHolder.icon.getLayoutParams().width=(int) getResources().getDimension(R.dimen.dimen_15);
				viewHolder.icon.setBackgroundResource(R.drawable.order_state_icon);
			}
			viewHolder.date.setText(list.get(position).getCreateTime());
			viewHolder.title.setText(list.get(position).getName()+"  "+list.get(position).getEdescription());
			if(position==list.size()-1){
				viewHolder.dividerLine.setVisibility(View.GONE);
			}else{
				viewHolder.dividerLine.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
	}
	class ViewHolder{
		private View viewOne;
		private View dividerLine;
		private ImageView icon;
		private TextView title;
		private TextView date;
	}
}

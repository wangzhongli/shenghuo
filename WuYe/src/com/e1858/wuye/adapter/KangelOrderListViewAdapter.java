package com.e1858.wuye.adapter;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DateUtil;
import com.e1858.utils.Encrypt;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.KangelRecordDetailActivity;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.KangelCancelOrder;
import com.e1858.wuye.protocol.http.KangelDeleteOrder;
import com.e1858.wuye.protocol.http.KangelOrder;

public class KangelOrderListViewAdapter extends BaseAdapter {
	private Context context;
	private List<KangelOrder> list;
	private static Handler handler;
	private static int communityID;
	private static String key;

	public KangelOrderListViewAdapter(Context context, List<KangelOrder> list,
			Handler handler, int communityID, String key) {
		this.context = context;
		this.list = list;
		this.handler = handler;
		this.communityID = communityID;
		this.key = key;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.kangel_order_record_item, null);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.state = (TextView) convertView.findViewById(R.id.state);
			viewHolder.handlerBtn = (TextView) convertView
					.findViewById(R.id.handlerBtn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 1：待接单 2：收件中 3：执行中 4：已签收 5:已取消
		switch (list.get(position).getOrderStatus().get(0).getStatus()) {
		case Constant.ORDER_STATUS.WAIT:
			viewHolder.handlerBtn.setVisibility(View.VISIBLE);
			viewHolder.handlerBtn.setText("取消订单");
			break;
		/*
		 * case 2: viewHolder.state.setText("收件中"); break; case 3:
		 * viewHolder.state.setText("执行中"); break;
		 */
		case Constant.ORDER_STATUS.SIGN:
			viewHolder.handlerBtn.setVisibility(View.VISIBLE);
			viewHolder.handlerBtn.setText("删除订单");
			break;
		case Constant.ORDER_STATUS.CANCLE:
			viewHolder.handlerBtn.setVisibility(View.VISIBLE);
			viewHolder.handlerBtn.setText("删除订单");
			break;
			default:
				viewHolder.handlerBtn.setVisibility(View.GONE);
				break;
		/*
		 * case 5: viewHolder.state.setText("已取消"); break; case 6:
		 * viewHolder.state.setText("取消中"); break; case 7:
		 * viewHolder.state.setText("提交失败"); break;
		 */
		}
		viewHolder.state.setText(list.get(position).getOrderStatus().get(0)
				.getName());
		viewHolder.title.setText("订单号: " + list.get(position).getOrderSn());
		viewHolder.time.setText("订单生成时间: "
				+ DateUtil.dateToZh(list.get(position).getCreateDate()));
		viewHolder.handlerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 弹出提示框
				createCallDialog(context, list.get(position).getOrderStatus()
						.get(0).getStatus(), list.get(position).getUuid());
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						KangelRecordDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uuid", list.get(position).getUuid());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView title;
		private TextView time;
		private TextView state;
		private TextView handlerBtn;
	}

	public static void createCallDialog(Context context, final int state,
			final String uuid) {
		final Dialog handlerDialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		handlerDialog.setContentView(R.layout.kangel_handle_dialog);
		Window window = handlerDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.8f;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		TextView callContent = (TextView) handlerDialog
				.findViewById(R.id.dialog_call_content);
		Button okBtn = (Button) handlerDialog.findViewById(R.id.ok_btn);
		Button cancleBtn = (Button) handlerDialog.findViewById(R.id.cancel_btn);
		if (state == Constant.ORDER_STATUS.WAIT) {
			callContent.setText("要取消当前订单吗?");
		} else if (state == Constant.ORDER_STATUS.SIGN||state==Constant.ORDER_STATUS.CANCLE) {
			callContent.setText("要删除当前订单吗?");
		}
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handlerDialog.dismiss();
				if (state == Constant.ORDER_STATUS.WAIT) {
					// 取消
					KangelCancelOrder cancelOrder = new KangelCancelOrder();
					cancelOrder.setCommunityID(communityID);
					cancelOrder.setKey(key);
					cancelOrder.setToken(Encrypt.MD5(key + Constant.TokenSalt));
					cancelOrder.setUuid(uuid);
					NetUtil.post(Constant.BASE_URL, cancelOrder, handler,
							HttpDefine.KANGEL_CANCLE_ORDER_RESP);
				} else if (state == Constant.ORDER_STATUS.SIGN||state==Constant.ORDER_STATUS.CANCLE) {
					// 删除
					KangelDeleteOrder deleteOrder = new KangelDeleteOrder();
					deleteOrder.setCommunityID(communityID);
					deleteOrder.setKey(key);
					deleteOrder.setToken(Encrypt.MD5(key + Constant.TokenSalt));
					deleteOrder.setUuid(uuid);
					NetUtil.post(Constant.BASE_URL, deleteOrder, handler,
							HttpDefine.KANGEL_DELETE_ORDER_RESP);
				}
			}
		});
		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handlerDialog.dismiss();
			}
		});
		handlerDialog.show();
	}
}

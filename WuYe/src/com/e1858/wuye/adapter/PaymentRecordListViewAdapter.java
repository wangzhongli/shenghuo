package com.e1858.wuye.adapter;

import java.util.List;
import com.e1858.common.Constant;
import com.e1858.utils.DateUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.PaymentRecordDetailActivity;
import com.e1858.wuye.protocol.http.FeeBill;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentRecordListViewAdapter extends BaseAdapter
{
	private Context context;
	private List<FeeBill> list;

	public PaymentRecordListViewAdapter(Context context,List<FeeBill> list){
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.payment_record_item, null);
			viewHolder.feeType=(TextView)convertView.findViewById(R.id.feeType);
			viewHolder.feeTime=(TextView)convertView.findViewById(R.id.feeTiem);
			viewHolder.feeAmount=(TextView)convertView.findViewById(R.id.feeAmount);
			viewHolder.feeState=(TextView)convertView.findViewById(R.id.feeState);
			viewHolder.icon=(ImageView)convertView.findViewById(R.id.icon);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		switch(list.get(position).getFeeType().getID()){
		case Constant.PAYMENT_TYPE.WUYE_PAYMENT:
			viewHolder.icon.setBackgroundResource(R.drawable.property_icon);
			break;
		case Constant.PAYMENT_TYPE.WATER_PAYMENT:
			viewHolder.icon.setBackgroundResource(R.drawable.water_icon);
			break;
		case Constant.PAYMENT_TYPE.ELEC_PAYMENT:
			viewHolder.icon.setBackgroundResource(R.drawable.electric_icon);
			break;
		case Constant.PAYMENT_TYPE.GAS_PAYMENT:
			viewHolder.icon.setBackgroundResource(R.drawable.gas_icon);
			break;
		}
		viewHolder.feeType.setText(list.get(position).getFeeType().getName());
		switch(list.get(position).getState()){
		case 1:
			viewHolder.feeState.setText("已支付");
			break;
		case 2:
			viewHolder.feeState.setText("未支付");
			break;
		case 3:
			viewHolder.feeState.setText("已关闭");
			break;
		}
		viewHolder.feeAmount.setText("￥"+list.get(position).getAmount());
		viewHolder.feeTime.setText(DateUtil.dateToZh(list.get(position).getCreateTime()));
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,PaymentRecordDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("feeBill", list.get(position));
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder{
		private ImageView icon;
		private TextView feeTime;
		private TextView feeType;
		private TextView feeAmount;
		private TextView feeState;
	}
}

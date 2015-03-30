package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.common.Constant;
import com.e1858.utils.DateUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.MaintenanceDetailActivity;
import com.e1858.wuye.protocol.http.Maintenance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MaintenanceListViewAdapter extends BaseAdapter
{
	private Context context;
	private List<Maintenance> list;

	public MaintenanceListViewAdapter(Context context,List<Maintenance> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.listview_item, null);
			viewHolder.title=(TextView)convertView.findViewById(R.id.item_title);
			viewHolder.time=(TextView)convertView.findViewById(R.id.item_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		if(null!=list.get(position).getType()){
			viewHolder.title.setText("["+list.get(position).getType().getName()+"]"+list.get(position).getContent());
		}else{
			viewHolder.title.setText("[无]"+list.get(position).getContent());
		}
		viewHolder.time.setText(DateUtil.dateToZh(list.get(position).getCreateTime()));
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,MaintenanceDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt(Constant.DETAIL_ID, list.get(position).getID());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder{
		private TextView title;
		private TextView time;
	}
}

package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.wuye.R;
import com.hg.android.widget.CityDBManager.AreaEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SysAreaAdapter extends BaseAdapter
{
	private Context context;
	private List<AreaEntity> list;
	public SysAreaAdapter(Context context,List<AreaEntity> list){
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
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.area_item, null);
			viewHolder.areaName=(TextView)convertView.findViewById(R.id.areaName);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.areaName.setText(list.get(position).getName());
		return convertView;
	}

	class ViewHolder{
		private TextView areaName;
	}
}

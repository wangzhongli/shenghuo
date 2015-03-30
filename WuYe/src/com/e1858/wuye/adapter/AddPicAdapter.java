package com.e1858.wuye.adapter;

import java.util.LinkedList;

import com.e1858.wuye.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AddPicAdapter extends BaseAdapter
{
	private Context context;
	private LinkedList<Bitmap> list;

	public AddPicAdapter(Context context, LinkedList<Bitmap> list)
	{
		this.context = context;
		this.list = list;
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
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.maintenance_pic_gridview_item, null);
			viewHolder.item_icon = (ImageView) convertView.findViewById(R.id.item_icon);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.item_icon.setImageBitmap(list.get(position));
		return convertView;
	}
	class ViewHolder
	{
		private ImageView item_icon;
	}
}

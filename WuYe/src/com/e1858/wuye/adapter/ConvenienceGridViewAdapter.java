package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.common.Constant;
import com.e1858.widget.CircularImage;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.ConvenienceInfoActivity;
import com.e1858.wuye.protocol.http.ConvenientType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConvenienceGridViewAdapter extends BaseAdapter
{
	private Context context;
	private List<ConvenientType> list;
	private DisplayImageOptions options;
	public ConvenienceGridViewAdapter(MainApplication application,Context context,List<ConvenientType> list){
		this.context=context;
		this.list=list;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.color.white)
		.showImageForEmptyUri(R.color.white)
		.showImageOnFail(R.color.white)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
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
			convertView=LayoutInflater.from(context).inflate(R.layout.convenience_fast_cmd_item, null);
			viewHolder.item_text=(TextView)convertView.findViewById(R.id.fast_grid_title);
			viewHolder.item_icon=(CircularImage)convertView.findViewById(R.id.fast_grid_icon);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getIcon(), viewHolder.item_icon, options);
		viewHolder.item_text.setText(list.get(position).getName());
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ConvenienceInfoActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt(Constant.DETAIL_ID, list.get(position).getID());
				bundle.putString(Constant.DETAIL_TITLE, list.get(position).getName());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	class ViewHolder{
		private TextView item_text;
		private CircularImage item_icon;
	}

}

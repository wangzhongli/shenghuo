package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.common.CallDialgoManager;
import com.e1858.common.Constant;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.ConvenienceDetailActivity;
import com.e1858.wuye.protocol.http.Convenient;
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
import android.widget.ImageView;
import android.widget.TextView;

public class ConvenienceInfoAdapter extends BaseAdapter
{
	private Context context;
	private List<Convenient> list;
	private DisplayImageOptions options;

	public ConvenienceInfoAdapter(Context context, List<Convenient> list)
	{
		this.context = context;
		this.list = list;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.convenience_store_default_icon)
		.showImageForEmptyUri(R.drawable.convenience_store_default_icon)
		.showImageOnFail(R.drawable.convenience_store_default_icon)
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
		if(null==convertView){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.convenience_info_item, null);
			viewHolder.item_icon=(ImageView)convertView.findViewById(R.id.item_icon);
			viewHolder.item_phone=(ImageView)convertView.findViewById(R.id.item_phone);
			viewHolder.item_title=(TextView)convertView.findViewById(R.id.item_title);
			viewHolder.item_address=(TextView)convertView.findViewById(R.id.item_address);
			viewHolder.item_content=(TextView)convertView.findViewById(R.id.item_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getIcon(), viewHolder.item_icon, options);
		viewHolder.item_title.setText(list.get(position).getName());
		viewHolder.item_address.setText("地址："+list.get(position).getAddress());
		viewHolder.item_content.setText("电话："+list.get(position).getPhone());
		viewHolder.item_phone.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				CallDialgoManager.createCallDialog(context,list.get(position).getPhone());
			}
		});
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ConvenienceDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt(Constant.DETAIL_ID, list.get(position).getID());
				bundle.putString(Constant.DETAIL_TITLE, list.get(position).getName());
				intent.putExtras(bundle);
				context.startActivity(intent);
				
			}
		});
		return convertView;
	}

	class ViewHolder
	{
		private ImageView item_icon;
		private ImageView item_phone;
		private TextView item_title;
		private TextView item_address;
		private TextView item_content;
	}
}

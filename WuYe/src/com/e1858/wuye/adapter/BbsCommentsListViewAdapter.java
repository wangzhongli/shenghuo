package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.utils.DateUtil;
import com.e1858.widget.CircularImage;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.BbsComments;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BbsCommentsListViewAdapter extends BaseAdapter
{

	private Context context;
	private List<BbsComments> list;
	private DisplayImageOptions options;
	public BbsCommentsListViewAdapter(Context context, List<BbsComments> list)
	{
		 options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.head_portrait_small_icon)
		.showImageForEmptyUri(R.drawable.head_portrait_small_icon)
		.showImageOnFail(R.drawable.head_portrait_small_icon)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.topic_reply_item, null);
			viewHolder.item_head_portrait = (CircularImage) convertView.findViewById(R.id.item_head_portrait);
			viewHolder.item_time = (TextView) convertView.findViewById(R.id.item_time);
			viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
			viewHolder.nickName = (TextView) convertView.findViewById(R.id.nickname);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).getHeadPortrait(), viewHolder.item_head_portrait,options);
		viewHolder.nickName.setText(list.get(position).getCreatorNickname());
		viewHolder.item_time.setText(DateUtil.dateToZh(list.get(position).getCreateTime()));
		viewHolder.item_content.setText(list.get(position).getContent());
		return convertView;
	}

	class ViewHolder
	{
		private CircularImage item_head_portrait;
		private TextView nickName;
		private TextView item_time;
		private TextView item_content;
	}

}

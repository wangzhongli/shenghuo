package com.e1858.wuye.adapter;

import java.util.ArrayList;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.utils.DateUtil;
import com.e1858.utils.StringUtils;
import com.e1858.widget.CircularImage;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.TopicInfoActivity;
import com.e1858.wuye.activity.TopicsActivity;
import com.e1858.wuye.activity.ViewPagerActivity;
import com.e1858.wuye.protocol.http.BbsTopic;
import com.hg.android.widget.ImagesGridViewSpecialOne;
import com.hg.android.widget.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BbsTopicListViewAdapter extends BaseAdapter
{
	private Context context;
	private List<BbsTopic> list;
	private long boradID;
	private MainApplication application;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private DisplayImageOptions head_options;

	public BbsTopicListViewAdapter(MainApplication application, Context context, List<BbsTopic> list, long boardID, ImageLoader imageLoader, DisplayImageOptions options)
	{
		this.context = context;
		this.list = list;
		this.boradID = boardID;
		this.application = application;
		this.imageLoader = imageLoader;
		this.options = options;
		head_options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.head_portrait_small_icon).showImageForEmptyUri(R.drawable.head_portrait_small_icon).showImageOnFail(R.drawable.head_portrait_small_icon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
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
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.topic_item, null);
			viewHolder.head_portrait = (CircularImage) convertView.findViewById(R.id.item_head_portrait);
			viewHolder.item_title = (TextView) convertView.findViewById(R.id.item_title);
			viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
			viewHolder.item_time = (TextView) convertView.findViewById(R.id.item_time);
			viewHolder.nickname = (TextView) convertView.findViewById(R.id.nickname);
			viewHolder.gridview1 = (ImagesGridViewSpecialOne) convertView.findViewById(R.id.gridview);
			viewHolder.topic_browse_num = (TextView) convertView.findViewById(R.id.topic_browse_num);
			viewHolder.topic_reply_num = (TextView) convertView.findViewById(R.id.topic_reply_num);
			viewHolder.select_pic_lin = (LinearLayout) convertView.findViewById(R.id.select_pic_lin);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(list.get(position).getHeadPortrait(), viewHolder.head_portrait, head_options);
		if(StringUtils.isEmpty(list.get(position).getCreatorNickname())){
			viewHolder.nickname.setText("匿名");
		}else{
			viewHolder.nickname.setText(list.get(position).getCreatorNickname());
		}
		
		viewHolder.item_title.setText(list.get(position).getTitle());
		viewHolder.item_content.setText(list.get(position).getContent());
		viewHolder.item_time.setText(DateUtil.dateToZh(list.get(position).getCreateTime()));
		viewHolder.topic_browse_num.setText(list.get(position).getViewCount() + "");
		viewHolder.topic_reply_num.setText(list.get(position).getCommentCount() + "");
		if ((null != list.get(position).getImages()) && list.get(position).getImages().size() > 0)
		{
			viewHolder.select_pic_lin.setVisibility(View.VISIBLE);
			viewHolder.gridview1.setImageUrls(list.get(position).getImages());
		}
		else
		{
			viewHolder.select_pic_lin.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, TopicInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("TOPIC_INFO", list.get(position));
				// bundle.putLong(Constant.DETAIL_ID,
				// list.get(position).getBbsTopic());
				// bundle.putString(Constant.DETAIL_TITLE,
				// list.get(position).getPost().getTitle());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder
	{
		private CircularImage head_portrait;
		private TextView nickname;
		private TextView item_time;
		private TextView item_title;
		private TextView item_content;
		private TextView topic_browse_num;
		private TextView topic_reply_num;
		private ImagesGridViewSpecialOne gridview1;
		private LinearLayout select_pic_lin;
	}
}

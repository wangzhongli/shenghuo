package com.e1858.wuye.adapter;
import java.util.ArrayList;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.utils.DateUtil;
import com.e1858.widget.CircularImage;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.ViewPagerActivity;
import com.e1858.wuye.protocol.http.BbsComments;
import com.e1858.wuye.protocol.http.BbsTopic;
import com.hg.android.widget.ImagesGridView;
import com.hg.android.widget.ImagesGridViewSpecialOne;
import com.hg.android.widget.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class TopicDetailAdapter extends BaseAdapter {
	private Context context;
	private MainApplication application;
	private BbsTopic bbsTopic;
	private List<BbsComments> lists;
	private final int TYPE_COUNT = 2;
	private final int FIRST_TYPE = 0;
	private final int OTHERS_TYPE = 1;
	private int currentType;
	private DisplayImageOptions options;
	private DisplayImageOptions options_One;
//	public List<String> pics = new ArrayList<String>();
	public TopicDetailAdapter(Context context, MainApplication application,
			BbsTopic bbsTopic, List<BbsComments> lists) {
		options_One= new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.head_portrait_icon)
		.showImageForEmptyUri(R.drawable.head_portrait_icon)
		.showImageOnFail(R.drawable.head_portrait_icon)
		.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.head_portrait_small_icon)
				.showImageForEmptyUri(R.drawable.head_portrait_small_icon)
				.showImageOnFail(R.drawable.head_portrait_small_icon)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		this.context = context;
		this.application = application;
		this.bbsTopic = bbsTopic;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		if(lists==null||lists.size()==0){
			return 1;
		}else{
			return lists.size()+1;
		}
	}

	@Override
	public Object getItem(int position) {
		if(lists==null||lists.size()==0){
			return null;
		}else{
			if (position>0) {
				return lists.get(position-1);
			} else {
				return lists.get(position+1);
			}
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return FIRST_TYPE;
		} else {
			return OTHERS_TYPE;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View firstItemView = null;
		View othersItemView = null;
		// 获取到当前位置所对应的Type
		currentType = getItemViewType(position);
		if (currentType == FIRST_TYPE) {
			firstItemView = convertView;
			ViewHolderOne viewHolderOne = null;
			if (firstItemView == null) {
				firstItemView = LayoutInflater.from(context).inflate(
						R.layout.topic_detail_item_top, null);
				viewHolderOne = new ViewHolderOne();
				viewHolderOne.head_portrait = (CircularImage) firstItemView
						.findViewById(R.id.head_portrait);
				viewHolderOne.nickName = (TextView) firstItemView
						.findViewById(R.id.nickname);
				viewHolderOne.item_time = (TextView) firstItemView
						.findViewById(R.id.item_time);
				viewHolderOne.select_pic_lin = (LinearLayout) firstItemView
						.findViewById(R.id.select_pic_lin);
				viewHolderOne.content = (TextView) firstItemView
						.findViewById(R.id.topic_content);
				viewHolderOne.gridview1 = (ImagesGridViewSpecialOne) firstItemView
						.findViewById(R.id.gridview);
				firstItemView.setTag(viewHolderOne);
			} else {
				viewHolderOne = (ViewHolderOne) firstItemView.getTag();
			}
			// 设置值
			ImageLoader.getInstance().displayImage(bbsTopic.getHeadPortrait(), viewHolderOne.head_portrait,options_One);
			viewHolderOne.nickName.setText(bbsTopic.getCreatorNickname());
			viewHolderOne.item_time.setText(DateUtil.dateToZh(bbsTopic
				.getCreateTime()));
			viewHolderOne.content.setText(bbsTopic.getContent());
			if ((null != bbsTopic.getImages())
					&& bbsTopic.getImages().size() > 0) {
				viewHolderOne.select_pic_lin.setVisibility(View.VISIBLE);
				viewHolderOne.gridview1.setImageUrls(bbsTopic.getImages());
			}
					
			convertView = firstItemView;

		} else {
			othersItemView = convertView;
			ViewHolder viewHolder = null;
			if (othersItemView == null) {
				othersItemView = LayoutInflater.from(context).inflate(
						R.layout.topic_reply_item, null);
				viewHolder = new ViewHolder();
				viewHolder.item_head_portrait = (CircularImage) othersItemView
						.findViewById(R.id.item_head_portrait);
				viewHolder.item_time = (TextView) othersItemView
						.findViewById(R.id.item_time);
				viewHolder.item_content = (TextView) othersItemView
						.findViewById(R.id.item_content);
				viewHolder.nickName = (TextView) othersItemView
						.findViewById(R.id.nickname);
				othersItemView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) othersItemView.getTag();
			}

			if (lists != null&&lists.size()!=0) {
				// 设置值
				ImageLoader.getInstance().displayImage(lists.get(position-1).getHeadPortrait(), viewHolder.item_head_portrait, options);
				viewHolder.nickName.setText(lists.get(position-1).getCreatorNickname());
				viewHolder.item_time.setText(DateUtil.dateToZh(lists.get(position-1)
				.getCreateTime()));
				viewHolder.item_content.setText(lists.get(position-1).getContent());
			}
			convertView = othersItemView;
		}
		return convertView;
	}
	class ViewHolderOne {
		private CircularImage head_portrait;
		private TextView nickName;
		private TextView item_time;
		private TextView content;
		private LinearLayout select_pic_lin;
		private ImagesGridViewSpecialOne gridview1;
	}
	class ViewHolder {
		private CircularImage item_head_portrait;
		private TextView nickName;
		private TextView item_time;
		private TextView item_content;
	}
}

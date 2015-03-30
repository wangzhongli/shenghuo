package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.utils.DateUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.Util;
import com.e1858.widget.CircularImage;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.TopicInfoActivity;
import com.e1858.wuye.protocol.http.BbsTopic;
import com.e1858.wuye.protocol.http.UserHouse;
import com.e1858.wuye.protocol.http.UserInfo;
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
public class MyTopicAdapter extends BaseAdapter {
	private MainApplication application;
	private Context context;
	private List<BbsTopic> list;
	private DisplayImageOptions options;
	private final int TYPE_COUNT = 2;
	private final int FIRST_TYPE = 0;
	private final int OTHERS_TYPE = 1;
	private int currentType;
	private int topicNum;
	private int topicComment;
	public MyTopicAdapter(MainApplication application,Context context,List<BbsTopic> list,int topicNum,int topicComment){
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.head_portrait_icon)
		.showImageForEmptyUri(R.drawable.head_portrait_icon)
		.showImageOnFail(R.drawable.head_portrait_icon)
		.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		this.application=application;
		this.context=context;
		this.list=list;
		this.topicNum=topicNum;
		this.topicComment=topicComment;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list==null||list.size()==0){
			return 1;
		}else{
			return list.size()+1;
		}
	}

	@Override
	public Object getItem(int position) {
		if(list==null||list.size()==0){
			return null;
		}else{
			return list.get(position-1);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
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
						R.layout.my_topic_item_top, null);
				viewHolderOne = new ViewHolderOne();
				viewHolderOne.head_portrait = (CircularImage) firstItemView
						.findViewById(R.id.head_portrait);
				viewHolderOne.nickName = (TextView) firstItemView
						.findViewById(R.id.nickname);
				viewHolderOne.topicNum = (TextView) firstItemView
						.findViewById(R.id.topic_num);
				viewHolderOne.topicComment = (TextView) firstItemView
						.findViewById(R.id.topic_comment_num);
				firstItemView.setTag(viewHolderOne);
			} else {
				viewHolderOne = (ViewHolderOne) firstItemView.getTag();
			}
			// 设置值
			UserInfo  userInfo=PreferencesUtils.getUserInfo();
			UserHouse userHouse=PreferencesUtils.getUserHouse();
			if(null!=userInfo&&!StringUtils.isEmpty(userInfo.getHeadPortrait())){
				ImageLoader.getInstance().displayImage(userInfo.getHeadPortrait(), viewHolderOne.head_portrait,options);
			}
			if(null==userHouse||StringUtils.isEmpty(userInfo.getNickname())||null==userHouse){
				viewHolderOne.nickName.setText("昵称/楼号");
			}
			if(null!=userHouse){
				viewHolderOne.nickName.setText(Util.getHouseInfo(true));
			}
			if(userInfo!=null&&!StringUtils.isEmpty(userInfo.getNickname())){
				viewHolderOne.nickName.setText(userInfo.getNickname());
			}
			viewHolderOne.topicNum.setText(topicNum+"");
			viewHolderOne.topicComment.setText(topicComment+"");
			convertView = firstItemView;
		} else {
			othersItemView = convertView;
			ViewHolder viewHolder = null;
			if (othersItemView == null) {
				othersItemView = LayoutInflater.from(context).inflate(
						R.layout.my_topic_item, null);
				viewHolder = new ViewHolder();
				viewHolder.item_time = (TextView) othersItemView
						.findViewById(R.id.topic_time);
				viewHolder.item_title = (TextView) othersItemView
						.findViewById(R.id.item_title);
				viewHolder.item_comment = (TextView) othersItemView
						.findViewById(R.id.topic_comment_num);
				othersItemView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) othersItemView.getTag();
			}
			if (list!= null&&list.size()!=0) {
				// 设置值
				viewHolder.item_title.setText(list.get(position-1).getTitle());
				viewHolder.item_time.setText(DateUtil.dateToZh(list.get(position-1)
				.getCreateTime()));
				viewHolder.item_comment.setText(list.get(position-1).getCommentCount()+"");
			}
			othersItemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,TopicInfoActivity.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("TOPIC_INFO", list.get(position-1));
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			});
			convertView = othersItemView;
		}
		return convertView;
	}
	class ViewHolderOne {
		private CircularImage head_portrait;
		private TextView nickName;
		private TextView topicNum;
		private TextView topicComment;
	}
	class ViewHolder {
		private TextView item_title;
		private TextView item_time;
		private TextView item_comment;
	}

}

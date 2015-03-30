package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.common.Constant;
import com.e1858.utils.DateUtil;
import com.e1858.utils.StringUtils;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.NoticeActivity;
import com.e1858.wuye.protocol.http.Notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoticeListViewAdapter extends BaseAdapter
{
	private Context context;
	private List<Notice> list;

	public NoticeListViewAdapter(Context context,List<Notice> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.notice_item, null);
			viewHolder.title=(TextView)convertView.findViewById(R.id.item_title);
			viewHolder.time=(TextView)convertView.findViewById(R.id.item_time);
			viewHolder.browserNum=(TextView)convertView.findViewById(R.id.item_browserNum);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.title.setText(list.get(position).getTitle());
		viewHolder.time.setText(DateUtil.dateToZh(list.get(position).getSendTime()));
		viewHolder.browserNum.setText("浏览:"+list.get(position).getViewCount());
		if(StringUtils.isEmpty(list.get(position).getReadTime())){
			convertView.setBackgroundResource(R.drawable.listview_item_background);
		}else{
			viewHolder.title.setTextColor(context.getResources().getColor(R.color.notes_textColor));
		}
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,NoticeActivity.class);
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
		private TextView browserNum;
	}
}

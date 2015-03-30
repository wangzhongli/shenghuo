package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.common.Constant;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.TopicsActivity;
import com.e1858.wuye.protocol.http.BbsBoard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NeighborhoodListViewAdapter extends BaseAdapter
{
	private Context context;
	private List<BbsBoard> list;
	public NeighborhoodListViewAdapter(Context context,List<BbsBoard> list){
		
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
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.neighborhood_interactions_item, null);
			viewHolder.item_title=(TextView)convertView.findViewById(R.id.item_title);
			viewHolder.item_content=(TextView)convertView.findViewById(R.id.item_content);
			viewHolder.bbs_topic_num=(TextView)convertView.findViewById(R.id.bbs_topic_num);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.item_title.setText(list.get(position).getName());
		viewHolder.item_content.setText(list.get(position).getEdescription());
		if(list.get(position).getTopicCount()>99){
			viewHolder.bbs_topic_num.setText("99+");
		}else{
			viewHolder.bbs_topic_num.setText(""+list.get(position).getTopicCount());
		}
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,TopicsActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt(Constant.PIC_REMARK, 1);
				bundle.putInt(Constant.DETAIL_ID, list.get(position).getID());
				bundle.putString(Constant.DETAIL_TITLE, list.get(position).getName());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	class ViewHolder{
		private TextView item_title;
		private TextView item_content;
		private TextView bbs_topic_num;
	}

}

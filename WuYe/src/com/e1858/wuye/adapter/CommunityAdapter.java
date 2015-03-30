package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.Community;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class CommunityAdapter extends BaseAdapter {

	private List<Community> list;
	private Context context;
	public CommunityAdapter(Context context,List<Community> list){
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(null==convertView){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.community_item, null);
			viewHolder.item_title=(TextView)convertView.findViewById(R.id.item_title);
			viewHolder.item_content=(TextView)convertView.findViewById(R.id.item_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.item_title.setText(list.get(position).getName());
		viewHolder.item_content.setText(list.get(position).getAddress());
		return convertView;
	}
	class ViewHolder{
		private TextView item_title;
		private TextView item_content;
	}

}

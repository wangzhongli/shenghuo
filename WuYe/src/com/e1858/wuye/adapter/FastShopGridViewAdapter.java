package com.e1858.wuye.adapter;

import java.util.List;

import com.e1858.wuye.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FastShopGridViewAdapter extends BaseAdapter
{
	private Context context;
	private List<Integer> list;
	public FastShopGridViewAdapter(Context context,List<Integer> list){
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
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.fast_shop_gridview_item, null);
			viewHolder.fast_grid_textview=(TextView)convertView.findViewById(R.id.fast_grid_textview);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		switch(list.get(position)){
		case 1:
			viewHolder.fast_grid_textview.setText("通知公告");
			viewHolder.fast_grid_textview.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.notice_icon),null,null);
			break;
		case 2:
			viewHolder.fast_grid_textview.setText("缴物业费");
			viewHolder.fast_grid_textview.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.pay_wuye_icon),null,null);
			break;
		case 3:
			viewHolder.fast_grid_textview.setText("邻里互动");
			viewHolder.fast_grid_textview.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.interact_icon),null,null);
			break;
		}
		convertView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				//进入一个webview页面
			}
		});
		return convertView;
	}

	class ViewHolder{
		private TextView fast_grid_textview;
	}
}

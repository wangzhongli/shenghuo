package com.e1858.wuye.adapter;
import java.util.List;
import com.e1858.common.CallDialgoManager;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.Phone;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class ServicePhoneAdapter extends BaseAdapter
{
	private Context context;
	private List<Phone> list;
	public ServicePhoneAdapter(Context context,List<Phone> list){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.service_phone_item, null);
			viewHolder.item_title=(TextView)convertView.findViewById(R.id.item_title);
			viewHolder.item_content=(TextView)convertView.findViewById(R.id.item_content);
			viewHolder.item_icon=(ImageView)convertView.findViewById(R.id.item_icon);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.item_title.setText(list.get(position).getTitle());
		viewHolder.item_content.setText(list.get(position).getPhone());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CallDialgoManager.createCallDialog(context,list.get(position).getPhone());
			}
		});
		return convertView;
	}

	class ViewHolder{
		private TextView item_title;
		private TextView item_content;
		@SuppressWarnings("unused")
		private ImageView item_icon;
	}
}

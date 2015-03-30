package com.e1858.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.mall.address.MallAddressActivity;
import com.e1858.mall.order.MallOrderActivity;
import com.e1858.mall.recommend.MallMyRecommendActivity;
import com.e1858.mall.recommend.MallPersonRecommActivity;
import com.e1858.wuye.mall.R;

public class MallMineActivity extends BaseActionBarActivity {

	
	String[]	names	= new String[] { "我的订单", "我的收货地址","我的推荐" };
	int[]		icons	= new int[] { R.drawable.mall_ic_myorder, R.drawable.mall_ic_myaddress,R.drawable.mall_ic_myrecomm};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_listview);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new MyAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent(getActivity(), MallOrderActivity.class));
					break;
				case 1:
					startActivity(new Intent(getActivity(), MallAddressActivity.class));
					break;
				case 2:
					startActivity(new Intent(getActivity(), MallMyRecommendActivity.class));
					break;
				}
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.mall_listitem_mine, null);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.textView);
			textView.setText(names[position]);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
			imageView.setBackgroundResource(icons[position]);
			return convertView;
		}

	}

}

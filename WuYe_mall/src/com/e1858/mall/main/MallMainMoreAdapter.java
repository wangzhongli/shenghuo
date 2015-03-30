package com.e1858.mall.main;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.protocol.bean.MallProductCategoryBean;
import com.e1858.wuye.mall.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 日期: 2015年2月2日 下午5:15:28
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class MallMainMoreAdapter extends BaseAdapter {
	List<MallProductCategoryBean>	moreCategories;

	public void setMoreCategories(List<MallProductCategoryBean> moreCategories) {
		this.moreCategories = moreCategories;
		notifyDataSetChanged();
	}

	/*
	 */
	@Override
	public int getCount() {
		return moreCategories == null ? 0 : moreCategories.size();
	}

	/*
	 */
	@Override
	public MallProductCategoryBean getItem(int position) {
		return moreCategories.get(position);
	}

	/*
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/*
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(), R.layout.mall_griditem_main_more, null);
		}
		MallProductCategoryBean bean = getItem(position);
		TextView textView_title = (TextView) convertView.findViewById(R.id.textView_title);
		textView_title.setText(bean.getName());

		TextView textView_detail = (TextView) convertView.findViewById(R.id.textView_detail);
		textView_detail.setText(bean.getEdescription());

		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
		ImageLoader.getInstance().displayImage(bean.getImageUrl(), imageView,
				BaseApplication.displayImageOptions_empty());
		return convertView;
	}

}

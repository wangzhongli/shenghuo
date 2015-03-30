package com.e1858.mall.main;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.product.MallProductActivity;
import com.e1858.mall.protocol.bean.MallRecommendedCategoryBean;
import com.e1858.wuye.mall.R;
import com.e1858.wuye.protocol.http.BannerBean;
import com.hg.android.utils.HGUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * jiaofeijilufagwuxzinxi,jiaofeiyuefen
 * 日期: 2015年2月2日 下午5:15:28
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class MallMainRecommendAdapter extends BaseAdapter {

	public List<MallRecommendedCategoryBean>	recommendedCategories;

	public void setRecommendedCategories(List<MallRecommendedCategoryBean> recommendedCategories) {
		this.recommendedCategories = recommendedCategories;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return recommendedCategories == null ? 0 : recommendedCategories.size();
	}

	/*
	 */
	@Override
	public MallRecommendedCategoryBean getItem(int position) {
		return recommendedCategories.get(position);
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
			convertView = View.inflate(parent.getContext(), R.layout.mall_listitem_main_recommend, null);
			convertView.findViewById(R.id.textView_more).setOnClickListener(onClickListener);
			convertView.findViewById(R.id.imageView_0).setOnClickListener(onClickListener);
			convertView.findViewById(R.id.imageView_1).setOnClickListener(onClickListener);
			convertView.findViewById(R.id.imageView_2).setOnClickListener(onClickListener);
		}
		MallRecommendedCategoryBean bean = getItem(position);
		TextView textView_title = (TextView) convertView.findViewById(R.id.textView_title);
		textView_title.setText(bean.getProductCategory().getName());

		ImageView imageView_0 = (ImageView) convertView.findViewById(R.id.imageView_0);
		ImageView imageView_1 = (ImageView) convertView.findViewById(R.id.imageView_1);
		ImageView imageView_2 = (ImageView) convertView.findViewById(R.id.imageView_2);

		convertView.findViewById(R.id.textView_more).setTag(position);
		imageView_0.setTag(position);
		imageView_1.setTag(position);
		imageView_2.setTag(position);

		List<BannerBean> banners = bean.getBanners();
		if (!HGUtils.isListEmpty(banners)) {
			if (banners.size() >= 1)
				ImageLoader.getInstance().displayImage(banners.get(0).getImageUrl(), imageView_0,
						BaseApplication.displayImageOptions_empty());
			if (banners.size() >= 2)
				ImageLoader.getInstance().displayImage(banners.get(1).getImageUrl(), imageView_1,
						BaseApplication.displayImageOptions_empty());
			if (banners.size() >= 3)
				ImageLoader.getInstance().displayImage(banners.get(2).getImageUrl(), imageView_2,
						BaseApplication.displayImageOptions_empty());
		}
		return convertView;
	}

	OnClickListener	onClickListener	= new OnClickListener() {

										@Override
										public void onClick(View v) {
											try {
												MallRecommendedCategoryBean bean = getItem((Integer) v.getTag());
												if (v.getId() == R.id.textView_more) {
													Intent intent = new Intent(v.getContext(),
															MallProductActivity.class);
													intent.putExtra(MallProductActivity.IntentKey_Category,
															bean.getProductCategory());
													v.getContext().startActivity(intent);
												} else if (v.getId() == R.id.imageView_0) {
													BaseApplication.getBaseInstance().judgeForBannerUrl(
															(Activity) v.getContext(), bean.getBanners().get(0));
												} else if (v.getId() == R.id.imageView_1) {
													BaseApplication.getBaseInstance().judgeForBannerUrl(
															(Activity) v.getContext(), bean.getBanners().get(1));
												} else if (v.getId() == R.id.imageView_2) {
													BaseApplication.getBaseInstance().judgeForBannerUrl(
															(Activity) v.getContext(), bean.getBanners().get(2));
												}
											}
											catch (Exception e) {
												e.printStackTrace();
											}
										}
									};

}

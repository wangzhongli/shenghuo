package com.e1858.mall.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.protocol.bean.MallRecommend;
import com.e1858.mall.recommend.MallRecommendInfoActivity;
import com.e1858.wuye.mall.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallMyRecommedHolder extends MallViewBaseHolder {

	private ImageView	recommend_item_iv_pic;
	private TextView	recommend_item_tv_dec;
	private TextView	recommend_item_tv_time;
	private TextView	recommend_item_tv_heart;
	private TextView	recommend_item_tv_comm;
	MallRecommend		mallRecommend;

	public MallMyRecommedHolder(Context context, View view) {
		super(context, view, R.layout.mall_recommend_item);
	}

	@Override
	protected void initSubviews() {
		recommend_item_iv_pic = (ImageView) findViewById(R.id.recommend_item_iv_pic);
		recommend_item_tv_dec = (TextView) findViewById(R.id.recommend_item_tv_dec);
		recommend_item_tv_time = (TextView) findViewById(R.id.recommend_item_tv_time);
		recommend_item_tv_heart = (TextView) findViewById(R.id.recommend_item_tv_heart);
		recommend_item_tv_comm = (TextView) findViewById(R.id.recommend_item_tv_comm);
		getView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MallRecommendInfoActivity.class);
				intent.putExtra(MallRecommendInfoActivity.RECOMMEND_INFO, mallRecommend);
				context.startActivity(intent);
				
			}
		});
	}

	public void updateView(MallRecommend mallRecommend) {
		this.mallRecommend = mallRecommend;
		if (mallRecommend == null) {
			return;
		}
		ImageLoader.getInstance().displayImage(mallRecommend.getProductIcon(), recommend_item_iv_pic,
				BaseApplication.displayImageOptions());
		recommend_item_tv_dec.setText(mallRecommend.getRecDescription() == null ? "暂无描述信息" : mallRecommend.getRecDescription());
		recommend_item_tv_time
				.setText(mallRecommend.getRecommendTime() == null ? "" : mallRecommend.getRecommendTime());
		recommend_item_tv_heart.setText(mallRecommend.getHeartCount() + "");
		recommend_item_tv_comm.setText(mallRecommend.getCommentCount() + "");
	}

}

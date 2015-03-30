package com.e1858.mall.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e1858.common.app.BaseApplication;
import com.e1858.common.widget.CircularImage;
import com.e1858.mall.protocol.bean.MallRecommend;
import com.e1858.mall.protocol.packet.MallAddHeartRequest;
import com.e1858.mall.protocol.packet.MallAddHeartResp;
import com.e1858.mall.recommend.MallRecommendInfoActivity;
import com.e1858.mall.utils.ToastUtil;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.MLog;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.StringUtils;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallPersonRecommedHolder extends MallViewBaseHolder {
	private ImageView				mall_person_rec_iv_icon;
	private CircularImage			head_portrait;
	private TextView				mall_person_rec_tv_dec;
	private TextView				mall_tv_recomm_name;
	private TextView				mall_tv_recomm_time;
	private TextView				mall_tv_recomm_heart;
	private TextView				mall_tv_recomm_comm;
	private LinearLayout			mall_ll_recomm_comm;
	protected DisplayImageOptions	options, options1;
	MallRecommend					mallRecommend;
	private ImageView				mall_iv_recomm_heart;
	public List<String>				hearteds_l	= new ArrayList<String>();

	public MallPersonRecommedHolder(Context context, View view) {
		super(context, view, R.layout.person_recomm_item);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.head_portrait_icon)
				.showImageForEmptyUri(R.drawable.head_portrait_icon).showImageOnFail(R.drawable.head_portrait_icon)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		options1 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.mall_add_recomm_bg)
				.showImageForEmptyUri(R.drawable.mall_add_recomm_bg).showImageOnFail(R.drawable.mall_add_recomm_bg)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	@Override
	protected void initSubviews() {
		mall_person_rec_iv_icon = (ImageView) findViewById(R.id.mall_person_rec_iv_icon);
		head_portrait = (CircularImage) findViewById(R.id.head_portrait);
		mall_person_rec_tv_dec = (TextView) findViewById(R.id.mall_person_rec_tv_dec);

		mall_tv_recomm_name = (TextView) findViewById(R.id.mall_tv_recomm_name);
		mall_tv_recomm_time = (TextView) findViewById(R.id.mall_tv_recomm_time);
		mall_tv_recomm_heart = (TextView) findViewById(R.id.mall_tv_recomm_heart);
		mall_tv_recomm_comm = (TextView) findViewById(R.id.mall_tv_recomm_comm);

		mall_iv_recomm_heart = (ImageView) findViewById(R.id.mall_iv_recomm_heart);
		mall_ll_recomm_comm = (LinearLayout) findViewById(R.id.mall_ll_recomm_comm);
		getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, MallRecommendInfoActivity.class);
				intent.putExtra(MallRecommendInfoActivity.RECOMMEND_INFO, mallRecommend);
				intent.putExtra(MallRecommendInfoActivity.RECOMMEND_MYHEARTS, (Serializable) hearteds_l);
				context.startActivity(intent);
			}
		});

	}

	public void updateView(final MallRecommend mallRecommend, final List<String> hearteds, final Activity activity) {

		this.mallRecommend = mallRecommend;
		hearteds_l.addAll(hearteds);
		ImageLoader.getInstance().displayImage(mallRecommend.getProductIcon(), mall_person_rec_iv_icon, options1);
		ImageLoader.getInstance().displayImage(mallRecommend.getRecommendUserIcon(), head_portrait, options);
		mall_person_rec_tv_dec.setText(mallRecommend.getRecDescription() == null ? "无描述" : mallRecommend
				.getRecDescription());
		mall_tv_recomm_name.setText(mallRecommend.getRecommendUserName() == null ? " " : (StringUtils
				.isMobileNO(mallRecommend.getRecommendUserName()) ? StringUtils.replaceMobileNo(mallRecommend
				.getRecommendUserName()) : mallRecommend.getRecommendUserName()));
		mall_tv_recomm_time.setText(mallRecommend.getRecommendTime() == null ? " " : mallRecommend.getRecommendTime());
		mall_tv_recomm_heart.setText(mallRecommend.getHeartCount() + "");
		mall_tv_recomm_comm.setText(mallRecommend.getCommentCount() + "");
		if (!HGUtils.isListEmpty(hearteds_l))
			if (hearteds_l.contains(mallRecommend.getRecommendID())) {
				mall_iv_recomm_heart.setBackgroundResource(R.drawable.mall_recommend_info_heart_select);
			} else {
				mall_iv_recomm_heart.setBackgroundResource(R.drawable.mall_recommend_info_heart);
			}
		mall_iv_recomm_heart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(activity)) {
					if (!HGUtils.isListEmpty(hearteds_l) && hearteds_l.contains(mallRecommend.getProductID())) {//已经点过赞的
						ToastUtil.show(context, "你已经点过赞了，看看别的推荐吧！");
					} else {
						//添加点赞
						addHeart(mallRecommend.getRecommendID());
					}

				}

			}
		});
		mall_ll_recomm_comm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(activity)) {
					Intent intent = new Intent(context, MallRecommendInfoActivity.class);
					intent.putExtra(MallRecommendInfoActivity.RECOMMEND_INFO, mallRecommend);
					intent.putExtra(MallRecommendInfoActivity.RECOMMEND_MYHEARTS, (Serializable) hearteds_l);
					context.startActivity(intent);
				}
			}
		});
	}

	protected void addHeart(final String recommendID) {

		ResponseHandler<MallAddHeartResp> responseHandler = new ResponseHandler<MallAddHeartResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallAddHeartResp response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					ToastUtil.show(context, "点赞成功！");
					MLog.i("TAG", recommendID + "点赞成功");
					int heartCount = mallRecommend.getHeartCount();
					mall_iv_recomm_heart.setBackgroundResource(R.drawable.mall_recommend_info_heart_select);
					mall_tv_recomm_heart.setText((heartCount + 1) + "");
					hearteds_l.add(mallRecommend.getRecommendID());
				}
			}
		};

		MallAddHeartRequest request = new MallAddHeartRequest();
		request.setRecommendID(recommendID);
		request.setHeartFlag(0);
		HttpPacketClient.postPacketAsynchronous(request, MallAddHeartResp.class, responseHandler, true);
	}

}

package com.e1858.point;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.common.R;
import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.WebViewActivity;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.protocol.http.PointGetMyProfileRequest;
import com.e1858.wuye.protocol.http.PointGetMyProfileResp;
import com.e1858.wuye.protocol.http.PointProfileBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PointActivity extends BaseActionBarActivity {
	DisplayImageOptions	displayImageOptions	= new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
													.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
													.build();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_point);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews();

		loadProfile();
	}

	void loadProfile() {
		ResponseHandler<PointGetMyProfileResp> responseHandler = new ResponseHandler<PointGetMyProfileResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(PointGetMyProfileResp response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					updateViews(response.getMyProfile());
				}
			}
		};

		PointGetMyProfileRequest request = new PointGetMyProfileRequest();
		HttpPacketClient.postPacketAsynchronous(request, PointGetMyProfileResp.class, responseHandler, true);
	}

	void updateViews(PointProfileBean profile) {
		findViewById(R.id.levelContainer).setVisibility(View.VISIBLE);
		View listitem_records = findViewById(R.id.listitem_records);
		{
			TextView textView = (TextView) listitem_records.findViewById(R.id.textView);
			textView.setText(Html.fromHtml("现有积分: <font color=\'#eb6100\'>" + profile.getPoints() + "</font>"));
		}

		TextView textView_vip = (TextView) findViewById(R.id.textView_vip);
		textView_vip.setText(Html.fromHtml("会员等级: <font color=\'#eb6100\'>" + profile.getLevelName() + "</font>"));

		TextView textView_level = (TextView) findViewById(R.id.textView_level);
		textView_level.setText(profile.getLevelName());

		TextView textView_maxLevel = (TextView) findViewById(R.id.textView_maxLevel);
		textView_maxLevel.setText(profile.getMaxLevelName());

		ImageView imageView_level = (ImageView) findViewById(R.id.imageView_level);
		ImageLoader.getInstance().displayImage(profile.getLevelIconUrl(), imageView_level, displayImageOptions);

		ImageView imageView_maxLevel = (ImageView) findViewById(R.id.imageView_maxLevel);
		ImageLoader.getInstance().displayImage(profile.getMaxLevelIconUrl(), imageView_maxLevel, displayImageOptions);

		int progress = (int) (profile.getLevelProgress() * 100);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		TextView textView_progress = (TextView) findViewById(R.id.textView_progress);
		textView_progress.setText(String.format("%d%%", progress));
		textView_progress.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, progress));

		progressBar.setMax(100);
		progressBar.setProgress(progress);
	}

	void initViews() {
		findViewById(R.id.levelContainer).setVisibility(View.GONE);
		findViewById(R.id.button_rule).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), WebViewActivity.class);
				intent.putExtra(WebViewActivity.IntentKey_Title, "如何升级");
				intent.putExtra(WebViewActivity.IntentKey_URL, Constant.POINT_RULE_URL);
				startActivity(intent);
			}
		});

		{
			View listitem = findViewById(R.id.listitem_records);
			ImageView imageView = (ImageView) listitem.findViewById(R.id.imageView);
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(R.drawable.point_now);
			TextView textView = (TextView) listitem.findViewById(R.id.textView);
			TextView textView_detail = (TextView) listitem.findViewById(R.id.textView_detail);
			textView.setText("现有积分:");
			textView_detail.setVisibility(View.VISIBLE);
			textView_detail.setText("积分记录");

			listitem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), PointRecordsActivity.class);
					startActivity(intent);
				}
			});
		}

		{
			View listitem = findViewById(R.id.listitem_introduction);
			ImageView imageView = (ImageView) listitem.findViewById(R.id.imageView);
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(R.drawable.point_introduction);
			TextView textView = (TextView) listitem.findViewById(R.id.textView);
			textView.setText("积分说明");

			listitem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), WebViewActivity.class);
					intent.putExtra(WebViewActivity.IntentKey_Title, "积分说明");
					intent.putExtra(WebViewActivity.IntentKey_URL, Constant.POINT_INTRODUCTION_URL);
					startActivity(intent);
				}
			});
		}

	}
}

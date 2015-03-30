package com.e1858.mall.review;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.mall.protocol.bean.MallOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.bean.MallProductReviewBean;
import com.e1858.mall.protocol.packet.MallAddProductReviewRequest;
import com.e1858.mall.protocol.packet.MallAddProductReviewResponse;
import com.e1858.mall.widget.MallAddProductReviewCardHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ImageUploadTask;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.mall.R;

/**
 * 日期: 2015年2月4日 上午10:00:59
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class MallProductReviewAddActivity extends BaseActionBarActivity {

	public static final String				IntentKey_Order		= "IntentKey_Order";
	public static final String				IntentKey_Title		= "IntentKey_Title";

	MallOrderBean							orderBean;
	View									checkBox_anonymous;
	boolean									isAppend			= false;

	List<MallAddProductReviewCardHolder>	addReviewHolders	= new ArrayList<MallAddProductReviewCardHolder>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent() == null) {
			finish();
			return;
		}

		orderBean = (MallOrderBean) getIntent().getSerializableExtra(IntentKey_Order);
		if (orderBean == null) {
			finish();
			return;
		}

		setContentView(R.layout.mall_activity_addproductreview);
		setTitle(getIntent().getStringExtra(IntentKey_Title));

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		initViews();
	}

	void initViews() {
		checkBox_anonymous = findViewById(R.id.checkBox_anonymous);
		checkBox_anonymous.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setSelected(!v.isSelected());
			}
		});
		TextView textView_shopName = (TextView) findViewById(R.id.textView_shopName);
		TextView textView_orderTime = (TextView) findViewById(R.id.textView_orderTime);

		textView_shopName.setText(orderBean.getShopName());
		textView_orderTime.setText("成交时间:" + orderBean.getCreateTime());

		ViewGroup addReviewGroupView = (ViewGroup) findViewById(R.id.addReviewGroupView);

		int productCount = orderBean.getOrderProducts().size();
		int reviewCount = 0, appendReviewCount = 0;
		for (MallOrderProductBean product : orderBean.getOrderProducts()) {
			if (product.getReview() != null) {
				reviewCount++;
				if (product.getReview().getAppendReview() != null) {
					appendReviewCount++;
				}
			}
		}

		if (reviewCount < productCount) {
			//进入评价模式
			isAppend = false;
			findViewById(R.id.bottomBar).setVisibility(View.VISIBLE);
			for (MallOrderProductBean product : orderBean.getOrderProducts()) {
				if (product.getReview() == null) {
					MallAddProductReviewCardHolder holder = new MallAddProductReviewCardHolder(getActivity(), null);
					addReviewGroupView.addView(holder.detach());
					holder.updateForBean(product);
					addReviewHolders.add(holder);
				}
			}
		} else if (appendReviewCount < productCount) {
			//进入追加模式 
			isAppend = true;
			findViewById(R.id.bottomBar).setVisibility(View.GONE);
			for (MallOrderProductBean product : orderBean.getOrderProducts()) {
				if (product.getReview().getAppendReview() == null) {
					MallAddProductReviewCardHolder holder = new MallAddProductReviewCardHolder(getActivity(), null);
					addReviewGroupView.addView(holder.detach());
					holder.updateForBean(product);
					addReviewHolders.add(holder);
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		for (MallAddProductReviewCardHolder holder : addReviewHolders) {
			if (holder.imagesGridEditView.handleForRequestCode(requestCode)) {
				holder.imagesGridEditView.onActivityResult(requestCode, resultCode, data);
				return;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_addreview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_accept) {
			onSubmitReview();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onSubmitReview() {
		boolean contentIsAllEmpty = true;
		for (MallAddProductReviewCardHolder holder : addReviewHolders) {
			if (!TextUtils.isEmpty(holder.editText_content.getText())) {
				contentIsAllEmpty = false;
				break;
			}
		}
		if (contentIsAllEmpty) {
			Toast.makeText(getActivity(), "请填写评价内容", Toast.LENGTH_SHORT).show();
			return;
		}

		List<String> imagePaths = new ArrayList<String>();
		for (MallAddProductReviewCardHolder holder : addReviewHolders) {
			//评价内容不为空,才上传图片
			if ((!TextUtils.isEmpty(holder.editText_content.getText())) && holder.imagesGridEditView.imagesCount() > 0) {
				imagePaths.addAll(holder.imagesGridEditView.getImageUrls());
			}
		}

		if (imagePaths.size() > 0) {
			new ImageUploadTask(this, imagePaths) {
				@Override
				public void onUploadSuccess(List<String> imageUrls) {
					doSubmitReview(imageUrls);
				}
			}.execute();
		} else {
			doSubmitReview(null);
		}
	}

	private void doSubmitReview(List<String> imageUrls) {
		List<MallProductReviewBean> reviews = new ArrayList<MallProductReviewBean>();
		MallAddProductReviewRequest request = new MallAddProductReviewRequest();
		request.setReviews(reviews);
		request.setAnonymous(checkBox_anonymous.isSelected());
		request.setReviewIndex(isAppend ? 1 : 0);

		for (MallAddProductReviewCardHolder holder : addReviewHolders) {
			String content = holder.editText_content.getText().toString();
			if (TextUtils.isEmpty(content)) {
				continue;
			}
			int imagesCount = holder.imagesGridEditView.imagesCount();
			MallProductReviewBean review = new MallProductReviewBean();
			reviews.add(review);
			review.setOrderProductID(holder.productBean.getID());
			review.setContent(content);
			review.setRatingDescription(holder.ratingBar_description.getRating());
			review.setRatingQuality(holder.ratingBar_quality.getRating());
			review.setRatingSpeed(holder.ratingBar_speed.getRating());
			if (imagesCount > 0) {
				review.setPictureUrls(imageUrls.subList(0, imagesCount));
				imageUrls = imageUrls.subList(imagesCount, imageUrls.size());
			}
		}

		ResponseHandler<MallAddProductReviewResponse> responseHandler = new ResponseHandler<MallAddProductReviewResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在提交");
			}

			@Override
			public void onFinish(MallAddProductReviewResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error) && !hasDestroyed()) {
					Toast.makeText(getActivity(), "评价成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(getActivity(), MallProductReviewSuccessActivity.class);
					intent.putExtra(MallProductReviewSuccessActivity.IntentKey_Points, response.getPoints());
					startActivity(intent);
					finish();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallAddProductReviewResponse.class, responseHandler, true);
	}
}

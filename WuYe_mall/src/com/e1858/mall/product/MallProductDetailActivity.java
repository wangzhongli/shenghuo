package com.e1858.mall.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.BaseApplication;
import com.e1858.common.app.WebViewActivity;
import com.e1858.mall.cart.MallCartActivity;
import com.e1858.mall.order.MallConfirmOrderActivity;
import com.e1858.mall.protocol.bean.MallConfirmOrderBean;
import com.e1858.mall.protocol.bean.MallOrderProductBean;
import com.e1858.mall.protocol.bean.MallProductBean;
import com.e1858.mall.protocol.bean.MallProductReviewBean;
import com.e1858.mall.protocol.bean.MallProductTagBean;
import com.e1858.mall.protocol.packet.MallGetProductRequest;
import com.e1858.mall.protocol.packet.MallGetProductResponse;
import com.e1858.mall.protocol.packet.MallGetProductReviewsRequest;
import com.e1858.mall.protocol.packet.MallGetProductReviewsResponse;
import com.e1858.mall.review.MallProductReviewActivity;
import com.e1858.mall.utils.MallCartBadgeManager;
import com.e1858.mall.utils.MallRequestHelper;
import com.e1858.mall.widget.MallProductReviewCardHolder;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;
import com.hg.android.widget.ImagePagerLayout;

public class MallProductDetailActivity extends BaseActionBarActivity {
	public static final String	IntentKey_ProductID	= "IntentKey_ProductID";
	String						productID			= null;
	MallProductBean				productBean;
	MallCartBadgeManager		cartBadgeManager	= new MallCartBadgeManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_activity_productdetail);

		if (getIntent() != null) {
			productID = getIntent().getStringExtra(IntentKey_ProductID);
		}
		if (productID == null) {
			finish();
			return;
		}

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		initViews();

		loadProductDetail(productID);
		loadProductReviews();
	}

	void loadProductDetail(String productID) {

		MallGetProductRequest request = new MallGetProductRequest();
		request.setID(productID);
		ResponseHandler<MallGetProductResponse> responseHandler = new ResponseHandler<MallGetProductResponse>() {

			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetProductResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					productBean = response.getProduct();
					findViewById(R.id.progressContainer).setVisibility(View.GONE);
					updateViews();
				} else {
					finish();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallGetProductResponse.class, responseHandler, true);
	}

	protected void loadProductReviews() {

		ResponseHandler<MallGetProductReviewsResponse> responseHandler = new ResponseHandler<MallGetProductReviewsResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetProductReviewsResponse response, String error) {
				if (response != null && response.isSuccess()) {
					updateReview(response);
				}
			}
		};

		MallGetProductReviewsRequest request = new MallGetProductReviewsRequest();
		request.setOffset(0);
		request.setCount(1);
		request.setProductID(productID);
		HttpPacketClient.postPacketAsynchronous(request, MallGetProductReviewsResponse.class, responseHandler, true);
	}

	void initViews() {
		findViewById(R.id.button_cart).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				add2cart();
			}
		});
		findViewById(R.id.button_buy).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buyNow();
			}
		});
		{
			View webBtn = findViewById(R.id.listitem_webdetail);
			webBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!TextUtils.isEmpty(productBean.getDetailUrl())) {
						Intent intent = new Intent(MallProductDetailActivity.this, WebViewActivity.class);
						intent.putExtra(WebViewActivity.IntentKey_Title, "商品图文详情");
						intent.putExtra(WebViewActivity.IntentKey_URL, productBean.getDetailUrl());
						startActivity(intent);
					} else {
						Toast.makeText(MallProductDetailActivity.this, "抱歉,没有图文详情", Toast.LENGTH_SHORT).show();
					}
				}
			});
			((TextView) webBtn.findViewById(R.id.textView)).setText("查看详情");
		}

		{
			View listitem_review = findViewById(R.id.listitem_review);
			OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), MallProductReviewActivity.class);
					intent.putExtra(MallProductReviewActivity.IntentKey_ProductID, productID);
					startActivity(intent);
				}
			};
			listitem_review.setOnClickListener(listener);
			((TextView) listitem_review.findViewById(R.id.textView)).setText("商品评价");

			findViewById(R.id.productReviewCardView).setOnClickListener(listener);
		}
	}

	void updateViews() {

		ImagePagerLayout imagePagerLayout = (ImagePagerLayout) findViewById(R.id.imagePagerLayout);
		imagePagerLayout.setImageUrls(productBean.getImageUrls());

		TextView textView_introduce = (TextView) findViewById(R.id.textView_introduce);
		TextView textView_title = (TextView) findViewById(R.id.textView_title);
		TextView textView_tags = (TextView) findViewById(R.id.textView_tags);
		TextView textView_price = (TextView) findViewById(R.id.textView_price);
		TextView textView_priceBase = (TextView) findViewById(R.id.textView_priceBase);
		TextView textView_quantity = (TextView) findViewById(R.id.textView_quantity);
		TextView textView_shippingCost = (TextView) findViewById(R.id.textView_shippingCost);
		textView_introduce.setText(productBean.getIntroduce());
		textView_title.setText(productBean.getName());
		textView_price.setText(String.format("¥%.2f", productBean.getPrice()));
		textView_priceBase.setText(String.format("价格:¥%.2f", productBean.getPriceBase()));
		textView_priceBase.getPaint().setFlags(textView_priceBase.getPaint().getFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		textView_quantity.setText("库存数量:" + productBean.getQuantity());
		textView_shippingCost.setText("快递:0.00元");

		StringBuilder tags = new StringBuilder();
		if (productBean.getTags() != null) {
			for (MallProductTagBean tag : productBean.getTags()) {
				tags.append(tag.getName());
				tags.append(" ");
			}
			if (tags.length() > 0) {
				tags.deleteCharAt(tags.length() - 1);
			}
		}
		textView_tags.setText(tags);

		textView_tags.setVisibility(tags.length() > 0 ? View.VISIBLE : View.GONE);
		textView_introduce.setVisibility(textView_introduce.getText().length() > 0 ? View.VISIBLE : View.GONE);
	}

	void updateReview(MallGetProductReviewsResponse response) {
		View listitem_review = findViewById(R.id.listitem_review);
		((TextView) listitem_review.findViewById(R.id.textView)).setText("商品评价(" + response.getTotalCount() + ")");

		if (HGUtils.isListEmpty(response.getReviews())) {
			return;
		}

		findViewById(R.id.view_sep_review).setVisibility(View.VISIBLE);
		MallProductReviewCardHolder holder = new MallProductReviewCardHolder(getActivity(),
				findViewById(R.id.productReviewCardView));
		holder.getView().setVisibility(View.VISIBLE);
		MallProductReviewBean bean = response.getReviews().get(0);
		bean.setAppendReview(null);
		holder.updateForBean(bean);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		cartBadgeManager.unregister();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_productdetail, menu);
		cartBadgeManager.register(menu.findItem(R.id.mall_action_cart), this);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_cart) {
			if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(getActivity())) {
				startActivity(new Intent(MallProductDetailActivity.this, MallCartActivity.class));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void add2cart() {
		if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(getActivity())) {
			MallRequestHelper.add2cart(productBean, getActivity());
		}
	}

	void buyNow() {
		if (!BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(getActivity())) {
			return;
		}
		MallOrderProductBean orderProduct = new MallOrderProductBean();
		orderProduct.setID(null);
		orderProduct.setProductID(productBean.getID());
		orderProduct.setThumbUrl(productBean.getThumbUrl());
		orderProduct.setPrice(productBean.getPrice());
		orderProduct.setQuantity(1);
		orderProduct.setShopID(productBean.getShopID());
		orderProduct.setName(productBean.getName());

		MallConfirmOrderBean confirmOrder = new MallConfirmOrderBean();
		confirmOrder.setShopID(productBean.getShopID());
		confirmOrder.setShopName(productBean.getShopName());
		List<MallOrderProductBean> orderProducts = new ArrayList<MallOrderProductBean>();
		orderProducts.add(orderProduct);
		confirmOrder.setOrderProducts(orderProducts);

		Intent intent = new Intent(this, MallConfirmOrderActivity.class);
		intent.putExtra(MallConfirmOrderActivity.IntentKey_ConfirmOrders,
				(Serializable) Arrays.asList(new MallConfirmOrderBean[] { confirmOrder }));
		startActivity(intent);
	}
}

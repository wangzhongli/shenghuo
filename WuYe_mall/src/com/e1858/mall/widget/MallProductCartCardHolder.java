package com.e1858.mall.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.entity.MallCartShopEntity;
import com.e1858.mall.product.MallProductDetailActivity;
import com.e1858.mall.protocol.bean.MallCartProductBean;
import com.e1858.mall.protocol.packet.MallEditCartProductRequest;
import com.e1858.mall.protocol.packet.MallEditCartProductResponse;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.SqliteOpenHelper;
import com.e1858.wuye.mall.R;
import com.hg.android.ormlite.extra.OrmLiteNotificationCenter;
import com.hg.android.widget.EditTextDialog;
import com.hg.android.widget.EditTextDialog.OnFinishedListener;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallProductCartCardHolder extends MallViewBaseHolder {

	private MallCartProductBean	productBean;

	public ImageView			imageView_product;
//	public TextView			textView_introduce;

	public TextView				textView_amount;
	public TextView				textView_title;
//	public TextView			textView_tags;
	public TextView				textView_quantity;
	public TextView				textView_price;

	public View					button_plus;
	public View					button_minus;

	public Object				object;

	public MallProductCartCardHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_product_cart);
	}

	int getQuantity() {
		try {
			return Integer.parseInt(textView_quantity.getText().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	void showEditQuantityDialog() {
		OnFinishedListener l = new OnFinishedListener() {

			@Override
			public void onFinished(String text) {
				try {
					int quantity = Integer.parseInt(text);
					if (quantity < 1) {
						Toast.makeText(getContext(), "数量不能小于1", Toast.LENGTH_SHORT).show();
					} else {
						want2changeQuantity(quantity);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new EditTextDialog(getContext(), "修改数量", null, textView_quantity.getText(), l).show();
	}

	@Override
	protected void initSubviews() {
		imageView_product = (ImageView) findViewById(R.id.imageView_product);
		textView_quantity = (TextView) findViewById(R.id.textView_quantity);
		textView_title = (TextView) findViewById(R.id.textView_title);
		textView_price = (TextView) findViewById(R.id.textView_price);
		textView_amount = (TextView) findViewById(R.id.textView_amount);

		textView_quantity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showEditQuantityDialog();
			}
		});

		getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), MallProductDetailActivity.class);
				intent.putExtra(MallProductDetailActivity.IntentKey_ProductID, productBean.getProductID());
				getContext().startActivity(intent);
			}
		});

		button_plus = findViewById(R.id.button_plus);
		button_minus = findViewById(R.id.button_minus);
		button_minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				want2minusQuantity();
			}
		});
		button_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				want2plusQuantity();
			}
		});
	}

	void want2changeQuantity(int quantity) {
		MallCartProductBean tmp = ResponseUtils.gson().fromJson(ResponseUtils.gson().toJson(productBean),
				MallCartProductBean.class);
		tmp.setQuantity(quantity);
		editCartProduct(tmp);
	}

	void want2minusQuantity() {
		if (productBean.getQuantity() <= 1) {
			return;
		}
		MallCartProductBean tmp = ResponseUtils.gson().fromJson(ResponseUtils.gson().toJson(productBean),
				MallCartProductBean.class);
		tmp.setQuantity(productBean.getQuantity() - 1);
		editCartProduct(tmp);
	}

	void editCartProduct(final MallCartProductBean productBean) {
		MallEditCartProductRequest request = new MallEditCartProductRequest();
		request.setID(productBean.getID());
		request.setSkuID(productBean.getSkuID());
		request.setQuantity(productBean.getQuantity());
		ResponseHandler<MallEditCartProductResponse> responseHandler = new ResponseHandler<MallEditCartProductResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getContext(), "正在处理中");
			}

			@Override
			public void onFinish(MallEditCartProductResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					MallProductCartCardHolder.this.productBean = productBean;
					textView_quantity.setText("" + productBean.getQuantity());
					SqliteOpenHelper openHelper = OpenHelperManager.getHelper(BaseApplication.getBaseInstance(),
							SqliteOpenHelper.class);
					try {
						Dao<MallCartShopEntity, String> dao = openHelper.getDao(MallCartShopEntity.class);
						MallCartShopEntity entity = dao.queryForId(productBean.getShopID());
						if (entity != null) {
							entity.getBean().updateProductBean(productBean);
							entity.setBean(entity.getBean());
							dao.createOrUpdate(entity);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					OpenHelperManager.releaseHelper();
					OrmLiteNotificationCenter.sharedInstance().notifyChange(MallCartShopEntity.class);
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, MallEditCartProductResponse.class, responseHandler, true);
	}

	void want2plusQuantity() {
//		MallGetStockQuantityRequest request = new MallGetStockQuantityRequest();
//		request.setProductID(productBean.getProductID());
//		request.setSkuID(productBean.getSkuID());
//		ResponseHandler<MallGetStockQuantityResponse> responseHandler = new ResponseHandler<MallGetStockQuantityResponse>() {
//			@Override
//			public void onStart() {
//				showProgressDialog(getContext(), "正在处理中");
//			}
//
//			@Override
//			public void onFinish(MallGetStockQuantityResponse response, String error) {
//				hideProgressDialog();
//				if (MallUtils.checkResponseAndToastError(response, error)) {
//					if (response.getQuantity() >= productBean.getQuantity() + 1) {
//						MallCartProductBean tmp = MallUtils.gson().fromJson(MallUtils.gson().toJson(productBean),
//								MallCartProductBean.class);
//						tmp.setQuantity(productBean.getQuantity() + 1);
//						editCartProduct(tmp);
//					} else {
//						Toast.makeText(getContext(), "库存不足", Toast.LENGTH_LONG).show();
//					}
//				}
//			}
//		};
//		HttpPacketClient.postPacketAsynchronous(request, MallGetStockQuantityResponse.class, responseHandler);
		MallCartProductBean tmp = ResponseUtils.gson().fromJson(ResponseUtils.gson().toJson(productBean),
				MallCartProductBean.class);
		tmp.setQuantity(productBean.getQuantity() + 1);
		editCartProduct(tmp);
	}

	public void updateForEntity(MallCartProductBean productBean) {
		this.productBean = productBean;

		ImageLoader.getInstance().displayImage(productBean.getThumbUrl(), imageView_product,
				BaseApplication.displayImageOptions());
		textView_title.setText(productBean.getName());
		textView_price.setText(String.format("价格:¥%.2f", productBean.getPrice()));
		textView_amount.setText(String.format("合计:¥%.2f", productBean.getPrice() * productBean.getQuantity()));
		textView_quantity.setText("" + productBean.getQuantity());

		button_minus.setEnabled(productBean.getQuantity() > 1);
	}

	public MallCartProductBean getProductBean() {
		return productBean;
	}

}

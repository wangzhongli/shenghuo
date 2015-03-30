package com.e1858.mall.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.e1858.mall.protocol.bean.MallCartProductBean;
import com.e1858.mall.protocol.bean.MallCartShopBean;
import com.e1858.wuye.mall.R;
import com.hg.android.utils.HGUtils;

public class MallShopCartCardHolder extends MallViewBaseHolder implements OnClickListener {
	ViewGroup						productGroupView;
	CheckBox						checkBox_shopName;

	OnProductCheckedChangeListener	onCheckChangedListener;

	MallCartShopBean				shopBean;

	List<CheckBox>					checkBoxs	= new ArrayList<CheckBox>();

	public MallShopCartCardHolder(Context context, View view) {
		super(context, view, R.layout.mall_card_shop_cart);
	}

	@Override
	protected void initSubviews() {
		checkBox_shopName = (CheckBox) findViewById(R.id.checkBox_shopName);
		checkBox_shopName.setOnClickListener(this);
		productGroupView = (ViewGroup) findViewById(R.id.productContainer);
	}

	public void updateForShop(MallCartShopBean shopBean, Map<String, MallCartProductBean> checkedProducts) {
		this.shopBean = shopBean;
		checkBox_shopName.setText(shopBean.getShopName());

		checkBoxs.clear();
		productGroupView.removeAllViews();
		if (HGUtils.isListEmpty(shopBean.getCartProducts())) {
			return;
		}

		boolean isCheckedAll = true;
		for (int count = shopBean.getCartProducts().size(), i = 0; i < count; i++) {
			View view = View.inflate(getContext(), R.layout.mall_listitem_cart, null);
			MallProductCartCardHolder holder = new MallProductCartCardHolder(getContext(),
					view.findViewById(R.id.cartProductCardView));
			view.setTag(holder);
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
			checkBoxs.add(checkBox);
			MallCartProductBean product = shopBean.getCartProducts().get(i);
			product.setShopID(shopBean.getID());//服务器没传这个值shopID
			checkBox.setChecked(checkedProducts.containsKey(product.getID()));
			if (!checkBox.isChecked()) {
				isCheckedAll = false;
			}
			checkBox.setTag(i);
			checkBox.setOnClickListener(this);
			holder.updateForEntity(product);
			productGroupView.addView(view);
			if (i < count - 1) {
				View.inflate(getContext(), R.layout.mall_sep_h, productGroupView);
			}
		}
		checkBox_shopName.setChecked(isCheckedAll);
	}

	public void setOnCheckChangedListener(OnProductCheckedChangeListener onCheckChangedListener) {
		this.onCheckChangedListener = onCheckChangedListener;
	}

	public interface OnProductCheckedChangeListener {
		public void onProductChecked(MallCartProductBean product, boolean checked);
	}

	@Override
	public void onClick(View arg0) {
		if (onCheckChangedListener == null) {
			return;
		}
		CheckBox checkBox = (CheckBox) arg0;
		boolean isChecked = checkBox.isChecked();
		if (this.checkBox_shopName == checkBox) {
			for (int count = checkBoxs.size(), i = 0; i < count; i++) {
				CheckBox iCheckBox = checkBoxs.get(i);
				iCheckBox.setChecked(isChecked);
				MallCartProductBean product = shopBean.getCartProducts().get(i);
				onCheckChangedListener.onProductChecked(product, isChecked);
			}
		} else {
			int i = (Integer) arg0.getTag();
			MallCartProductBean product = shopBean.getCartProducts().get(i);
			onCheckChangedListener.onProductChecked(product, checkBox.isChecked());
			if (!isChecked) {
				this.checkBox_shopName.setChecked(false);
			}
		}
	}
}

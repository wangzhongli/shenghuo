package com.e1858.mall.protocol.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MallCartShopBean implements Serializable {
	public String						shopID;		//	
	public String						shopName;		//
	public List<MallCartProductBean>	cartProducts;

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<MallCartProductBean> getCartProducts() {
		return cartProducts;
	}

	public void setCartProducts(List<MallCartProductBean> cartProducts) {
		this.cartProducts = cartProducts;
	}

	public String getID() {
		return shopID;
	}

	public boolean removeProductByID(String ID) {
		int index = indexForCartProductID(ID);
		if (index >= 0) {
			cartProducts.remove(index);
			return true;
		}
		return false;
	}

	public int indexForCartProductID(String ID) {
		if (cartProducts == null) {
			return -1;
		}
		for (int i = cartProducts.size() - 1; i >= 0; i--) {
			MallCartProductBean product = cartProducts.get(i);
			if (product.getID().equals(ID)) {
				return i;
			}
		}
		return -1;
	}

	public void updateProductBean(MallCartProductBean product) {
		int index = indexForCartProductID(product.getID());
		if (index >= 0) {
			cartProducts.set(index, product);
		}
	}

}

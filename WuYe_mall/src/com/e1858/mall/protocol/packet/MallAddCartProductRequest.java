package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallAddCartProductRequest extends AutoFillPacketRequest {
	public String	productID;	//	string	商品ID
	public String	skuID;		//	string	单品ID(MallSKU.ID)
	public int		quantity;	//int	数量

	public MallAddCartProductRequest() {
		super(HttpDefine.MallAddCartProduct);
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getSkuID() {
		return skuID;
	}

	public void setSkuID(String skuID) {
		this.skuID = skuID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}

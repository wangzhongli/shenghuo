package com.e1858.mall.protocol.packet;

import com.e1858.mall.protocol.HttpDefine;
import com.e1858.wuye.protocol.http.AutoFillPacketRequest;

@SuppressWarnings("serial")
public class MallEditCartProductRequest extends AutoFillPacketRequest {

	public String	ID;			//	string	MallCartProduct.ID
	public String	skuID;		//	string	单品ID(MallSKU.ID)
	public int		quantity;	//	int	数量

	public MallEditCartProductRequest() {
		super(HttpDefine.MallEditCartProduct);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
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

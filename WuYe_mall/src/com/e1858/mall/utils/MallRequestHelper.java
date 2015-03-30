package com.e1858.mall.utils;

import android.app.Activity;
import android.widget.Toast;

import com.e1858.common.app.BaseApplication;
import com.e1858.mall.MallConstant.PrefKey;
import com.e1858.mall.protocol.bean.MallProductBean;
import com.e1858.mall.protocol.packet.MallAddCartProductRequest;
import com.e1858.mall.protocol.packet.MallAddCartProductResponse;
import com.e1858.mall.protocol.packet.MallGetCartProductCountRequest;
import com.e1858.mall.protocol.packet.MallGetCartProductCountResponse;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.ResponseUtils;

public class MallRequestHelper {

	public static void requestCartProductCount() {
		ResponseHandler<MallGetCartProductCountResponse> responseHandler = new ResponseHandler<MallGetCartProductCountResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetCartProductCountResponse response, String error) {
				if (response != null && response.isSuccess()) {
					MallDataPersister.getSharedPreferences().edit()
							.putInt(PrefKey.CartProductCount, response.getCount()).commit();
				}
			}
		};
		MallGetCartProductCountRequest request = new MallGetCartProductCountRequest();
		HttpPacketClient.postPacketAsynchronous(request, MallGetCartProductCountResponse.class, responseHandler, true);
	}

	public static void add2cart(MallProductBean productBean, Activity activity) {
		if (!BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(activity)) {
			return;
		}

		ResponseHandler<MallAddCartProductResponse> responseHandler = new ResponseHandler<MallAddCartProductResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallAddCartProductResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					Toast.makeText(BaseApplication.getBaseInstance(), "成功加入购物车", Toast.LENGTH_SHORT).show();
					MallRequestHelper.requestCartProductCount();//请求一次真实数据
				}
			}
		};
		MallAddCartProductRequest request = new MallAddCartProductRequest();
		request.setProductID(productBean.getID());
		request.setQuantity(1);
		HttpPacketClient.postPacketAsynchronous(request, MallAddCartProductResponse.class, responseHandler, true);
	}
}

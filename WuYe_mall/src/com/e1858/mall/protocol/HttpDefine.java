package com.e1858.mall.protocol;

public class HttpDefine {

	static int RESP(int code) {
		return 80000000 + code;
	}

//	1.MallGetProductCategories(command=200)	27
//	2.获取商品列表(command=201)	27
//	3.获取商品详情(command=202)	27
//	4.添加商品进购物车(command=203)	28
//	5.修改购物车内商品(command=204)	28
//	6.删除购物车内产品(command=205)	28
//	7.获取购物车内商品列表(command=206)	29
//	8.获取订单列表(command=207)	29
//	9.删除订单(command=208)	29
//	10.商品退换货(command=209)	29
//	11.提交付款前生成付款订单请求(command=210)	30
//	12.提交付款成功请求(command=211)	30
//	13.获取收货地址列表(command=212)	30
//	14.修改/增加收货地址(command=213)	31
//	15.删除收货地址(command=214)	31
//	16.下单(command=215)	31
//	17.取消订单(command=216)	32
//	18.删除订单(command=217)	32
	public static final int	MallGetProductCategories			= 200;									//获取商品大分类
	public static final int	MallGetProductCategories_RESP		= RESP(MallGetProductCategories);
	public static final int	MallGetProducts						= 201;									//
	public static final int	MallGetProducts_RESP				= RESP(MallGetProducts);
	public static final int	MallGetProductDetail				= 202;									//
	public static final int	MallGetProductDetail_RESP			= RESP(MallGetProductDetail);
	public static final int	MallAddCartProduct					= 203;									//
	public static final int	MallAddCartProduct_RESP				= RESP(MallAddCartProduct);
	public static final int	MallEditCartProduct					= 204;									//
	public static final int	MallEditCartProduct_RESP			= RESP(MallEditCartProduct);
	public static final int	MallDeleteCartProduct				= 205;									//
	public static final int	MallDeleteCartProduct_RESP			= RESP(MallDeleteCartProduct);
	public static final int	MallGetCartProducts					= 206;									//
	public static final int	MallGetCartProducts_RESP			= RESP(MallGetCartProducts);
	public static final int	MallGetOrders						= 207;									//
	public static final int	MallGetOrders_RESP					= RESP(MallGetOrders);
	public static final int	MallDeleteOrder						= 208;									//
	public static final int	MallDeleteOrder_RESP				= RESP(MallDeleteOrder);
	public static final int	MallRefund							= 209;									//
	public static final int	MallRefund_RESP						= RESP(MallRefund);
	public static final int	MallGeneratePaymentOrder			= 210;									//
	public static final int	MallGeneratePaymentOrder_RESP		= RESP(MallGeneratePaymentOrder);
	public static final int	MallPaySuccess						= 211;									//
	public static final int	MallPaySuccess_RESP					= RESP(MallPaySuccess);
	public static final int	MallGetManagedAddresses				= 212;									//
	public static final int	MallGetManagedAddresses_RESP		= RESP(MallGetManagedAddresses);
	public static final int	MallEditManagedAddress				= 213;									//
	public static final int	MallEditManagedAddress_RESP			= RESP(MallEditManagedAddress);
	public static final int	MallDeleteManagedAddress			= 214;									//
	public static final int	MallDeleteManagedAddress_RESP		= RESP(MallDeleteManagedAddress);
	public static final int	MallGenerateOrder					= 215;									//
	public static final int	MallGenerateOrder_RESP				= RESP(MallGenerateOrder);
	public static final int	MallCancelOrder						= 216;									//
	public static final int	MallCancelOrder_RESP				= RESP(MallCancelOrder);
	public static final int	MallGetCartProductCount				= 217;									//
	public static final int	MallGetCartProductCount_RESP		= RESP(MallGetCartProductCount);
	public static final int	MallGetStockQuantity				= 218;									//
	public static final int	MallGetStockQuantity_RESP			= RESP(MallGetStockQuantity);
	public static final int	MallReceiveOrder					= 219;
	public static final int	MallReceiveOrder_RESP				= RESP(MallReceiveOrder);
	public static final int	MallGetOrder						= 220;
	public static final int	MallGetOrder_RESP					= RESP(MallGetOrder);
	public static final int	MallGetMainData						= 221;
	public static final int	MallGetMainData_RESP				= RESP(MallGetMainData);

	//12.07推荐接口增加
	//获取我的推荐列表MallGetMyRecommend(command=231)
	public static final int	MallGetMyRecommend					= 231;
	public static final int	MallGetMyRecommend_RESP				= RESP(MallGetMyRecommend);
	//获取推荐商品的评论信息列表MallGetComment(command=232)
	public static final int	MallGetComment						= 232;
	public static final int	MallGetComment_RESP					= RESP(MallGetComment);
	//添加推荐商品的评论信息MallAddComment(command=233)
	public static final int	MallAddComment						= 233;
	public static final int	MallAddComment_RESP					= RESP(MallAddComment);
	//新增推荐MallAddRecommend(command=234)
	public static final int	MallAddRecommend					= 234;
	public static final int	MallAddRecommend_RESP				= RESP(MallAddRecommend);
	//查询已经购买的商品列表MallGetProductAlready(command=235)
	public static final int	MallGetProductAlready				= 235;
	public static final int	MallGetProductAlready_RESP			= RESP(MallGetProductAlready);
	//业主推荐展现MallGetPersonRecommend
	public static final int	MallGetPersonRecommend				= 236;
	public static final int	MallGetPersonRecommend_RESP			= RESP(MallGetPersonRecommend);
	//点赞
	public static final int	MallAddHeart						= 237;
	public static final int	MallAddHeart_RESP					= RESP(MallAddHeart);
	//删除推荐接口
	public static final int	MallDeleteRecommend					= 238;
	public static final int	MallDeleteRecommend_RESP			= RESP(MallDeleteRecommend);
	public static final int	MallGenerateOrderNewVersion			= 280;
	public static final int	MallGenerateOrderNewVersion_RESP	= RESP(MallGenerateOrderNewVersion);
	public static final int	MallGetProductReviews				= 240;
	public static final int	MallAddProductReview				= 241;
	public static final int	MallCalcShippingCost				= 242;

}

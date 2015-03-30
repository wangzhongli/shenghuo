package com.e1858.mall;

public class MallConstant {
	public static final int		FetchCount			= 10;
	public static final int		FetchCount_buyed	= 9;

	public static final String	PrefName			= "wuye_mall_data";

	public static class PrefKey {
		public static final String	CartProductCount	= "CartProductCount";
	}

	public static final class MallOrderStatus {
		//1：已下单 2：已付款 3待收货 4：已完成 5：已关闭 
		public static final int	Unknow			= -1;
		public static final int	All				= 0;
		public static final int	WaitingPay		= 1;
		public static final int	Paid			= 2;
		public static final int	WaitingReceive	= 3;
		public static final int	Done			= 4;
		public static final int	Closed			= 5;
		public static final int	WaitingReview	= 6;	//虚假的状态,它其实还是已完成
	}

	public static final class MallRefundStatus {
		//1：退货中 2：退货完成 3换货中 4：换货完成
		public static final int	Unknow		= -1;
		public static final int	Refunding	= 1;
		public static final int	Refunded	= 2;
		public static final int	Exchanging	= 3;
		public static final int	Exchanged	= 4;
	}

	public static final class MallOrderRefundType {
		//1.退货2.换货
		public static final int	Refund		= 1;
		public static final int	Exchange	= 2;
	}

}

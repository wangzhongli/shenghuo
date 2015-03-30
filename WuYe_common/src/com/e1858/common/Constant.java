package com.e1858.common;

import java.text.Collator;

public class Constant {
	public static final int			PICTURE_MAX_SIZE						= 1024 * 1024;																									//像素
	public static final int			PICTURE_MIN_SIDE_LENGTH					= 800;

	public final static String		Host_URL								= "http://182.92.174.68:803/";
//	public final static String		Host_URL								= "http://api.elife.net.cn/";
	public final static String		BASE_URL								= Host_URL + "api/hxapi";
	public final static String		UPLOAD_URL								= Host_URL + "api/hxapiupload";
	public final static String		PROTOCOL_URL							= Host_URL + "Agreement.aspx";
	public final static String		ABOUT_US_URL							= Host_URL + "About.aspx";
	public final static String		POINT_RULE_URL							= Host_URL + "WebPages/IntegralRule.aspx";
	public final static String		POINT_INTRODUCTION_URL					= Host_URL + "WebPages/IntegralDesc.aspx";

	public final static String		EMAIL									= "email";
	public final static String		DETAIL_ID								= "detail_ID";
	public final static String		DETAIL_TITLE							= "detail_Title";
	public final static String		HOUSE_LIST								= "houseList";
	public final static String		HOUSE_DATA								= "houseDate";
	public final static String		PIC_REMARK								= "fromPage";
	public final static String		NICKNAME								= "nickname";
	public final static int			SOCKET_RECONNECT_INTERVAL				= 5 * 1000;
	public final static int			SOCKET_ACTIVETEST_INTERVAL				= 90 * 1000;
	public final static int			CONNECT_TIMEOUT							= 60 * 1000;
	public final static int			NETWORK_CHECK_INTERVAL					= 5 * 1000;
	public final static int			NETWORK_AVAILABLE_SOCKET_OPEN_INTERVAL	= 5000;
	public final static int			LOCATION_LISTEN_INTERVAL				= 60 * 1000;
	public final static String		TokenSalt								= "HUANXIA2001*yuehaixuntong2006";

	public final static String		LUA_COMMENT								= "--";
	public final static String		ENCODING								= "UTF-8";
	public final static String		PATH_SEPARATOR							= System.getProperty("path.separator");
	public final static String		FILE_SEPARATOR							= System.getProperty("file.separator");
	public final static String		LINE_SEPARATOR							= System.getProperty("line.separator");
	public final static String		DATE_SEPARATOR							= "-";

	public final static String		CR_LF									= "\r\n";
	public final static String		TWO_HYPHEN								= "--";
	public final static String		BOUNDARY								= "--boundary=_NextPart_000_00EE_01C07425.958FDFE0";
	public final static String		DES3_KEY								= "www.e1858.com";
	public static final String		PICTURE_EXTENSION						= ".png";
	public static final String		IMAGE_TYPE								= "image/*";

	public final static float[]		COLOR_SELECTED							= new float[] { 0.308f, 0.609f, 0.082f, 0,
			0, 0.308f, 0.609f, 0.082f, 0, 0, 0.308f, 0.609f, 0.082f, 0, 0, 0, 0, 0, 1, 0 };

	public final static int			ONE_KB									= 1024;
	public final static int			ONE_MB									= ONE_KB * 1024;
	public final static int			CACHE_SIZE								= 5 * ONE_MB;
	public final static int			FREE_SD_SPACE_NEEDED_TO_CACHE			= CACHE_SIZE;
	public final static String		CACHDIR									= "data/ceapp/cache/imgcache";
	public final static String		APKNAME									= "WuYe_App.apk";
	public final static String		WHOLESALE_CONV							= ".cach";
	public final static int			REMOVE_CACHE_PERCENT					= 40;
	public final static int			CWJ_HEAP_SIZE							= 6 * 1024 * 1024;
	public final static Collator	COLLATOR								= Collator
																					.getInstance(java.util.Locale.CHINA);

	public static String			ROOT_PATH								= "";
	public final static String		ROOT_NAME								= "wuye";
	public static String			CRASH_PATH								= "";
	public static String			CACHE_PATH								= "data/wuye/cache/";
	public static String			DATABASE_PATH							= "data/wuye/database/";

	public final static String		EMAIL_REGP								= "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
	public final static String		MOBILE_REGP								= "^1[3|4|5|8][0-9]\\d{8}$";
	public final static String		PASSWORD_REGP							= "[a-zA-Z0-9]{6,24}";
	public final static String		CAR_NUMBER_REGP							= "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$";
	public final static int			PAGE_SIZE								= 10;

	public final static class ToastMessage {
		public final static String	EMPTY_USERNAME			= "请输入用户名";
		public final static String	EMPTY_PASSWORD			= "请输入密码";
		public final static String	EMPTY_EMAIL				= "请输入您的邮箱";
		public final static String	EMAIL_REGP_ERROR		= "请输入正确的邮箱格式";
		public final static String	CONFIRM_PASSWORD		= "请再次确认密码";
		public final static String	USED_USERNAME			= "您输入的用户名已存在,请重新输入.";
		public final static String	NO_EQUAL_PASSWORD		= "您输入的密码不一致,请重新输入";
		public final static String	USERNAME_LACK_LENGTH	= "用户名长度低于6个字符,请重新输入";
		public final static String	PASSWORD_LACK_LENGTH	= "请输入6-24位数字或字母";
		public final static String	EMPTY_VERIFY			= "请输入验证码";
		public final static String	SUCCESS_EMAIL			= "验证码发送成功!";
		public final static String	FAIL_EMAIL				= "验证码发送失败,请稍后重试!";
		public final static String	RESETPASS_SUCCESS		= "重置密码成功!";
		public final static String	RESETPASS_FAIL			= "重置密码失败,请稍后重试";
		public final static String	REGIST_FAIL				= "注册失败!";
		public final static String	AGREEMENT				= "请同意注册许可及服务协议";
		public final static String	NO_COMMUNITY			= "对不起,请选择您的小区";
		public final static String	MOBILE_REGP_ERROR		= "请输入有效的手机号码";
	}

	public final static class DIRECTION {
		public final static int	FORWARD		= 1;
		public final static int	BACKWARD	= 2;
	}

	public final static class FASTCMD {
		public final static int	NOTICE			= 5;
		public final static int	PAY_WUYE		= 8;
		public final static int	INTERACT		= 6;
		public final static int	CALL_WUYE		= 7;
		public final static int	COMPLAINT		= 4;
		public final static int	COMMISSION		= 2;
		public final static int	MAINTENANCE		= 1;
		public final static int	SERVICE_PHONE	= 3;
		public final static int	KANGEL			= 9;
		public final static int	LOVEAROUND		= 10;
	}

	public final static class ADDSUCCESS_MODULE {
		public final static int	COMMISSION	= 1;
		public final static int	COMPLAINT	= 2;
		public final static int	MAINTENANCE	= 3;
		public final static int	KANGEL		= 4;
	}

	public final static class PUSH_TYPE {
		public final static int	NOTICE		= 1;
		public final static int	COMMISSION	= 2;
		public final static int	COMPLAINT	= 3;
		public final static int	MAINTENANCE	= 4;
	}

	public final static class NOTICE_TYPE {
		public final static int	PUBLIC	= 1;
		public final static int	PRIVATE	= 2;
		public final static int	ALL		= 3;
	}

	public final static class PAYMENT_TYPE {
		public final static int	WUYE_PAYMENT	= 1;
		public final static int	WATER_PAYMENT	= 2;
		public final static int	ELEC_PAYMENT	= 3;
		public final static int	GAS_PAYMENT		= 4;
		public final static int	MOBILE_PAYMENT	= 5;
	}

	public final static int		UPDATE_TEXT							= 304;
	public final static int		FAIL_CODE							= 300;
	public final static int		UPLOAD_RESULT_CODE					= 301;
	public final static int		UPLOAD_FAIL_CODE					= 302;
	public final static int		UPLOAD_OVER							= 303;
	public static final String	EXCEPTION							= "服务连接失败,请稍后尝试！";
	public static final String	UPLOAD_EXCEPTION					= "上传失败,请稍后重试";
	public final static int		MAINTENANCE_EDIT_PIC_RESULT_CODE	= 201;
	public final static int		TOPIC_EDIT_PIC_RESULT_CODE			= 202;
	public final static int		SEE_PIC_RESULT_CODE					= 203;

	public static final int		CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE	= 100;
	public static final int		PICK_IMAGE_ACTIVITY_REQUEST_CODE	= 200;
	public final static int		HOUSE_INFO_RESULT_CODE				= 101;
	public final static int		NICK_NAME_RESULT_CODE				= 105;
	public final static int		SELECT_CITY_RESULT_CODE				= 106;
	public final static int		SELECT_COMMUNITY_RESULT_CODE		= 107;
	public final static int		PERSONAL_INFO_RESULT_CODE			= 108;
	public final static int		EMAIL_RESULT_CODE					= 109;

	public final static class CONVENIENCE_TYPE {
		public final static String	ONE_TITLE	= "生活服务";
		public final static String	TWO_TITLE	= "居家服务";
		public final static String	THREE_TITLE	= "公共服务";
		public final static byte	ONE			= 1;
		public final static byte	TWO			= 2;
		public final static byte	THREE		= 3;
	}

	public final static class ORDER_STATUS {
		public final static int	WAIT		= 1;
		public final static int	PICKUP		= 2;
		public final static int	EXCUTE		= 3;
		public final static int	SIGN		= 4;
		public final static int	CANCLE		= 5;
		public final static int	CANCLING	= 6;
		public final static int	FAIED		= 7;
	}

	public final static class ACTION_TYPE {
		public final static int	REGISTE			= 1;
		public final static int	RESET_PASSWORD	= 2;
	}

	public final static class BannerHost {
		public final static String	Mall		= "mall";
		public final static String	Recommend	= "recommend";
	}

	public final static String	ANDROID				= "ANDROID";
	public static final String	NOTIFICATION_INFO	= "NotifictionInfo";
	public static final int		PUSH_MSG			= 506;
}

package com.e1858.wuye.protocol.http;

public class HttpDefine
{
	public static final String DateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static final int	RESPONSE_SUCCESS =  0;
	
	public static final int REGIST=1; //注册用户
	public static final int REGIST_RESP=80000001;
	public static final int LOGIN=2;//登录
	public static final int LOGIN_RESP=80000002;
	public static final int LOGOUT=3;//退出
	public static final int LOGOUT_RESP=80000003;

	public static final int GET_VERIFY_CODE=4;//找回密码中的获取验证码
	public static final int GET_VERIFY_CODE_RESP=80000004;
	public static final int RESET_PASS=5;//重置密码
	public static final int RESET_PASS_RESP=80000005;

	public static final int GET_COMMUNITY_BY_AREA=6;//根据地区（省、地区、县区名称）获取小区列表
	public static final int GET_COMMUNITY_BY_AREA_RESP=80000006;
	
	public static final int GET_COMMUNITY_BYLOCATION=91;
	public static final int GET_COMMUNITY_BYLOCATION_RESP=80000091;
	
	public static final int CHANGE_COMMUNITY=7;//切换用户小区
	public static final int CHANGE_COMMUNITY_RESP=80000007;
	
//	public static final int GET_NOTICES=8;//获取通知列表
//	public static final int GET_NOTICES_RESP=80000008;
	public static final int GET_NOTICES_SELF=92;
	public static final int GET_NOTICES_SELF_RESP=800000092;
	
	public static final int READ_NOTICE_BY_ID=9;//根据通知ID获取该通知详细信息
	public static final int READ_NOTICE_BY_ID_RESP=80000009;
	public static final int GET_LAST_NOTICE=24;//获取小区最近一条通知
	public static final int GET_LAST_NOTICE_RESP=80000024;
	public static final int GET_SERVICE_PHONES=10;//获取小区电话列表
	public static final int GET_SERVICE_PHONES_RESP=80000010;
	
	public static final int GET_BBS_BOARDS=11;//获取论坛板块列表
	public static final int GET_BBS_BOARDS_RESP=80000011;
	public static final int GET_BBS_TOPICS=12;//获取论坛话题列表
	public static final int GET_BBS_TOPICS_RESP=80000012;
	public static final int GET_BBS_TOPICS_BY_CREATOR=13;//获取论坛帖子列表
	public static final int GET_BBS_TOPICS_BY_CREATOR_RESP=80000013;
	
	public static final int GET_BBS_POSTS=14;//获取论坛帖子列表
	public static final int GET_BBS_POSTS_RESP=80000014;
	public static final int GET_BBS_POSTS_BY_CREATOR=15;//获取论坛帖子列表
	public static final int GET_BBS_POSTS_BY_CREATOR_RESP=80000015;
	
	public static final int ADD_BBS_TOPIC=16;//增加论坛话题
	public static final int ADD_BBS_TOPIC_RESP=80000016;
	public static final int EDIT_BBS_TOPIC=17;//编辑论坛话题
	public static final int EDIT_BBS_TOPIC_RESP=80000017;
	public static final int DELETE_BBS_TOPIC=18;//删除论坛话题
	public static final int DELETE_BBS_TOPIC_RESP=80000018;
	public static final int ADD_BBS_COMMENT=19;//增加论坛帖子
	public static final int ADD_BBS_COMMENT_RESP=80000019;
	public static final int EDIT_BBS_POST=20;//编辑帖子
	public static final int EDIT_BBS_POST_RESP=80000020;
	public static final int DELETE_BBS_POST=21;//删除帖子
	public static final int DELETE_BBS_POST_RESP=80000021;
	
	public static final int GET_HOUSES=25;//切换用户房屋
	public static final int GET_HOUSES_RESP=80000025;
	
	public static final int GET_HOUSEUNITS=96;
	public static final int GET_HOUSEUNITS_RESP=80000096;
	
	public static final int GET_HOUSEFLOORS=97;
	public static final int GET_HOUSEFLOORS_RESP=80000097;
	
	
	public static final int CHANGE_HOUSE=26;//切换用户房屋
	public static final int CHANGE_HOUSE_RESP=80000026;

	public static final int GET_COMPLAINTS=27;//获取投诉建议列表
	public static final int GET_COMPLAINTS_RESP=80000027;
	public static final int GET_COMPLAINT_BY_ID=28;//获取投诉建议
	public static final int GET_COMPLAINT_BY_ID_RESP=80000028;
	public static final int ADD_COMPLAINT=29;//新增投诉建议
	public static final int ADD_COMPLAINT_RESP=80000029;
	public static final int EDIT_COMPLAINT=30;// 编辑
	public static final int EDIT_COMPLAINT_RESP=80000030;
	public static final int DELETE_COMPLAINT=31;//删除
	public static final int DELETE_COMPLAINT_RESP=80000031;
	public static final int ADD_COMPLAINTRESPONSE=32;//新增投诉建议回复
	public static final int ADD_COMPLAINTRESPONSE_RESP=80000032;
	public static final int EDIT_COMPLAINTRESPONSE=33;// 编辑回复
	public static final int EDIT_COMPLAINTRESPONSE_RESP=80000033;
	public static final int DELETE_COMPLAINTRESPONSE=34;//删除回复
	public static final int DELETE_COMPLAINTRESPONSE_RESP=80000034;
	
	public static final int GET_COMMISSIONS=36;//获取代办事物列表
	public static final int GET_COMMISSIONS_RESP=80000036;
	public static final int GET_COMMISSION_BY_ID=37;//获取代办事务
	public static final int GET_COMMISSION_BY_ID_RESP=80000037;
	public static final int GET_COMMISSION_TYPES=38;//获取代办事务类型列表
	public static final int GET_COMMISSION_TYPES_RESP=80000038;
	public static final int GET_COMMISSION_TEMPLETES=39;//获取代办事务模板列表
	public static final int GET_COMMISSION_TEMPLETES_RESP=80000039;
	public static final int ADD_COMMISSION=40;//申请代办事物
	public static final int ADD_COMMISSION_RESP=80000040;
	public static final int EDIT_COMMISSION=41;//修改
	public static final int EDIT_COMMISSION_RESP=80000041;
	public static final int DELETE_COMMISSION=42;//删除
	public static final int DELETE_COMMISSION_RESP=80000042;
	public static final int ADD_COMMISSIONRESPONSE=43;//代办回复
	public static final int ADD_COMMISSIONRESPONSE_RESP=80000043;
	public static final int EDIT_COMMISSIONRESPONSE=44;//修改回复
	public static final int EDIT_COMMISSIONRESPONSE_RESP=80000044;
	public static final int DELETE_COMMISSIONRESPONSE=45;//删除回复
	public static final int DELETE_COMMISSIONRESPONSE_RESP=80000045;
	
	public static final int GET_MAINTENANCES=50;//获取设备维修列表
	public static final int GET_MAINTENANCES_RESP=80000050;
	public static final int GET_MAINTENANCE_BY_ID=51;//获取代办事务
	public static final int GET_MAINTENANCE_BY_ID_RESP=80000051;
	public static final int GET_MAINTENANCE_TYPES=52;//获取代办事务类型列表
	public static final int GET_MAINTENANCE_TYPES_RESP=80000052;
	public static final int GET_MAINTENANCE_TEMPLETES=53;//获取代办事务模板列表
	public static final int GET_MAINTENANCE_TEMPLETES_RESP=80000053;
	public static final int ADD_MAINTENANCE=54;//申请设备维修
	public static final int ADD_MAINTENANCE_RESP=80000054;
	public static final int EDIT_MAINTENANCE=55;//修改
	public static final int EDIT_MAINTENANCE_RESP=80000055;
	public static final int DELETE_MAINTENANCE=56;//删除
	public static final int DELETE_MAINTENANCE_RESP=80000056;
	public static final int ADD_MAINTENANCERESPONSE=57;//设备维修回复
	public static final int ADD_MAINTENANCERESPONSE_RESP=80000057;
	public static final int EDIT_MAINTENANCERESPONSE=58;//修改回复
	public static final int EDIT_MAINTENANCERESPONSE_RESP=80000058;
	public static final int DELETE_MAINTENANCERESPONSE=59;//删除回复
	public static final int DELETE_MAINTENANCERESPONSE_RESP=80000059;
	
	
	public static final int GET_MY_PROFILE=65; //获取个人信息
	public static final int GET_MY_PROFILE_RESP=80000065;
	public static final int EDIT_MY_PROFILE=66;//修改个人信息
	public static final int EDIT_MY_PROFILE_RESP=80000066;
	
	public static final int GET_CONSUME_TYPES=70;//获取账单类型列表
	public static final int GET_CONSUME_TYPES_RESP=80000070;
	public static final int GET_CONSUMES=71;//根据类型、户号和时间段查询账单
	public static final int GET_CONSUMES_RESP=80000071;
	public static final int GET_UNPAY_CONSUMES=72;//根据类型、户号查询未缴费账单
	public static final int GET_UNPAY_CONSUMES_RESP=80000072;
	public static final int GET_CONSUME_BY_ID=73;//根据ID获取账单信息
	public static final int GET_CONSUME_BY_ID_RESP=80000073;
	public static final int PAY_CONSUME=74;//缴费
	public static final int PAY_CONSUME_RESP=80000074;
	
	public static final int GET_CONVENIENT_TYPES=80;//获取便民项目类型
	public static final int GET_CONVENIENT_TYPES_RESP=80000080;
	public static final int GET_CONVENIENTS=81;//获取小区便民项目
	public static final int GET_CONVENIENTS_RESP=80000081;
	public static final int GET_CONVENIENT_BY_ID=82;//根据ID获取小区便民项目
	public static final int GET_CONVENIENT_BY_ID_RESP=80000082;
	
	public static final int CHECK_MAIL=85;//验证用户邮箱
	public static final int CHECK_MAIL_RESP=80000085;//验证用户邮箱
	
	public static final int CHECK_VERSION=90;//检测新版本
	public static final int CHECK_VERSION_RESP=80000090;//检测新版本
	
	
	public static final int EDIT_HEADPORTRAIT=69;
	public static final int EDIT_HEADPORTRAIT_RESP=80000069;
	
	public static final int EDIT_NICKNAME=68;
	public static final int EDIT_NICKNAME_RESP=80000068;
	
	public static final int ADD_FEEDBACK=93;
	public static final int ADD_FEEDBACK_RESP=80000093;
	
	public static final int CHANGE_PASSWORD=106;
	public static final int CHANGE_PASSWORD_RESP=800000106;
	
	public static final int SET_GETUI_CLIENTID=107;
	public static final int SET_GETUI_CLIENTID_RESP=800000107;
	
	//缴费
	public static final int GET_FEETYPE=121;
	public static final int GET_FEETYPE_RESP=800000121;
	
	public static final int GET_FEES=122;
	public static final int GET_FEES_RESP=800000122;
	
	public static final int GENERATE_FEEBILL=123;
	public static final int GENERATE_FEEBILL_RESP=800000123;
	
//	public static final int PAY_SUCCESS=124;
//	public static final int PAY_SUCCESS_RESP=800000124;
	
	public static final int GET_FEEBILLS=125;
	public static final int GET_FEEBILLS_RESP=800000125;
	
	public static final int DELETE_FEEBILL=126;
	public static final int DELETE_FEEBILL_RESP=800000126;
	//康洁
	public static final int KANGEL_GENERATE_ORDER=150;
	public static final int KANGEL_GENERATE_ORDER_RESP=800000150;
	
	public static final int KANGEL_GET_PLATBASE_REGIN=151;
	public static final int KANGEL_GET_PLATBASE_REGIN_RESP=800000151;
	
	public static final int KANGEL_GET_ORDERS=152;
	public static final int KANGEL_GET_ORDERS_RESP=800000152;
	
	public static final int KANGEL_CANCLE_ORDER=153;
	public static final int KANGEL_CANCLE_ORDER_RESP=800000153;
	
	public static final int KANGEL_DELETE_ORDER=154;
	public static final int KANGEL_DELETE_ORDER_RESP=800000154;
	
	public static final int KANGEL_GET_ORDER=155;
	public static final int KANGEL_GET_ORDER_RESP=800000155;
	public static final int KANGEL_GET_ADDRESS=156;
	public static final int KANGEL_GET_ADDRESS_RESP=800000156;
	
	//根据城市手动搜索小区列表请求(command=401)
	public static final int GETCOMMUNITYBYKEY=94;
	public static final int GETCOMMUNITYBYKEY_RESP=80000094;
	// 根据城市手动选择小区列表请求(command=402)
	public static final int GETCOMMUNITYBYCITYNEW=95;
	public static final int GETCOMMUNITYBYCITYNEW_RESP=80000095;	
	
	// 积分
	public static final int PointGetMyProfile=300;
	public static final int PointGetMyProfile_RESP=80000300;
	public static final int PointGetRecords=301;
	public static final int PointGetRecords_RESP=80000301;
}

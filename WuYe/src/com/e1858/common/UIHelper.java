package com.e1858.common;
public class UIHelper
{
	public final static String TAG="UIHelper";
	public final static String packageName="com.e1858.wuye.activity";
	//根据传递的值不同，进入到不同的页面
	
	public static String  doIntent(int id){
		String intent="";
		switch(id){		
		case Constant.FASTCMD.NOTICE:
			intent=packageName+".NoticesActivity";
			break;
		case Constant.FASTCMD.PAY_WUYE:
			intent=packageName+".PaymentMainActivity";
			break;
		case Constant.FASTCMD.INTERACT:
			intent=packageName+".NeighborhoodActivity";
			break;
		case Constant.FASTCMD.CALL_WUYE:
			intent=packageName+".ServicePhoneActivity";
			break;
		case Constant.FASTCMD.COMMISSION:
			intent=packageName+".AddCommissionActivity";
			break;
		case Constant.FASTCMD.COMPLAINT:
			intent=packageName+".AddComplaintActivity";
			break;
		case Constant.FASTCMD.MAINTENANCE:
			intent=packageName+".AddMaintenanceActivity";
			break;
		case Constant.FASTCMD.SERVICE_PHONE:
			intent=packageName+".ServicePhoneActivity";
			break;
		case Constant.FASTCMD.KANGEL:
			intent=packageName+".AddKangelOrderActivity";
			break;
		default:
			break;
		}
		return intent;
	}
}

package com.e1858.common;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.CommissionDetailActivity;
import com.e1858.wuye.activity.CommissionsActivity;
import com.e1858.wuye.activity.ComplaintDetailActivity;
import com.e1858.wuye.activity.ComplaintsActivity;
import com.e1858.wuye.activity.KangelRecordDetailActivity;
import com.e1858.wuye.activity.MaintenanceDetailActivity;
import com.e1858.wuye.activity.MaintenancesActivity;
import com.e1858.wuye.activity.tabfragment.MainTabActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
/**
 * 添加成功弹出页面
 * @author jiajia
 *
 */
public class AddSuccessDialogManager {
	private static Dialog addSuccessDialog;
	public static  void createCallDialog(final Context context,final int command,final int id,final String uuid){
		addSuccessDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		addSuccessDialog.setContentView(R.layout.add_success_dialog);
		Window window = addSuccessDialog.getWindow();  
		WindowManager.LayoutParams lp=window.getAttributes(); 
		lp.dimAmount=0.8f; 
		window.setAttributes(lp); 
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		TextView content = (TextView) addSuccessDialog.findViewById(R.id.dialog_content);
		TextView title=(TextView)addSuccessDialog.findViewById(R.id.dialog_title);
		Button   okBtn = (Button) addSuccessDialog.findViewById(R.id.ok_btn);
		Button  cancleBtn = (Button) addSuccessDialog.findViewById(R.id.cancel_btn);
		switch(command){
		case Constant.ADDSUCCESS_MODULE.COMMISSION:
			title.setText("代办申请");
			content.setText("尊敬的业主,您的代办申请已提交!");
			break;
		case Constant.ADDSUCCESS_MODULE.COMPLAINT:
			title.setText("新增投诉与建议");
			content.setText("尊敬的业主,您的投诉与建议已提交!");
			break;
		case Constant.ADDSUCCESS_MODULE.MAINTENANCE:
			title.setText("新增设备报修");
			content.setText("尊敬的业主,您的设备报修申请已提交!");
			break;
		case Constant.ADDSUCCESS_MODULE.KANGEL:
			title.setText("提示");
			content.setText("恭喜您，您的订单已成功提交!");
			break;
		}
		
		okBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				addSuccessDialog.dismiss();
				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				switch(command){
				case Constant.ADDSUCCESS_MODULE.COMMISSION:
					
					intent=new Intent(context,MainTabActivity.class);
					intent.putExtra(MainTabActivity.IntentKey_TabButtonID, R.id.radio_button_personal);
					context.startActivity(intent);
					intent=new Intent(context,CommissionsActivity.class);
					context.startActivity(intent);
					intent = new Intent(context,CommissionDetailActivity.class);
					bundle.putInt(Constant.DETAIL_ID, id);
					intent.putExtras(bundle);
					context.startActivity(intent);
					((Activity)context).finish();
					break;
				case Constant.ADDSUCCESS_MODULE.COMPLAINT:
					intent=new Intent(context,MainTabActivity.class);
					intent.putExtra(MainTabActivity.IntentKey_TabButtonID, R.id.radio_button_personal);
					context.startActivity(intent);
					intent=new Intent(context,ComplaintsActivity.class);
					context.startActivity(intent);
					intent = new Intent(context,ComplaintDetailActivity.class);
					bundle.putInt(Constant.DETAIL_ID, id);
					intent.putExtras(bundle);
					context.startActivity(intent);
					((Activity)context).finish();
					break;
				case Constant.ADDSUCCESS_MODULE.MAINTENANCE:
					intent=new Intent(context,MainTabActivity.class);
					intent.putExtra(MainTabActivity.IntentKey_TabButtonID, R.id.radio_button_personal);
					context.startActivity(intent);
					intent=new Intent(context,MaintenancesActivity.class);
					context.startActivity(intent);
					intent = new Intent(context,MaintenanceDetailActivity.class);
					bundle.putInt(Constant.DETAIL_ID, id);
					intent.putExtras(bundle);
					context.startActivity(intent);
					((Activity)context).finish();
					break;
				case Constant.ADDSUCCESS_MODULE.KANGEL:
					intent=new Intent(context,KangelRecordDetailActivity.class);
					bundle.putString("uuid", uuid);
					intent.putExtras(bundle);
					context.startActivity(intent);
					break;
				}
				
			}
		});
		cancleBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				addSuccessDialog.dismiss();
				((Activity)context).finish();
			}
		});
//		addSuccessDialog.setCancelable(false);//禁止返回
		addSuccessDialog.show();
	}
}

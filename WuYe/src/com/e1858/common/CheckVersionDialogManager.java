package com.e1858.common;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.StringUtils;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.CheckVersion;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.service.UpdateService;
/**
 * 版本更新
 * @author jiajia
 *
 */
public class CheckVersionDialogManager {
//	private static Dialog updateDialog;
//	public static void checkVersion(final Context context,final Handler handler){
//		if (NetUtil.checkNetWorkStatus(context))
//		{
//			CheckVersion checkVersion=new CheckVersion();
//			checkVersion.setToken(Encrypt.MD5(Constant.TokenSalt));
//			checkVersion.setMobileOS(Constant.ANDROID);
//			String vercode = "";
//			try{
//				PackageManager manager = context.getPackageManager();
//				PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//				vercode=String.valueOf(info.versionCode);
//			}catch (Exception e) {
//					// TODO: handle exception
//				e.printStackTrace();
//			}
//			checkVersion.setVersionCode(vercode);
//			NetUtil.post(Constant.BASE_URL, checkVersion, handler, HttpDefine.CHECK_VERSION_RESP);
//		}else{
//			return;
//		}
//	}
//	public static void showDialog(final Context context,final Handler handler,String versionName,String content,final String url){
//		updateDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
//		updateDialog.setContentView(R.layout.update_verson_dialog);
//		Window window = updateDialog.getWindow();
//		WindowManager.LayoutParams lp=window.getAttributes(); 
//		lp.dimAmount=0.8f;  
//		window.setAttributes(lp); 
//		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//		TextView contentView=(TextView)updateDialog.findViewById(R.id.dialog_call_content);
//		try{
//			if(StringUtils.isEmpty(content)){
//				contentView.setText("更新程序bug,优化界面!");
//			}else{
//				contentView.setText("发现新版本:"+versionName+"\n"+Html.fromHtml(content.replace("#", "<br/>")));
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		Button ok_btn = (Button) updateDialog.findViewById(R.id.ok_btn);
//		Button cancleBtn = (Button) updateDialog.findViewById(R.id.cancel_btn);
//		ok_btn.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				updateDialog.dismiss();
//				Intent updateIntent =new Intent(context, UpdateService.class);  
//		        updateIntent.putExtra("download_url",url);  
//		        updateIntent.putExtra("currentActivity", context.getClass().getSimpleName());
//		        updateIntent.putExtra("forceUP", true);//非强制升级
//		        context.startService(updateIntent);
//			}
//		});
//		cancleBtn.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				updateDialog.dismiss();
//			}
//		});
//		updateDialog.show();
//	}
}

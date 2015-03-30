package com.e1858.common;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.e1858.wuye.R;
/**
 * 呼叫电话
 * @author jiajia
 *
 */
public class CallDialgoManager
{
	private static Dialog callDialog;
	public static  void createCallDialog(final Context context,final String phone)
	{
		callDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		callDialog.setContentView(R.layout.call_dialog);
		Window window = callDialog.getWindow();  
		WindowManager.LayoutParams lp=window.getAttributes(); 
		lp.dimAmount=0.8f;  
		window.setAttributes(lp); 
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		TextView callContent = (TextView) callDialog.findViewById(R.id.dialog_call_content);
		Button   okBtn = (Button) callDialog.findViewById(R.id.ok_btn);
		Button  cancleBtn = (Button) callDialog.findViewById(R.id.cancel_btn);
		callContent.setText(context.getResources().getString(R.string.text_call_content, phone));
		okBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				callDialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
				context.startActivity(intent);
			}
		});
		cancleBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				callDialog.dismiss();
			}
		});
		callDialog.show();
	}
}

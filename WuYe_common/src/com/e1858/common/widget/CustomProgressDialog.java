package com.e1858.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.R;

public class CustomProgressDialog extends Dialog
{
	@SuppressWarnings("unused")
	private Context context = null;
	private static CustomProgressDialog customProgressDialog = null;

	public CustomProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context,
				R.style.custom_progress_dialog);
		customProgressDialog.setContentView(R.layout.dialog_custom_progress);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (customProgressDialog == null) {
			return;
		}
		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	/**
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 */
	public CustomProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return customProgressDialog;
	}
}

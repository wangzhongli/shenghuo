package com.e1858.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.common.utils.ToastUtil;
import com.e1858.common.Constant;
import com.e1858.wuye.protocol.http.UploadJson;
import com.hg.android.utils.ImageUtils;

/**
 * 日期: 2015年2月5日 下午6:02:03
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public abstract class ImageUploadTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog			dialog;
	private Activity		activity;
	private List<String>	imagePaths;
	private String			error;
	List<String>			imageUrls;

	public ImageUploadTask(Activity activity, List<String> imagePaths) {
		this.activity = activity;
		dialog = new ProgressDialog(activity);
		this.imagePaths = imagePaths;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("正在上传图片");
		dialog.show();
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				ImageUploadTask.this.cancel(true);
				ToastUtil.show(activity, "图片上传已取消");
			}
		});
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		imageUrls = uploadImages(imagePaths);
		if (isCancelled() || imageUrls.size() < imagePaths.size()) {
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (dialog.isShowing()) {
			dialog.dismiss();
			if (result) {
				onUploadSuccess(imageUrls);
			} else if (!TextUtils.isEmpty(error)) {
				Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public abstract void onUploadSuccess(List<String> imageUrls);

	String uploadImage(String file) {
		if (file.startsWith("file://")) {
			file = file.substring("file://".length());
		}
		String destJPG = new File(activity.getExternalCacheDir(), System.currentTimeMillis() + ".jpg")
				.getAbsolutePath();
		if (!ImageUtils.scaleImage(file, destJPG, Constant.PICTURE_MIN_SIDE_LENGTH, Constant.PICTURE_MAX_SIZE)) {
			return null;
		}
		if (isCancelled()) {
			return null;
		}
		UploadJson json = HttpPacketClient.syncUpdaloadFile(destJPG);
		if (json == null || TextUtils.isEmpty(json.getUrl())) {
			if (json != null) {
				error = json.getError();
			}
			if (TextUtils.isEmpty(error)) {
				error = "图片上传出错";
			}
			return null;
		} else {
			return json.getUrl();
		}
	}

	List<String> uploadImages(List<String> files) {
		List<String> imageUrls = new ArrayList<String>();
		for (String file : files) {
			if (isCancelled()) {
				return imageUrls;
			}
			String url = uploadImage(file);
			if (!TextUtils.isEmpty(url)) {
				imageUrls.add(url);
			}
		}
		return imageUrls;
	}
}

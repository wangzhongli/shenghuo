package com.e1858.wuye.activity;

import java.io.File;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.common.app.AppManager;
import com.e1858.common.app.CropImageActivity;
import com.e1858.network.NetUtil;
import com.e1858.object.CityBean;
import com.e1858.utils.Encrypt;
import com.e1858.utils.ImageUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.widget.CircularImage;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.tabfragment.PersonalActivity;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.EditHeadPortrait;
import com.e1858.wuye.protocol.http.EditHeadPortraitResp;
import com.e1858.wuye.protocol.http.EditMyProfile;
import com.e1858.wuye.protocol.http.EditMyProfileResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.UploadJson;
import com.e1858.wuye.protocol.http.UserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 个人资料
 * 
 * @author jiajia 2014年8月22日
 * 
 */
public class PersonalInfoActivity extends BaseActivity implements
		OnClickListener {
	private RelativeLayout my_head_portrait;
	private CircularImage head_portrait;
	private TextView nickname;
	private TextView myCity;
	private TextView myCommunity;
	private TextView myAddress;
	private EditText myName;
	private EditText myPhone;
	private EditText myEmail;
	private EditText myCarNum;
	private LinearLayout changePass;
	private Button okBtn;
	private Dialog picDialog = null;
	private static String picFileFullName = "";
	private static final int PICTURE_CORP_RESULT = 300;
	private String imageUrl = "";
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.head_portrait_icon)
				.showImageForEmptyUri(R.drawable.head_portrait_icon)
				.showImageOnFail(R.drawable.head_portrait_icon)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		initView();
		initData(PreferencesUtils.getUserInfo());
	}

	private void initView() {
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		my_head_portrait = (RelativeLayout) findViewById(R.id.my_head_portrait);
		nickname = (TextView) findViewById(R.id.nickname);
		head_portrait = (CircularImage) findViewById(R.id.head_portrait);
		myCity = (TextView) findViewById(R.id.myCity);
		myAddress = (TextView) findViewById(R.id.myAddress);
		myCommunity = (TextView) findViewById(R.id.myCommunity);
		myName = (EditText) findViewById(R.id.myName);
		myPhone = (EditText) findViewById(R.id.myPhone);
		myEmail = (EditText) findViewById(R.id.input_email);
		myCarNum = (EditText) findViewById(R.id.myCarNum);
		changePass = (LinearLayout) findViewById(R.id.changePass);
		okBtn = (Button) findViewById(R.id.ok_btn);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(
				R.string.text_bar_personal_info));
		bar_leftBtn.setOnClickListener(this);
		my_head_portrait.setOnClickListener(this);
		nickname.setOnClickListener(this);
		// myCity.setOnClickListener(this);
		myAddress.setOnClickListener(this);
		// myCommunity.setOnClickListener(this);
		okBtn.setOnClickListener(this);
		changePass.setOnClickListener(this);
	}

	private void initData(UserInfo userInfo) {

		if (userInfo != null) {
			if (!StringUtils.isEmpty(userInfo.getNickname())) {
				nickname.setText(userInfo.getNickname());
			}
			if (!StringUtils.isEmpty(userInfo.getHeadPortrait())) {
				imageUrl = userInfo.getHeadPortrait();
				imageLoader.displayImage(userInfo.getHeadPortrait(),
						head_portrait, options);
				// ImageManager.from(this).displayImage(head_portrait,
				// resp.getHeadImgUrl(), R.drawable.head_portrait_icon, 80, 80);
			}
			if (!StringUtils.isEmpty(userInfo.getName())) {
				myName.setText(userInfo.getName());
			}
			if (!StringUtils.isEmpty(userInfo.getMobile())) {
				myPhone.setText(userInfo.getMobile());
			}
			if (!StringUtils.isEmpty(PreferencesUtils.getUserName())) {
				myPhone.setText(PreferencesUtils.getUserName());
			}
			if (!StringUtils.isEmpty(userInfo.getEmail())) {
				myEmail.setText(userInfo.getEmail());
			}
			if (!StringUtils.isEmpty(userInfo.getCarNumber())) {
				myCarNum.setText(userInfo.getCarNumber());
			}
			if (!StringUtils.isEmpty(PreferencesUtils.getCommunity().getCity())) {
				myCity.setText(PreferencesUtils.getCommunity().getCity());
			}
			if (StringUtils.isEmpty(PreferencesUtils.getCommunity().getCity())
					&& !StringUtils.isEmpty(PreferencesUtils.getCommunity()
							.getProvince())) {
				myCity.setText(PreferencesUtils.getCommunity().getProvince());
			}
			if (null != PreferencesUtils.getUserHouse()) {
				myAddress.setText(Util.getHouseInfo(false));
			}
			if (null != PreferencesUtils.getCommunity()) {
				myCommunity.setText(PreferencesUtils.getCommunity().getName());
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			closeProgressDialog();
			return true;
		}
		switch (msg.what) {
		case HttpDefine.EDIT_MY_PROFILE_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj) {
				EditMyProfileResp resp = JsonUtil.fromJson((String) msg.obj,
						EditMyProfileResp.class);
				if (null == resp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					ToastUtil.show(this, "保存成功!");
					bar_leftBtn.setEnabled(false);
					// 延迟两秒跳转
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							PersonalActivity.nickName = nickname.getText()
									.toString();
							if (!StringUtils.isEmpty(imageUrl)) {
								PersonalActivity.headBitmap = ((BitmapDrawable) head_portrait
										.getDrawable()).getBitmap();
							}
							UserInfo userInfo = PreferencesUtils.getUserInfo();
							userInfo.setCarNumber(myCarNum.getText().toString());
							userInfo.setEmail(myEmail.getText().toString());
							userInfo.setHeadPortrait(imageUrl);
							userInfo.setMobile(myPhone.getText().toString());
							userInfo.setName(myName.getText().toString());
							userInfo.setNickname(nickname.getText().toString());
							PreferencesUtils.setUserInfo(userInfo);
							PersonalActivity.isRefreshed = true;
							PersonalInfoActivity.this
									.setResult(Constant.PERSONAL_INFO_RESULT_CODE);
							AppManager.getAppManager().finishActivity();
						}
					}, 1000);
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case Constant.UPLOAD_RESULT_CODE:
			closeProgressDialog();
			if (null != (String) msg.obj) {
				UploadJson uploadJson = JsonUtil.fromJson((String) msg.obj,
						UploadJson.class);
				if (null == uploadJson) {
					break;
				} else {
					imageUrl = uploadJson.getUrl();
					EditHeadPortrait editHeadPortrait = new EditHeadPortrait();
					editHeadPortrait.setKey(PreferencesUtils.getLoginKey());
					editHeadPortrait.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()
							+ Constant.TokenSalt));
					editHeadPortrait.setHeadPortrait(imageUrl);
					NetUtil.post(Constant.BASE_URL, editHeadPortrait, handler,
							HttpDefine.EDIT_HEADPORTRAIT_RESP);
				}
			}
			msg.obj = null;
			break;
		case Constant.UPLOAD_FAIL_CODE:
			if (null != msg.obj) {
				closeProgressDialog();
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case HttpDefine.EDIT_HEADPORTRAIT_RESP:
			if (null != (String) msg.obj) {
				EditHeadPortraitResp resp = JsonUtil.fromJson((String) msg.obj,
						EditHeadPortraitResp.class);
				if (resp == null) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					application.setIsRefresh(true);
					break;
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}

		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.my_head_portrait:
			createSelectPicDialog();
			break;
		// case R.id.input_email:
		// intent = new Intent(this, EmailVerifyActivity.class);
		// bundle.putString(Constant.EMAIL, myEmail.getText().toString());
		// bundle.putInt(Constant.PIC_REMARK, 2);
		// intent.putExtras(bundle);
		// startActivityForResult(intent, Constant.EMAIL_RESULT_CODE);
		// break;
		case R.id.nickname:
			intent = new Intent(this, NickNameActivity.class);
			bundle.putString(NickNameActivity.USERNAME, nickname.getText().toString());
			intent.putExtras(bundle);
			startActivityForResult(intent, Constant.NICK_NAME_RESULT_CODE);
			break;
		case R.id.myCity:
			intent = new Intent(this, SwitchCityActivity.class);
			bundle.putString("CurrentCity", application.getCityName());
			bundle.putInt(Constant.PIC_REMARK, 1);
			intent.putExtras(bundle);
			startActivityForResult(intent, Constant.SELECT_CITY_RESULT_CODE);
			break;
		case R.id.myAddress:
			intent = new Intent(this, SwitchResidentAddressActivity.class);
			startActivityForResult(intent, Constant.HOUSE_INFO_RESULT_CODE);
			break;
		case R.id.myCommunity:
			if (StringUtils.isEmpty(myCity.getText().toString())) {
				ToastUtil.show(this, "请先选择城市");
			} else {
				intent = new Intent(this, SelectCommunityNewActivity.class);
				bundle = new Bundle();
				bundle.putInt(Constant.PIC_REMARK, 1);
				bundle.putString("CurrentCity", myCity.getText().toString());
				intent.putExtras(bundle);
				startActivityForResult(intent,
						Constant.SELECT_COMMUNITY_RESULT_CODE);
			}
			break;
		case R.id.ok_btn:
			editProfile();
			break;
		case R.id.bar_left_btn:
			PersonalInfoActivity.this
					.setResult(Constant.PERSONAL_INFO_RESULT_CODE);
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.changePass:
			intent = new Intent(this, ChangePasswordActivity.class);
			startActivity(intent);
			break;
		}

	}

	private void createSelectPicDialog() {
		picDialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		picDialog.setContentView(R.layout.select_pic_dialog);
		// 设置样式
		Window window = picDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.8f;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.pic_dialog_style);
		Button cameraBtn = (Button) picDialog.findViewById(R.id.camera_btn);
		Button pic_mapBtn = (Button) picDialog.findViewById(R.id.pic_map_btn);
		Button cancleBtn = (Button) picDialog.findViewById(R.id.cancel_btn);
		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picDialog.dismiss();
			}
		});
		cameraBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openCamera();
				picDialog.dismiss();
			}
		});
		pic_mapBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openAblum();
				picDialog.dismiss();
			}
		});
		picDialog.show();
	}

	private void openAblum() {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent,
					Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.show(this, "打开图库失败");
		}

	}

	private void openCamera() {
		try {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			// 判断存储卡是否可以用，可用进行存储
			if (hasSdcard()) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
						Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME)));
			}
			startActivityForResult(intent,
					Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} catch (Exception e) {
			ToastUtil.show(this, "打开相机失败");
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE:
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				startPhotoZoomFromUri(uri);
			}
			break;
		case Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode != RESULT_OK) {
				return;
			}
			if (hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				startPhotoZoomFromUri(Uri.fromFile(tempFile));
			} else {
				ToastUtil.show(PersonalInfoActivity.this, "未找到存储卡，无法存储照片！");
			}

			break;
		case PICTURE_CORP_RESULT:
			if (data != null) {
				String path = data.getStringExtra("cropImagePath");
				setPicToView(path);
			}
			break;
		case Constant.NICK_NAME_RESULT_CODE:
			if (data != null) {
				Bundle bundle = data.getExtras();
				nickname.setText(bundle.getString(Constant.NICKNAME));
			}
			break;
		// case Constant.EMAIL_RESULT_CODE:
		// if (data != null)
		// {
		// Bundle bundle = data.getExtras();
		// myEmail.setText(bundle.getString(Constant.EMAIL));
		// }
		// break;
		case Constant.HOUSE_INFO_RESULT_CODE:
			myAddress.setText(Util.getHouseInfo(false));
			break;
		case Constant.SELECT_CITY_RESULT_CODE:
			if (data == null || "".equals(data)) {
				return;
			} else {
				Bundle bundle = data.getExtras();
				CityBean cityBean = (CityBean) bundle.get("cityBean");
				myCity.setText(cityBean.getCityName());
			}

			break;
		case Constant.SELECT_COMMUNITY_RESULT_CODE:
			if (data == null || "".equals(data)) {
				return;
			} else {
				Bundle bundle = data.getExtras();
				Community community = (Community) bundle
						.get(Constant.HOUSE_DATA);
				myCommunity.setText(community.getName());
			}
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = this.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	/*
	 * public void startPhotoZoom(Uri uri) { try { Intent intent = new
	 * Intent("com.android.camera.action.CROP"); intent.setDataAndType(uri,
	 * "image/*"); // 设置在开启的Intent中设置显示的VIEW可裁剪 intent.putExtra("crop", "true");
	 * // 宽高的比例 intent.putExtra("aspectX", 1); intent.putExtra("aspectY", 1); //
	 * 裁剪图片宽高 intent.putExtra("outputX", 200); intent.putExtra("outputY", 200);
	 * intent.putExtra("return-data", true);
	 * PersonalInfoActivity.this.startActivityForResult(intent,
	 * PICTURE_CORP_RESULT); } catch (Exception e) { e.printStackTrace();
	 * ToastUtil.show(this, "图片剪切失败,请稍后重试"); }
	 * 
	 * }
	 */
	public void startPhotoZoomFromUri(Uri uri) {
		try {
			Intent intent = new Intent(PersonalInfoActivity.this,
					CropImageActivity.class);
			intent.putExtra("bitmap", uri);
			intent.putExtra("bitmap_name", "icon.jpg");
			intent.putExtra("cropWidth", 300);
			intent.putExtra("cropHeight", 300);
			PersonalInfoActivity.this.startActivityForResult(intent,
					PICTURE_CORP_RESULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * private void setPicToView(Intent picdata) { Bundle extras =
	 * picdata.getExtras(); if (extras != null) { Bitmap photo =
	 * extras.getParcelable("data"); picFileFullName =
	 * ImageUtil.savePhotoToSDCard
	 * (Environment.getExternalStorageDirectory().getAbsolutePath(),
	 * String.valueOf(System.currentTimeMillis()) + ".png", photo); Bitmap
	 * bitmap = ImageUtil.roundedCornerBitmap(photo, 2);
	 * head_portrait.setImageBitmap(bitmap); PersonalActivity.headBitmap =
	 * bitmap; } openProgressDialog("上传中..."); if
	 * (!StringUtils.isEmpty(picFileFullName)) {
	 * NetUtil.upload(Constant.UPLOAD_URL, null, picFileFullName, handler,
	 * Constant.UPLOAD_RESULT_CODE); } }
	 */
	private void setPicToView(String path) {
		Drawable bitmap = BitmapDrawable.createFromPath(path);
		BitmapDrawable bd = (BitmapDrawable) bitmap;
		Bitmap bm = ImageUtil.roundedCornerBitmap(bd.getBitmap(), 2);
		head_portrait.setImageBitmap(bm);
		PersonalActivity.headBitmap = bm;
		openProgressDialog("上传中...");
		if (!StringUtils.isEmpty(path)) {
			NetUtil.upload(Constant.UPLOAD_URL, null, path, handler,
					Constant.UPLOAD_RESULT_CODE);
		}
	}

	private void editProfile() {
		if (StringUtils.isBlank(nickname.getText().toString())) {
			ToastUtil.show(this, "昵称不能为空");
		} else if (!(StringUtils.isBlank(myEmail.getText().toString()))
				&& !(Pattern.matches(Constant.EMAIL_REGP, myEmail.getText()
						.toString()))) {
			ToastUtil.show(this, Constant.ToastMessage.EMAIL_REGP_ERROR);
		} else if (!(StringUtils.isBlank(myPhone.getText().toString()))
				&& !(Pattern.matches(Constant.MOBILE_REGP, myPhone.getText()
						.toString()))) {
			ToastUtil.show(this, Constant.ToastMessage.MOBILE_REGP_ERROR);
		} else if (!(StringUtils.isBlank(myCarNum.getText().toString()))
				&& !(Pattern.matches(Constant.CAR_NUMBER_REGP, myCarNum
						.getText().toString()))) {
			ToastUtil.show(this, "车牌号输入错误,请重新输入!");
		} else if (StringUtils.isEquals(myName.getText().toString(),
				PreferencesUtils.getUserInfo().getName())
				&& StringUtils.isEquals(myEmail.getText().toString(),
						PreferencesUtils.getUserInfo().getEmail())
				&& StringUtils.isEquals(myPhone.getText().toString(),
						PreferencesUtils.getUserInfo().getMobile())
				&& StringUtils.isEquals(myCarNum.getText().toString(),
						PreferencesUtils.getUserInfo().getCarNumber())) {
			ToastUtil.show(this, "对不起,您未做任何修改");
		} else {
			submit();
		}
	}

	private void submit() {
		try {
			if (application.isNetworkAvailable()) {
				openProgressDialog("保存中...");
				EditMyProfile editMyProfile = new EditMyProfile();
				// editMyProfile.setKey(application.getKey());
				// editMyProfile.setToken(application.getToken());
				editMyProfile.setKey(PreferencesUtils.getLoginKey());
				editMyProfile.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()
						+ Constant.TokenSalt));
				editMyProfile.setName(myName.getText().toString());
				editMyProfile.setMobile(myPhone.getText().toString());
				editMyProfile.setEmail(myEmail.getText().toString());
				editMyProfile.setCarNumber(myCarNum.getText().toString());
				NetUtil.post(Constant.BASE_URL, editMyProfile, handler,
						HttpDefine.EDIT_MY_PROFILE_RESP);
			} else {
				ToastUtil.show(this,
						getResources().getString(R.string.network_fail));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}

package com.e1858.wuye.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.common.app.AppManager;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.Encrypt;
import com.e1858.utils.ImageUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.AddPicAdapter;
import com.e1858.wuye.adapter.SysAreaAdapter;
import com.e1858.wuye.protocol.http.AddBbsTopic;
import com.e1858.wuye.protocol.http.AddBbsTopicResp;
import com.e1858.wuye.protocol.http.BbsBoard;
import com.e1858.wuye.protocol.http.GetBbsBoards;
import com.e1858.wuye.protocol.http.GetBbsBoardsResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.UploadJson;
import com.hg.android.widget.CityDBManager.AreaEntity;
import com.hg.android.widget.MyGridView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 新增话题
 * 
 * @author jiajia 2014年8月22日
 * 
 */
public class AddTopicActivity extends BaseActivity implements OnClickListener {
	@SuppressWarnings("unused")
	private static String TAG = "AddTopicActivity";
	private EditText titleEditText;
	private EditText contentEditText;
	private MyGridView gridview;
	public static LinkedList<Bitmap> pics = new LinkedList<Bitmap>();
	public static LinkedList<String> picPaths = new LinkedList<String>();
	public ArrayList<String> uploadReturnUrls = new ArrayList<String>();
	private int bbsBord=-1;
	private AddPicAdapter adapter = null;
	private String picFileFullName;
	private Dialog picDialog;
	private Button okBtn;
	private String borad_key;
	private List<BbsBoard> bbsBoards = new ArrayList<BbsBoard>();
	private Spinner boardSpinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_topic);
//		if (null == PreferencesUtils.getCommunity()) {
//			Util.saveStatus(application, getSp());
//		}
		borad_key = NeighborhoodActivity.class.getSimpleName() + "_"
				+ PreferencesUtils.getCommunity().getID();
		bbsBord = getIntent().getExtras().getInt(Constant.DETAIL_ID);
		initView();
		initSpinner(true);
	}

	private void initSpinner(boolean localCache) {
		if (localCache) {

			GetBbsBoardsResp resp = (GetBbsBoardsResp) DataFileUtils
					.readObject(borad_key);
			if (null != resp) {
				bbsBoards = resp.getBbsBoards();
				Message msg = handler.obtainMessage(
						HttpDefine.RESPONSE_SUCCESS, bbsBoards);
				msg.arg1 = 1;
				msg.sendToTarget();
			}

			if (application.isNetworkAvailable()) {
				openProgressDialog("加载中...");
				GetBbsBoards getBbsBoards = new GetBbsBoards();
//				getBbsBoards.setKey(application.getKey());
//				getBbsBoards.setToken(application.getToken());
				getBbsBoards.setKey(PreferencesUtils.getLoginKey());
				getBbsBoards.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				getBbsBoards.setCommunityID(PreferencesUtils.getCommunity().getID());
				NetUtil.post(Constant.BASE_URL, getBbsBoards, handler,
						HttpDefine.GET_BBS_BOARDS_RESP);
			} else {
				ToastUtil.show(this,
						getResources().getString(R.string.network_fail));
			}
		}

	}

	private void initView() {
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title
				.setText(getResources().getString(R.string.text_bar_add_topic));
		titleEditText = (EditText) findViewById(R.id.item_title);
		contentEditText = (EditText) findViewById(R.id.item_content);
		gridview = (MyGridView) findViewById(R.id.gridview);
		boardSpinner = (Spinner) findViewById(R.id.board_spinner);
		boardSpinner.setPrompt("请选择所属板块");
		
		okBtn = (Button) findViewById(R.id.ok_btn);
		pics.clear();
		picPaths.clear();
		uploadReturnUrls.clear();
		pics.add(ImageUtil.drawable2Bitmap(getResources().getDrawable(
				R.drawable.add_pic_background)));
		adapter = new AddPicAdapter(this, pics);
		gridview.setAdapter(adapter);
		okBtn.setOnClickListener(this);
		if (bbsBord != -1) {
			boardSpinner.setEnabled(false);
		}else{
			boardSpinner.setEnabled(true);
		}
		boardSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				bbsBord = Integer.parseInt(((AreaEntity) boardSpinner
						.getSelectedItem()).getCode());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == pics.size() - 1) {
					createSelectPicDialog();
				} else {
					// 图片查看
					Intent intent = new Intent(AddTopicActivity.this,
							ViewPagerActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt(Constant.PIC_REMARK,
							Constant.TOPIC_EDIT_PIC_RESULT_CODE);
					intent.putExtras(bundle);
					startActivityForResult(intent,
							Constant.TOPIC_EDIT_PIC_RESULT_CODE);
				}
			}

		});
	}

	private void createSelectPicDialog() {
		picDialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		picDialog.setContentView(R.layout.select_pic_dialog);
		// 设置样式
		Window window = picDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.5f;
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

	private void openCamera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File outDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
			picFileFullName = outFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent,
					Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {
			ToastUtil.show(this, "请确认已插入SD卡");
		}
	}

	private void openAblum() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent,
				Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE);
		// // 打开本地相册
		// Intent intent = new Intent();
		// intent.setType("image/*");
		// intent.setAction(Intent.ACTION_GET_CONTENT);
		// this.startActivityForResult(intent,
		// Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode != RESULT_OK) {
				return;
			}
			setImageView(picFileFullName);

			break;
		case Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE:
			if (data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					String realPath = getRealPathFromURI(uri);
					setImageView(realPath);
				} else {
					ToastUtil.show(this, "从相册获取图片失败");
				}
			}
			break;
		case Constant.TOPIC_EDIT_PIC_RESULT_CODE:
			adapter.notifyDataSetChanged();
			break;
		}
	}

	private void setImageView(String realPath) {
		// Bitmap bmp = BitmapFactory.decodeFile(realPath);
		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(realPath, opts);

		opts.inSampleSize = ImageUtil.computeSampleSize(opts, -1, 128 * 128);
		opts.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(realPath, opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}

		Log.i("realPath", "==" + realPath);
		picPaths.addFirst(realPath);
		int degree = readPictureDegree(realPath);
		// ImageView imageView = new ImageView(this);
		if (degree <= 0) {
			// imageView.setImageBitmap(bmp);
			pics.addFirst(bmp);
		} else {
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,
					bmp.getWidth(), bmp.getHeight(), matrix, true);
			// imageView.setImageBitmap(resizedBitmap);
			pics.addFirst(resizedBitmap);
		}
		adapter.notifyDataSetChanged();

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

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			closeProgressDialog();
			return true;
		}
		switch (msg.what) {
		case Constant.FAIL_CODE:
			closeProgressDialog();
			if (msg.obj != null) {
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case HttpDefine.RESPONSE_SUCCESS:
			if (msg.arg1 == 1) {
				initBoardSpinner(bbsBoards);
			}
			break;
		case HttpDefine.ADD_BBS_TOPIC_RESP:
			closeProgressDialog();
			if (null != msg.obj) {
				AddBbsTopicResp resp = JsonUtil.fromJson((String) msg.obj,
						AddBbsTopicResp.class);
				if (null == resp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					// 成功处理
					ToastUtil.show(this, "新增话题成功!");
					bar_leftBtn.setEnabled(false);
					okBtn.setEnabled(false);
					application.setIsRefresh(true);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							application.setIsRefresh(true);
							AppManager.getAppManager().finishActivity();
						}
					}, 1000);
				} else {
					okBtn.setEnabled(true);
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.GET_BBS_BOARDS_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj) {
				GetBbsBoardsResp resp = JsonUtil.fromJson((String) msg.obj,
						GetBbsBoardsResp.class);
				if (resp == null) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					bbsBoards.clear();
					bbsBoards.addAll(resp.getBbsBoards());
					DataFileUtils.saveObject(resp, borad_key);
					initBoardSpinner(bbsBoards);
				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case Constant.UPLOAD_RESULT_CODE:
			if (null != (String) msg.obj) {
				UploadJson uploadJson = JsonUtil.fromJson((String) msg.obj,
						UploadJson.class);
				if (null == uploadJson) {
					break;
				} else {
					uploadReturnUrls.add(uploadJson.getUrl());
					if (uploadReturnUrls.size() == picPaths.size()) {
						Message over_msg = handler.obtainMessage(
								Constant.UPLOAD_OVER, "上传结束");
						handler.sendMessage(over_msg);
					}
				}
			}
			msg.obj = null;
			break;
		case Constant.UPLOAD_FAIL_CODE:
			if (null != msg.obj) {
				okBtn.setEnabled(true);
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case Constant.UPLOAD_OVER:
			if (null != msg.obj) {
				// 发包
				AddBbsTopic addBbsTopic = new AddBbsTopic();
				addBbsTopic.setCommunityID(PreferencesUtils.getCommunity().getID());
//				addBbsTopic.setKey(application.getKey());
//				addBbsTopic.setToken(application.getToken());
				addBbsTopic.setKey(PreferencesUtils.getLoginKey());
				addBbsTopic.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				addBbsTopic.setBoardID(bbsBord);
				addBbsTopic.setTitle(titleEditText.getText().toString());
				addBbsTopic.setContent(contentEditText.getText().toString());
				// 图片
				addBbsTopic.setImages(uploadReturnUrls);
				NetUtil.post(Constant.BASE_URL, addBbsTopic, handler,
						HttpDefine.ADD_BBS_TOPIC_RESP);
			}
			break;
		}

		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ok_btn:
			if (bbsBord == -1) {
				ToastUtil.show(this, "请选择板块");
			} else if (StringUtils.isBlank(titleEditText.getText().toString())) {
				ToastUtil.show(this, "请输入话题标题");
			} else if (StringUtils
					.isBlank(contentEditText.getText().toString())) {
				ToastUtil.show(this, "请输入话题内容");
			} else {
				okBtn.setEnabled(false);
				openProgressDialog("提交中...");
				if (null != picPaths && picPaths.size() > 0) {
					for (int i = 0; i < picPaths.size(); i++) {
						NetUtil.upload(Constant.UPLOAD_URL, null,
								picPaths.get(i), handler,
								Constant.UPLOAD_RESULT_CODE);
					}
				} else {
					Message over_msg = handler.obtainMessage(
							Constant.UPLOAD_OVER, "上传结束");
					handler.sendMessage(over_msg);
				}
			}
			break;
		}
	}

	private void initBoardSpinner(List<BbsBoard> bbsBoards) {
		List<AreaEntity> list = new ArrayList<AreaEntity>();
		int selection = 0;
		if (bbsBoards != null && bbsBoards.size() > 0) {
			for (int i = 0; i < bbsBoards.size(); i++) {
				if (bbsBord != -1 && bbsBord == bbsBoards.get(i).getID()) {
					selection = i;
				}
				AreaEntity sysArea = new AreaEntity();
				sysArea.setCode(bbsBoards.get(i).getID() + "");
				sysArea.setName(bbsBoards.get(i).getName());
				list.add(sysArea);
			}
		} else {
			AreaEntity sysArea = new AreaEntity();
			sysArea.setCode("-2");
			sysArea.setName("暂无板块");
			list.add(sysArea);
		}
		boardSpinner.setAdapter(new SysAreaAdapter(this, list));
		boardSpinner.setSelection(selection);
	}
}

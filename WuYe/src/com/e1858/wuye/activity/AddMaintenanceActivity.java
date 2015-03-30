package com.e1858.wuye.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.e1858.common.AddSuccessDialogManager;
import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DateUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.ImageUtil;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.widget.ServiceTimeDialog;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.AddPicAdapter;
import com.e1858.wuye.adapter.SysAreaAdapter;
import com.e1858.wuye.protocol.http.AddMaintenance;
import com.e1858.wuye.protocol.http.AddMaintenanceResp;
import com.e1858.wuye.protocol.http.GetMaintenanceTypes;
import com.e1858.wuye.protocol.http.GetMaintenanceTypesResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.MaintenanceType;
import com.e1858.wuye.protocol.http.UploadJson;
import com.hg.android.widget.CityDBManager.AreaEntity;
import com.hg.android.widget.MyGridView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * 新增报修
 * @author jiajia 2014年8月22日
 *
 */
@SuppressLint("SimpleDateFormat")
public class AddMaintenanceActivity extends BaseActivity implements OnClickListener
{
	private static final String TAG = "AddMaintenanceActivity";
	private TextView house_info;
	private EditText phone_info;
	private Spinner typeSpinner;
	private EditText input_cotent;
	private TextView serviceTime;
	private MyGridView gridView;
	private Button okBtn;
	private Dialog picDialog = null;
	private static String picFileFullName;
	public static LinkedList<Bitmap> pics = new LinkedList<Bitmap>();
	public static LinkedList<String> picPaths = new LinkedList<String>();
	public ArrayList<String> uploadReturnUrls = new ArrayList<String>();
//	private int year;
//	private int month;
//	private int day;
//	private int hour;
//	private int minute;
	private int typeID=-1;
	private AddPicAdapter adapter = null;
//	private Dialog timeDialog;
//	private Calendar calendar = Calendar.getInstance(Locale.CHINA);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_maintenance);
//		if(null==PreferencesUtils.getCommunity()){
//			Util.saveStatus(getSp());
//		}
		initView();
	}
	private void initView()
	{
//		Date myDate = new Date(System.currentTimeMillis());
//		
//		calendar.setTime(myDate);
//		year = calendar.get(Calendar.YEAR);
//		month = calendar.get(Calendar.MONTH);
//		day = calendar.get(Calendar.DAY_OF_MONTH);
//		hour = calendar.get(Calendar.HOUR_OF_DAY);
//		minute = calendar.get(Calendar.MINUTE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title.setText(getResources().getString(R.string.text_bar_add_maintenance));
		bar_leftBtn.setVisibility(View.VISIBLE);
		house_info = (TextView) findViewById(R.id.house_info);
		phone_info = (EditText) findViewById(R.id.input_phone);
		phone_info.setText(PreferencesUtils.getUserName());
		
		typeSpinner = (Spinner) findViewById(R.id.maintenance_type_spinner);
		input_cotent = (EditText) findViewById(R.id.input_maintenance);
		serviceTime = (TextView) findViewById(R.id.service_time);
		gridView = (MyGridView) findViewById(R.id.gridview);
		okBtn = (Button) findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(this);
		// updateTime(year, month, day,hour,minute);
		initTypeSpinner();
		pics.clear();
		picPaths.clear();
		uploadReturnUrls.clear();
		pics.add(ImageUtil.drawable2Bitmap(getResources().getDrawable(R.drawable.add_pic_background)));
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new AddPicAdapter(this, pics);
		gridView.setAdapter(adapter);
		if (null != PreferencesUtils.getUserHouse())
		{
			house_info.setText(Util.getHouseInfo(true));
		}

		house_info.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SwitchResidentAddressActivity.class);
				startActivityForResult(intent, Constant.HOUSE_INFO_RESULT_CODE);
			}
		});
		serviceTime.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				createDateandTimeDialog();

				// new DatePickerDialog(AddMaintenanceActivity.this,
				// mDateSetListener, year, month, day).show();
			}
		});
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				typeID =Integer.parseInt(((AreaEntity) typeSpinner.getSelectedItem()).getCode());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub

			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				if (position == pics.size() - 1)
				{
					createSelectPicDialog();
				}
				else
				{
					// 图片查看
					Intent intent = new Intent(AddMaintenanceActivity.this, ViewPagerActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt(Constant.PIC_REMARK, Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE);
					intent.putExtras(bundle);
					startActivityForResult(intent, Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE);
				}
			}

		});
	}

	private void initTypeSpinner()
	{
		// 请求报修类型
		typeSpinner.setPrompt("选择报修类型");
		openProgressDialog("请稍候...");
		GetMaintenanceTypes types = new GetMaintenanceTypes();
		types.setKey(PreferencesUtils.getLoginKey());
		types.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
		types.setCommunityID(PreferencesUtils.getCommunity().getID());
		NetUtil.post(Constant.BASE_URL, types, handler, HttpDefine.GET_MAINTENANCE_TYPES_RESP);
	}

	private void updateTime(String time)
	{
		String beginTime = time.substring(0, time.length() - 6);
		try{
			if(DateUtil.DateCompare(beginTime)){
				serviceTime.setText(time);
			}else{
				ToastUtil.show(this, "设置的上门服务时间不能小于当前时间!");
			}
			Log.i(TAG, "serviceTime="+serviceTime.getText().toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void createDateandTimeDialog()
	{
		 new ServiceTimeDialog(this, new ServiceTimeDialog.OnFinishedListener() {
			
			@Override
			public void onFinished(String fmtText) {
				updateTime(fmtText);
			}
		}).show();
	}

	/*
	 * private DatePickerDialog.OnDateSetListener mDateSetListener = new
	 * OnDateSetListener() {
	 * 
	 * @Override public void onDateSet(DatePicker view, int yearofyear, int
	 * monthOfYear, int dayOfMonth) { // TODO Auto-generated method stub year =
	 * yearofyear; month = monthOfYear; day = dayOfMonth; updateTime(year,
	 * month, day); } };
	 */

	private void createSelectPicDialog()
	{
		picDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		picDialog.setContentView(R.layout.select_pic_dialog);
		// 设置样式
		Window window = picDialog.getWindow();
		WindowManager.LayoutParams lp=window.getAttributes(); 
		lp.dimAmount=0.8f;  
		window.setAttributes(lp); 
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.pic_dialog_style);
		Button cameraBtn = (Button) picDialog.findViewById(R.id.camera_btn);
		Button pic_mapBtn = (Button) picDialog.findViewById(R.id.pic_map_btn);
		Button cancleBtn = (Button) picDialog.findViewById(R.id.cancel_btn);
		cancleBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				picDialog.dismiss();
			}
		});
		cameraBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				openCamera();
				picDialog.dismiss();
			}
		});
		pic_mapBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				openAblum();
				picDialog.dismiss();
			}
		});
		picDialog.show();
	}

	private void openCamera()
	{
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED))
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists())
			{
				outDir.mkdirs();
			}
			File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
			picFileFullName = outFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		}
		else
		{
			ToastUtil.show(this, "请确认已插入SD卡");
		}
	}

	private void openAblum()
	{
		 Intent intent = new Intent(Intent.ACTION_PICK, null);
		 intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		 "image/*");
		 startActivityForResult(intent,
		 Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE);
//		// 打开本地相册
//		Intent intent = new Intent();
//		intent.setType("image/*");
//		intent.setAction(Intent.ACTION_GET_CONTENT);
//		this.startActivityForResult(intent, Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode != RESULT_OK)
			{
				return;
			}
			setImageView(picFileFullName);
			break;
		case Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE:
			if(data!=null){
				Uri uri = data.getData();
				if (uri != null)
				{
					String realPath = getRealPathFromURI(uri);
					setImageView(realPath);
				}
				else
				{
					ToastUtil.show(this, "从相册获取图片失败");
				}
			}
			break;
		case Constant.HOUSE_INFO_RESULT_CODE:
			house_info.setText(Util.getHouseInfo(true));
			break;
		case Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE:
			adapter.notifyDataSetChanged();
			break;
		}
	}

	private void setImageView(String realPath)
	{
//		Bitmap bmp = BitmapFactory.decodeFile(realPath);
		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(realPath, opts);

		opts.inSampleSize = ImageUtil.computeSampleSize(opts, -1, 128 * 128);
		opts.inJustDecodeBounds = false;
		try
		{
			bmp = BitmapFactory.decodeFile(realPath, opts);
		}
		catch (OutOfMemoryError err)
		{
			err.printStackTrace();
		}
		Log.i("realPath", "==" + realPath);
		picPaths.addFirst(realPath);
		int degree = readPictureDegree(realPath);
		// ImageView imageView = new ImageView(this);
		if (degree <= 0)
		{
			// imageView.setImageBitmap(bmp);
			pics.addFirst(bmp);
		}
		else
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			// imageView.setImageBitmap(resizedBitmap);
			pics.addFirst(resizedBitmap);
		}
		adapter.notifyDataSetChanged();

	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri)
	{
		try
		{
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		catch (Exception e)
		{
			return contentUri.getPath();
		}
	}

	public static int readPictureDegree(String path)
	{
		int degree = 0;
		try
		{
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation)
			{
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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return degree;
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		if (null == msg.obj)
		{
			closeProgressDialog();
			return true;
		}
		switch (msg.what)
		{
		case Constant.FAIL_CODE:
			closeProgressDialog();
			if (msg.obj != null) {
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case HttpDefine.GET_MAINTENANCE_TYPES_RESP:
			closeProgressDialog();
			if (null != msg.obj)
			{
				GetMaintenanceTypesResp resp = JsonUtil.fromJson((String) msg.obj, GetMaintenanceTypesResp.class);
				if (null == resp)
				{
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet())
				{

					List<AreaEntity> sysAreas = new ArrayList<AreaEntity>();
					List<MaintenanceType> list = resp.getTypes();
					if (null==resp.getTypes()||resp.getTypes().size()==0)
					{
						AreaEntity sysArea = new AreaEntity();
						sysArea.setCode("-1");
						sysArea.setName("暂无类型");
						sysAreas.add(sysArea);
					}
					else
					{

						for (MaintenanceType type : list)
						{
							AreaEntity sysArea = new AreaEntity();
							sysArea.setCode(type.getID() + "");
							sysArea.setName(type.getName());
							sysAreas.add(sysArea);
						}
						
					}
					typeSpinner.setAdapter(new SysAreaAdapter(this, sysAreas));
				}
				else
				{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.ADD_MAINTENANCE_RESP:
			closeProgressDialog();
			if (null != msg.obj)
			{
				AddMaintenanceResp resp = JsonUtil.fromJson((String) msg.obj, AddMaintenanceResp.class);
				if (null == resp)
				{
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet())
				{
					// 成功处理
//					ToastUtil.show(this, "新增报修成功!");
					bar_leftBtn.setEnabled(false);
					//延迟一秒跳转
//					new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							AppManager.getAppManager().finishActivity();
//						}
//					}, 1000);
					application.setIsRefresh(true);
					AddSuccessDialogManager.createCallDialog(AddMaintenanceActivity.this, Constant.ADDSUCCESS_MODULE.MAINTENANCE,resp.getID(),null);
				}
				else
				{
					okBtn.setEnabled(true);
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case Constant.UPLOAD_RESULT_CODE:
			if(null!=(String)msg.obj){
				UploadJson uploadJson = JsonUtil.fromJson((String)msg.obj, UploadJson.class);
				if(null==uploadJson){
					break;
				}else{
					uploadReturnUrls.add(uploadJson.getUrl());
					if(uploadReturnUrls.size()==picPaths.size()){
						Message over_msg = handler.obtainMessage(Constant.UPLOAD_OVER, "上传结束");
						handler.sendMessage(over_msg);
					}
				}
			}
			msg.obj=null;
			break;
		case Constant.UPLOAD_FAIL_CODE:
			if(null!=msg.obj){
				okBtn.setEnabled(true);
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case Constant.UPLOAD_OVER:
			if(null!=msg.obj){
				// 发包
				okBtn.setEnabled(false);
				AddMaintenance addMaintenance = new AddMaintenance();
				addMaintenance.setCommunityID(PreferencesUtils.getCommunity().getID());
//				addMaintenance.setKey(application.getKey());
//				addMaintenance.setToken(application.getToken());
				addMaintenance.setKey(PreferencesUtils.getLoginKey());
				addMaintenance.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				addMaintenance.setTypeID(typeID);
				addMaintenance.setPhone(phone_info.getText().toString());
				addMaintenance.setContent(input_cotent.getText().toString());
				addMaintenance.setServiceTime(serviceTime.getText().toString());
				// 图片
				addMaintenance.setImages(uploadReturnUrls);
				NetUtil.post(Constant.BASE_URL, addMaintenance, handler, HttpDefine.ADD_MAINTENANCE_RESP);
			}
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.ok_btn:
			if (StringUtils.isBlank(house_info.getText().toString()))
			{
				ToastUtil.show(this, "请选择房屋信息");
			}
			else if (typeID == -1)
			{
				ToastUtil.show(this, "请选择报修类型");
			}
			if (StringUtils.isBlank(phone_info.getText().toString()))
			{
				ToastUtil.show(this, "请填写联系方式");
			}else if(!Pattern.matches(Constant.MOBILE_REGP, phone_info.getText().toString())){
				ToastUtil.show(this, Constant.ToastMessage.MOBILE_REGP_ERROR);
			}
			else if (StringUtils.isBlank(input_cotent.getText().toString()))
			{
				ToastUtil.show(this, "请输入报修情况");
			}else if(StringUtils.isBlank(serviceTime.getText().toString())){
				ToastUtil.show(this, "请设置上门服务时间");
			}
			else
			{
				openProgressDialog("提交中...");
				if (null != picPaths && picPaths.size() > 0)
				{
					okBtn.setEnabled(false);
					for (int i = 0; i < picPaths.size(); i++)
					{
						Log.i("TAG","====="+picPaths.get(i));
						NetUtil.upload(Constant.UPLOAD_URL, null, picPaths.get(i),handler,Constant.UPLOAD_RESULT_CODE);
					}
				
				}else{
					Message over_msg = handler.obtainMessage(Constant.UPLOAD_OVER, "上传结束");
					handler.sendMessage(over_msg);
				}
				
			}
			break;
		}

	}
}

package com.e1858.mall.recommend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.common.Constant;
import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.CropImageActivity;
import com.e1858.common.widget.HorizontalListView;
import com.e1858.mall.MallConstant;
import com.e1858.mall.protocol.bean.MallRCProduct;
import com.e1858.mall.protocol.packet.MallAddRecommendRequest;
import com.e1858.mall.protocol.packet.MallAddRecommendResp;
import com.e1858.mall.protocol.packet.MallGetProductAlreadyRequest;
import com.e1858.mall.protocol.packet.MallGetProductAlreadyResp;
import com.e1858.utils.FileUtils;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.MLog;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.mall.R;
import com.e1858.wuye.protocol.http.UploadJson;
import com.hg.android.utils.HGUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MallAddNewRecommActivity extends BaseActionBarActivity {
	private Context					context;
	private HorizontalListView		mall_add_rec_picture_hl, mall_add_rec_buyed_hl;
	private ImageButton				mall_add_recomm_photo_ib;
	private Dialog					picDialog			= null;
	private TextView				mall_add_new_comm_more_tv;

	private List<String>			iconUrls			= new ArrayList<String>();
	private List<MallRCProduct>		mallRCProducts		= new ArrayList<MallRCProduct>();
	private static final int		ADD_FROM_PICTURE	= 1;													//用户拍照推荐
	private static final int		ADD_FROM_BUYED		= 2;													//用户从已购商品中推荐图片
	private static int				add_from_flag		= ADD_FROM_PICTURE;
	private ImageView				mall_add_recomm_iv_pic;
	private EditText				mall_add_recomm_et_dec;
	protected DisplayImageOptions	options, options1;
	private String					pic_url				= null;
	private MyAdapter				hAdapter;
	private MallRCProduct			mallRCProduct;
	private static final int		BUYED_REQUESTCODE	= 101;
	private static final int		PHOTO_WITH_DATA		= 3011;
	private static final String		PHOTO_FILE_NAME		= "temp_photo.jpg";
	public static String			recomm_pic_dir		= FileUtils.getSDPath() + File.separator + "e1858"
																+ File.separator + "recommed" + File.separator;
//	public static String			recomm_pic_dir		= "/sdcard/e1858/recommend/";
	UploadJson						uploadJson			= null;
	private String					dec;
	public int						click_posi			= 0;
	private TextView				empty;
	private ImageView				empty_iv;
	private MyBuyedAdapter			bAdapter;
	private File					tempFile;
	private File					temp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_add_new_recomm_activity);
		context = getActivity();
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		initViews();
		temp = new File(recomm_pic_dir);
		if (!temp.exists()) {
			temp.mkdirs();
		}
	}

	private void initViews() {
		mall_add_rec_picture_hl = (HorizontalListView) this.findViewById(R.id.mall_add_rec_picture_hl);
		mall_add_rec_buyed_hl = (HorizontalListView) this.findViewById(R.id.mall_add_rec_buyed_hl);
		mall_add_recomm_photo_ib = (ImageButton) this.findViewById(R.id.mall_add_recomm_photo_ib);
		mall_add_recomm_iv_pic = (ImageView) this.findViewById(R.id.mall_add_recomm_iv_pic);
		empty_iv = (ImageView) this.findViewById(R.id.empty_iv);
		mall_add_recomm_et_dec = (EditText) this.findViewById(R.id.mall_add_recomm_et_dec);
		empty = (TextView) this.findViewById(R.id.empty);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.mall_add_recomm_bg)
				.showImageForEmptyUri(R.drawable.mall_add_recomm_bg).showImageOnFail(R.drawable.mall_add_recomm_bg)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		options1 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.mall_recommend_def)
				.showImageForEmptyUri(R.drawable.mall_recommend_def).showImageOnFail(R.drawable.mall_recommend_def)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		hAdapter = new MyAdapter();
		mall_add_rec_picture_hl.setAdapter(hAdapter);
		mall_add_recomm_photo_ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//弹出拍照的对话框
				createSelectPicDialog();

			}
		});
		mall_add_new_comm_more_tv = (TextView) this.findViewById(R.id.mall_add_new_comm_more_tv);
		mall_add_new_comm_more_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MallBuyedActivity.class);
				startActivityForResult(intent, BUYED_REQUESTCODE);
			}
		});
		loadBuyedProduct(0);

		mall_add_rec_picture_hl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				click_posi = position;
				pic_url = (String) hAdapter.getItem(position);
				ImageLoader.getInstance().displayImage(pic_url, mall_add_recomm_iv_pic, options);
//				hAdapter.notifyDataSetChanged();
			}
		});
		if (HGUtils.isListEmpty(iconUrls)) {
			mall_add_rec_picture_hl.setVisibility(View.GONE);
			empty_iv.setVisibility(View.VISIBLE);
		}
		bAdapter = new MyBuyedAdapter();
		mall_add_rec_buyed_hl.setAdapter(bAdapter);
		if (HGUtils.isListEmpty(mallRCProducts)) {
			mall_add_rec_buyed_hl.setVisibility(View.GONE);
			empty.setVisibility(View.VISIBLE);
		}
		mall_add_rec_buyed_hl.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				iconUrls.clear();
				add_from_flag = ADD_FROM_BUYED;
				mallRCProduct = mallRCProducts.get(position);
				if (HGUtils.isListEmpty(mallRCProduct.getImageUrls())) {
					if (mallRCProduct.getThumbUrl() == null) {
						ToastUtil.show(context, "该商品没有图片信息,请选择一张图片！");
					} else {
						iconUrls.clear();
						pic_url = null;
						iconUrls.add(mallRCProduct.getThumbUrl());
					}
				} else {
					iconUrls.clear();
					pic_url = null;
					iconUrls.addAll(mallRCProduct.getImageUrls());
				}
				if (!HGUtils.isListEmpty(iconUrls)) {
					mall_add_rec_picture_hl.setVisibility(View.VISIBLE);
					empty_iv.setVisibility(View.GONE);
					if (TextUtils.isEmpty(pic_url)) {
						pic_url = iconUrls.get(0);
						ImageLoader.getInstance().displayImage(pic_url, mall_add_recomm_iv_pic, options);
					}
				}
				hAdapter.notifyDataSetChanged();
			}
		});

		mall_add_recomm_et_dec.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 50) {
					ToastUtil.show(context, "你还能输入" + (60 - s.length()) + "个字符!");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 50) {
					ToastUtil.show(context, "你还能输入" + (60 - s.length()) + "个字符!");
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case BUYED_REQUESTCODE://打开已购商品activity返回结果
				add_from_flag = ADD_FROM_BUYED;
				mallRCProduct = null;
				mallRCProduct = (MallRCProduct) data.getSerializableExtra(MallBuyedActivity.SELECT_BUYED_PRO);
				iconUrls.clear();
				iconUrls.addAll(mallRCProduct.getImageUrls());
				if (!HGUtils.isListEmpty(iconUrls)) {
					mall_add_rec_picture_hl.setVisibility(View.VISIBLE);
					empty_iv.setVisibility(View.GONE);
					pic_url = iconUrls.get(0);
					ImageLoader.getInstance().displayImage(pic_url, mall_add_recomm_iv_pic, options);
				} else {
					if (mallRCProduct.getThumbUrl() != null) {
						pic_url = mallRCProduct.getThumbUrl();
						iconUrls.add(pic_url);
						ImageLoader.getInstance().displayImage(pic_url, mall_add_recomm_iv_pic, options);
					} else {
						ToastUtil.show(context, "该商品没有图片信息,请选择一张图片");
					}
				}
				hAdapter.notifyDataSetChanged();
				break;
			case Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE://打开图库
				if (data != null) {
					// 得到图片的全路径
					Uri uri = data.getData();
					startPhotoZoomFromUri(uri);
//					doCropPhoto(uri);
				}
				break;
			case Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE://打开照相机
				if (hasSdcard()) {
					File tempFile = new File(temp, PHOTO_FILE_NAME);
					startPhotoZoomFromUri(Uri.fromFile(tempFile));
//					doCropPhoto(Uri.fromFile(tempFile));
				} else {
					ToastUtil.show(MallAddNewRecommActivity.this, "未找到存储卡，无法存储照片！");
				}
				break;
			case PHOTO_WITH_DATA:
				if (data != null) {
					String path = tempFile.getAbsolutePath();
					setPicToView(path);
				}
			default:
				break;
			}
		} else if (resultCode == CropImageActivity.PICTURE_CORP_RESULT) {
			if (data != null) {
				String path = data.getStringExtra("cropImagePath");
				setPicToView(path);
			}
		}
	}

	private void setPicToView(String path) {
		MLog.e("MallAddNewRecommActivity", path);
		path = "file://" + path;
		iconUrls.add(0, path);
		if (!HGUtils.isListEmpty(iconUrls)) {
			mall_add_rec_picture_hl.setVisibility(View.VISIBLE);
			empty_iv.setVisibility(View.GONE);
		}
		ImageLoader.getInstance().displayImage(iconUrls.get(0), mall_add_recomm_iv_pic, options);
		hAdapter.notifyDataSetChanged();
	}

	public Intent getCropImageIntent(String uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");

//		intent.setType("image/*");
		intent.setDataAndType(Uri.parse(uri), "image/*");
//		intent.putExtra("data", uri);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 1024);
		intent.putExtra("outputY", 1024);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		tempFile = new File(recomm_pic_dir + System.currentTimeMillis() + ".jpg");
		intent.putExtra("output", Uri.fromFile(tempFile)); // 专入目标文件     
		intent.putExtra("outputFormat", "JPEG"); //输入文件格式    
		Intent wrapperIntent = Intent.createChooser(intent, "先择图片"); //开始 并设置标题 

		return wrapperIntent;
	}

	protected void doCropPhoto(Uri uri) {
		Intent intent = getCropImageIntent(uri.toString());
		try {
			startActivityForResult(intent, PHOTO_WITH_DATA);
		}
		catch (Exception e) {
			Toast.makeText(this, R.string.setting_no_app, Toast.LENGTH_SHORT).show();
		}
	}

	public void startPhotoZoomFromUri(Uri uri) {
		try {
			Intent intent = new Intent(MallAddNewRecommActivity.this, CropImageActivity.class);
			intent.putExtra("bitmap", uri);
			intent.putExtra("bitmap_name", System.currentTimeMillis() + ".jpg");
			intent.putExtra("cropWidth", 624);
			intent.putExtra("cropHeight", 530);
			MallAddNewRecommActivity.this.startActivityForResult(intent, CropImageActivity.PICTURE_CORP_RESULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void loadBuyedProduct(final int offset) {
		ResponseHandler<MallGetProductAlreadyResp> responseHandler = new ResponseHandler<MallGetProductAlreadyResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetProductAlreadyResp response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					//加载完成以后
					mallRCProducts.addAll(response.getMallRCProducts());
					if (!HGUtils.isListEmpty(mallRCProducts)) {
						mall_add_rec_buyed_hl.setVisibility(View.VISIBLE);
						empty.setVisibility(View.GONE);
					}
				}
			}
		};

		MallGetProductAlreadyRequest request = new MallGetProductAlreadyRequest();
		request.setOffset(offset);
		request.setCount(MallConstant.FetchCount_buyed);
		HttpPacketClient.postPacketAsynchronous(request, MallGetProductAlreadyResp.class, responseHandler, true);
	}

	@Override
	public void onDestroy() {
		FileUtils.delete(new File(FileUtils.getSDPath() + File.separator + "e1858" + File.separator + "recommed"));
		if (!HGUtils.isListEmpty(iconUrls)) {
			iconUrls.clear();
		}
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_save) {
			//提交新增推荐到服务器
			addNewRecomm();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mall_save, menu);
		return true;
	}

	/**
	 * 新增推荐接口
	 */
	private void addNewRecomm() {
		final ResponseHandler<MallAddRecommendResp> responseHandler = new ResponseHandler<MallAddRecommendResp>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍候...");
			}

			@Override
			public void onFinish(MallAddRecommendResp response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {}

				if (response != null && response.isSuccess()) {
					finish();
					//删除本地建立的缓存推荐图片
					if (!HGUtils.isListEmpty(iconUrls)) {
						iconUrls.clear();
					}
					ToastUtil.show(context, "推荐成功！");
					FileUtils.delete(new File(recomm_pic_dir));

				}
			}
		};
		dec = mall_add_recomm_et_dec.getText().toString().trim();
		if (TextUtils.isEmpty(dec)) {
			ToastUtil.show(context, "请输入您的推荐介绍！");
			return;
		}
		final MallAddRecommendRequest request = new MallAddRecommendRequest();
		switch (add_from_flag) {
		case ADD_FROM_PICTURE://自定义推荐的话，没有商品ID，图片首先要上传到服务器，然后返回地址
			request.setProductID("");
			MLog.i("add_from_flag", "add_from_flag==ADD_FROM_PICTURE");
			if (!HGUtils.isListEmpty(iconUrls)) {
				if (TextUtils.isEmpty(pic_url))
					pic_url = iconUrls.get(0);
			}
			if (!StringUtils.isEmpty(pic_url)) {
				if (pic_url.startsWith("file://"))
					pic_url = pic_url.replace("file://", "");
				MLog.i("TAG", pic_url);
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected void onPreExecute() {
						openProgressDialog("请稍候...");
						super.onPreExecute();
					}

					@Override
					protected Void doInBackground(Void... params) {
						HttpPacketClient.sInstance.setFileServer(Constant.UPLOAD_URL);
						uploadJson = HttpPacketClient.syncUpdaloadFile(pic_url);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						String up_pic_url = uploadJson.getUrl();
						if ("".equals(up_pic_url)) {
							ToastUtil.show(context, "对不起，图片上传失败，请重新推荐！");
							closeProgressDialog();
							return;
						}
						MLog.e("MallAddNewR", "上传成功" + up_pic_url);
						request.setCommentPicture(up_pic_url);
						request.setRecDescription(dec);
						request.setProductID("");
						HttpPacketClient.postPacketAsynchronous(request, MallAddRecommendResp.class, responseHandler,
								true);

						super.onPostExecute(result);
					}

				}.execute();
			} else {
				ToastUtil.show(context, "请选择一张您要推荐的图片！");
				return;
			}

			break;
		case ADD_FROM_BUYED://从已购商品中推荐的话，从在商品ID，图片地址也存在
			if (mallRCProduct != null) {
				MLog.i("add_from_flag", "add_from_flag==ADD_FROM_BUYED");
				request.setProductID(mallRCProduct.getRecProductID() == null ? "" : mallRCProduct.getRecProductID());
				MLog.e("RCProduct", mallRCProduct.getRecProductID() + "");
				if (TextUtils.isEmpty(pic_url)) {
					ToastUtil.show(context, "该商品没有图片信息,请选择一张图片！");
					request.setProductID("");
					return;
				}

				if (!StringUtils.isEmpty(pic_url) && pic_url.startsWith("file:///mnt/sdcard/e1858/recommed/")) {
					if (pic_url.startsWith("file://"))
						pic_url = pic_url.replace("file://", "");
					MLog.i("TAG", pic_url);
					new AsyncTask<Void, Void, Void>() {

						@Override
						protected void onPreExecute() {
							openProgressDialog("请稍候...");
							super.onPreExecute();
						}

						@Override
						protected Void doInBackground(Void... params) {
							HttpPacketClient.sInstance.setFileServer(Constant.UPLOAD_URL);
							uploadJson = HttpPacketClient.syncUpdaloadFile(pic_url);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							String up_pic_url = uploadJson.getUrl();
							if ("".equals(up_pic_url)) {
								ToastUtil.show(context, "对不起，图片上传失败，请重新推荐！");
								closeProgressDialog();
								return;
							}
							MLog.e("MallAddNewR", "上传成功" + up_pic_url);
							request.setCommentPicture(up_pic_url);
							request.setRecDescription(dec);
							HttpPacketClient.postPacketAsynchronous(request, MallAddRecommendResp.class,
									responseHandler, true);
							super.onPostExecute(result);
						}
					}.execute();
				} else {
					request.setCommentPicture(pic_url);
					request.setRecDescription(dec);
					HttpPacketClient.postPacketAsynchronous(request, MallAddRecommendResp.class, responseHandler, true);
				}
			} else {
				add_from_flag = ADD_FROM_PICTURE;

				break;
			}

		default:
			break;
		}

	}

	@Override
	public boolean hasDestroyed() {
		return super.hasDestroyed();
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public BaseActionBarActivity getActivity() {
		return super.getActivity();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return HGUtils.isListEmpty(iconUrls) ? 0 : iconUrls.size();
		}

		@Override
		public String getItem(int position) {
			return HGUtils.isListEmpty(iconUrls) ? null : iconUrls.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.mall_add_rec_item, null);
				viewHolder = new ViewHolder();
				viewHolder.iv = (ImageView) convertView.findViewById(R.id.mall_add_rec_iv);
				viewHolder.mall_add_rec_iv_sele = (ImageView) convertView.findViewById(R.id.mall_add_rec_iv_sele);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
//			if (click_posi == position) {
//				viewHolder.mall_add_rec_iv_sele.setVisibility(View.VISIBLE);
//			} else {
//				viewHolder.mall_add_rec_iv_sele.setVisibility(View.INVISIBLE);
//			}
			ImageLoader.getInstance().displayImage(iconUrls.get(position), viewHolder.iv);
			return convertView;
		}
	}

	class ViewHolder {
		ImageView	iv;
		ImageView	mall_add_rec_iv_sele;
	}

	private void createSelectPicDialog() {
		picDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
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
				picDialog.dismiss();

			}
		});
		cameraBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openCamera();
				picDialog.dismiss();
//				if (add_from_flag == ADD_FROM_BUYED) {
//					iconUrls.clear();
//					pic_url = "";
//					ImageLoader.getInstance().displayImage("", mall_add_recomm_iv_pic, options);
//					hAdapter.notifyDataSetChanged();
//				}
//				add_from_flag = ADD_FROM_PICTURE;
			}
		});
		pic_mapBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openAblum();
				picDialog.dismiss();
//				if (add_from_flag == ADD_FROM_BUYED) {
//					iconUrls.clear();
//					pic_url = "";
//					ImageLoader.getInstance().displayImage("", mall_add_recomm_iv_pic, options);
//					hAdapter.notifyDataSetChanged();
//				}
//				add_from_flag = ADD_FROM_PICTURE;
			}
		});
		picDialog.show();
	}

	private void openAblum() {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent, Constant.PICK_IMAGE_ACTIVITY_REQUEST_CODE);
		}
		catch (Exception e) {
			e.printStackTrace();
			ToastUtil.show(this, "打开图库失败");
		}

	}

	private void openCamera() {
		try {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			// 判断存储卡是否可以用，可用进行存储
			if (hasSdcard()) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(temp, PHOTO_FILE_NAME)));
			}
			startActivityForResult(intent, Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		}
		catch (Exception e) {
			ToastUtil.show(this, "打开相机失败");
			e.printStackTrace();
		}
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author momo
	 *         已购商品栏目
	 */
	class MyBuyedAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return HGUtils.isListEmpty(mallRCProducts) ? 0 : mallRCProducts.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder1 viewHolder = null;
			if (null == convertView) {
				convertView = View.inflate(context, R.layout.mall_buyed_item, null);
				viewHolder = new ViewHolder1();
				viewHolder.mall_buyed_item_iv = (ImageView) convertView.findViewById(R.id.mall_buyed_item_iv);
				viewHolder.mall_buyed_item_tv = (TextView) convertView.findViewById(R.id.mall_buyed_item_tv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder1) convertView.getTag();
			}

			MallRCProduct mallRCProduct = mallRCProducts.get(position);
			ImageLoader.getInstance()
					.displayImage(mallRCProduct.getThumbUrl(), viewHolder.mall_buyed_item_iv, options1);
			viewHolder.mall_buyed_item_tv.setText(mallRCProduct.getProductName() == null ? "" : mallRCProduct
					.getProductName());

			return convertView;
		}

	}

	class ViewHolder1 {
		ImageView	mall_buyed_item_iv;
		TextView	mall_buyed_item_tv;
	}
}

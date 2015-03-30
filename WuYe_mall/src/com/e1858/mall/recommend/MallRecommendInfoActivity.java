package com.e1858.mall.recommend;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.common.app.BaseApplication;
import com.e1858.common.widget.CircularImage;
import com.e1858.mall.MallConstant;
import com.e1858.mall.protocol.bean.MallComment;
import com.e1858.mall.protocol.bean.MallRecommend;
import com.e1858.mall.protocol.packet.MallAddCommentRequest;
import com.e1858.mall.protocol.packet.MallAddCommentResp;
import com.e1858.mall.protocol.packet.MallAddHeartRequest;
import com.e1858.mall.protocol.packet.MallAddHeartResp;
import com.e1858.mall.protocol.packet.MallDeleteRecommendRequest;
import com.e1858.mall.protocol.packet.MallDeleteRecommendResp;
import com.e1858.mall.protocol.packet.MallGetCommentRequest;
import com.e1858.mall.protocol.packet.MallGetCommentResp;
import com.e1858.mall.utils.ToastUtil;
import com.e1858.utils.HttpPacketClient;
import com.e1858.utils.HttpPacketClient.ResponseHandler;
import com.e1858.utils.MLog;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ResponseUtils;
import com.e1858.utils.StringUtils;
import com.e1858.wuye.mall.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.utils.HGUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MallRecommendInfoActivity extends BaseActionBarActivity {
	PullToRefreshListView			listView;
	private Context					context;
	private ImageView				mall_reco_info_iv_bigicon;
	private TextView				mall_reco_info_tv_dec;
	private TextView				mall_recommend_pic_username;
	private TextView				mall_recommend_pic_user_time;
	private TextView				mall_recommend_pic_haert_num;
	private TextView				mall_recommend_pic_comm_num;
	private EditText				mall_reco_info_et_signin;
	private ImageButton				mall_reco_info_ib_send;
	private CircularImage			head_portrait;
	public static final String		RECOMMEND_INFO		= "recommend_info";
	public static final String		RECOMMEND_MYHEARTS	= "myhearts";

	private MallRecommend			mallRecommend;
	protected DisplayImageOptions	options, options1;

	private List<MallComment>		mallComment_list	= new ArrayList<MallComment>();

	private BaseApplication			application;
	private MyAdapter				adapter;
	List<String>					hearteds_l;
	private ImageView				mall_recommend_pic_haert_iv;
	private TextView				mall_recom_info_com_tv;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_pulltorefresh_reco_info_listview);
		context = getActivity();
		application = BaseApplication.getBaseInstance();
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		if (null != getIntent()) {
			mallRecommend = (MallRecommend) getIntent().getSerializableExtra(RECOMMEND_INFO);
			hearteds_l = (List<String>) getIntent().getSerializableExtra(RECOMMEND_MYHEARTS);
		}
		initViews();
	}

	public void initViews() {
		mall_reco_info_et_signin = (EditText) this.findViewById(R.id.mall_reco_info_et_signin);
		mall_reco_info_ib_send = (ImageButton) this.findViewById(R.id.mall_reco_info_ib_send);

		listView = (PullToRefreshListView) findViewById(R.id.listView);

		listView.setMode(Mode.PULL_FROM_END);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadComms(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.getRefreshableView().addHeaderView(initHeadView());
		adapter = new MyAdapter(context);
		listView.setAdapter(adapter);

		//初始化时加载评论
		loadComms(0);
	}

	public View initHeadView() {
		View Headview = View.inflate(context, R.layout.mall_recom_info_headview, null);
		mall_reco_info_iv_bigicon = (ImageView) Headview.findViewById(R.id.mall_reco_info_iv_bigicon);
		mall_recommend_pic_haert_iv = (ImageView) Headview.findViewById(R.id.mall_recommend_pic_haert_iv);
		head_portrait = (CircularImage) Headview.findViewById(R.id.head_portrait);
		mall_reco_info_tv_dec = (TextView) Headview.findViewById(R.id.mall_reco_info_tv_dec);
		mall_recommend_pic_username = (TextView) Headview.findViewById(R.id.mall_recommend_pic_username);
		mall_recommend_pic_user_time = (TextView) Headview.findViewById(R.id.mall_recommend_pic_user_time);
		mall_recommend_pic_haert_num = (TextView) Headview.findViewById(R.id.mall_recommend_pic_haert_num);
		mall_recommend_pic_comm_num = (TextView) Headview.findViewById(R.id.mall_recommend_pic_comm_num);
		mall_recom_info_com_tv = (TextView) Headview.findViewById(R.id.mall_recom_info_com_tv);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.head_portrait_icon)
				.showImageForEmptyUri(R.drawable.head_portrait_icon).showImageOnFail(R.drawable.head_portrait_icon)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(5)).build();
		options1 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.mall_add_recomm_bg)
				.showImageForEmptyUri(R.drawable.mall_add_recomm_bg).showImageOnFail(R.drawable.mall_add_recomm_bg)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		mall_recommend_pic_haert_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BaseApplication.getBaseInstance().verifyLoggedInAndGoToLogin(MallRecommendInfoActivity.this)) {
					if (!HGUtils.isListEmpty(hearteds_l) && hearteds_l.contains(mallRecommend.getProductID())) {//已经点过赞的
						ToastUtil.show(context, "你已经点过赞了，看看别的推荐吧！");
					} else {
						//添加点赞
						addHeart(mallRecommend.getRecommendID());
					}
				}
			}
		});
		if (null != mallRecommend) {
			ImageLoader.getInstance().displayImage(mallRecommend.getProductIcon(), mall_reco_info_iv_bigicon, options1);
			ImageLoader.getInstance().displayImage(mallRecommend.getRecommendUserIcon(), head_portrait, options);
			mall_reco_info_tv_dec.setText(mallRecommend.getRecDescription() == null ? "无描述信息" : mallRecommend
					.getRecDescription());
			String name = mallRecommend.getRecommendUserName().trim();
			mall_recommend_pic_username
					.setText(StringUtils.isMobileNO(name) ? StringUtils.replaceMobileNo(name) : name);
			mall_recommend_pic_user_time.setText(mallRecommend.getRecommendTime());
			mall_recommend_pic_haert_num.setText(mallRecommend.getHeartCount() + "");
			mall_recommend_pic_comm_num.setText(mallRecommend.getCommentCount() + "");
			if (!HGUtils.isListEmpty(hearteds_l) && hearteds_l.contains(mallRecommend.getRecommendID())) {
				mall_recommend_pic_haert_iv.setBackgroundResource(R.drawable.mall_recommend_info_heart_select);
			} else {
				mall_recommend_pic_haert_iv.setBackgroundResource(R.drawable.mall_recommend_info_heart);
			}
		}

		return Headview;

	}

	/**
	 * 加载评论信息
	 * 
	 * @param offset
	 */
	protected void loadComms(final int offset) {

		ResponseHandler<MallGetCommentResp> responseHandler = new ResponseHandler<MallGetCommentResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallGetCommentResp response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					//加载完成以后
					mallComment_list.addAll(response.getMallComments());
					mall_recommend_pic_comm_num.setText(mallComment_list.size() + "");
					mall_recom_info_com_tv.setText(String.format("评论（共有%d条评论）", mallComment_list.size()));
					adapter.notifyDataSetChanged();
				}
			}
		};

		MallGetCommentRequest request = new MallGetCommentRequest();
		request.setOffset(offset);
		request.setCount(MallConstant.FetchCount);
		request.setRecommendID(mallRecommend.getID());
		HttpPacketClient.postPacketAsynchronous(request, MallGetCommentResp.class, responseHandler, true);
	}

	/**
	 * send按钮的点击事件
	 * 
	 * @param v
	 */
	public void addNewComm(View v) {
		// 判断登陆状态
		application.verifyLoggedInAndGoToLogin(MallRecommendInfoActivity.this);
		if (false) {//判断是否限制评论
			ToastUtil.show(context, "不好意思，该商品已禁止评论！");
		}
		String dec = mall_reco_info_et_signin.getText().toString().trim();
		if (TextUtils.isEmpty(dec)) {
			ToastUtil.show(context, "请输入评论内容！");
			return;
		}
		final MallComment mallComment = new MallComment();
		mallComment.setCommentDescription(dec);
		mallComment.setCommentName(PreferencesUtils.getUserInfo().getNickname());
		mallComment.setRecommendID(mallRecommend.getRecommendID());
		mallComment.setCommentIcon("");
		mallComment.setCommentID("");
		mallComment.setCommentTime("");

		ResponseHandler<MallAddCommentResp> responseHandler = new ResponseHandler<MallAddCommentResp>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍候...");
				mall_reco_info_ib_send.setEnabled(false);
			}

			@SuppressLint("NewApi")
			@Override
			public void onFinish(MallAddCommentResp response, String error) {
				listView.onRefreshComplete();
				closeProgressDialog();
				hideSoftKeyboard();
				mall_reco_info_ib_send.setEnabled(true);
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					//评论成功，添加到评论列表最下边
					if (HGUtils.isListEmpty(mallComment_list)) {
						mallComment_list.add(mallComment);
					} else {
						mallComment_list.add(mallComment_list.size(), mallComment);
					}
					mall_reco_info_et_signin.setText("");
					loadComms(0);
					adapter.notifyDataSetChanged();
					//滑动到第一条数据
					int i = listView.getRefreshableView().getLastVisiblePosition();
					MLog.e("000000000", i + "");
					mallComment_list.clear();
					mall_recommend_pic_comm_num.setText((mallRecommend.getCommentCount() + 1) + "");
//					ToastUtil.show(context, "评论成功！");
				}
			}
		};

		MallAddCommentRequest request = new MallAddCommentRequest();
		request.setMallComment(mallComment);
		HttpPacketClient.postPacketAsynchronous(request, MallAddCommentResp.class, responseHandler, true);
	}

	protected void addHeart(final String recommendID) {

		ResponseHandler<MallAddHeartResp> responseHandler = new ResponseHandler<MallAddHeartResp>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(MallAddHeartResp response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					ToastUtil.show(context, "点赞成功！");
					MLog.i("TAG", recommendID + "点赞成功");
					mall_recommend_pic_haert_iv.setBackgroundResource(R.drawable.mall_recommend_info_heart_select);
					int heartCount = mallRecommend.getHeartCount();
					mall_recommend_pic_haert_num.setText((heartCount + 1) + "");
				}
			}
		};

		MallAddHeartRequest request = new MallAddHeartRequest();
		request.setRecommendID(recommendID);
		request.setHeartFlag(0);
		HttpPacketClient.postPacketAsynchronous(request, MallAddHeartResp.class, responseHandler, true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
		private Context	context;

		public MyAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {

			return HGUtils.isListEmpty(mallComment_list) ? 0 : mallComment_list.size();
		}

		@Override
		public MallComment getItem(int position) {
			return mallComment_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = View.inflate(context, R.layout.mall_recom_info_comm_item, null);
				viewHolder = new ViewHolder();
				viewHolder.head_portrait = (CircularImage) convertView.findViewById(R.id.head_portrait);
				viewHolder.mall_recom_item_tv_name = (TextView) convertView.findViewById(R.id.mall_recom_item_tv_name);
				viewHolder.mall_recom_item_tv_time = (TextView) convertView.findViewById(R.id.mall_recom_item_tv_time);
				viewHolder.mall_recom_item_tv_dec = (TextView) convertView.findViewById(R.id.mall_recom_item_tv_dec);
				viewHolder.mall_recom_item_tv_dec.setMovementMethod(ScrollingMovementMethod.getInstance());
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MallComment mallComment = mallComment_list.get(position);
			ImageLoader.getInstance().displayImage(mallComment.getCommentIcon(), viewHolder.head_portrait, options);
			viewHolder.mall_recom_item_tv_name
					.setText(StringUtils.isMobileNO(mallComment.getCommentName()) ? StringUtils
							.replaceMobileNo(mallComment.getCommentName()) : mallComment.getCommentName());
			viewHolder.mall_recom_item_tv_time.setText(mallComment.getCommentTime());
			viewHolder.mall_recom_item_tv_dec.setText(mallComment.getCommentDescription());
			return convertView;
		}

	}

	private void hideSoftKeyboard() {
		// Hide soft keyboard, if visible
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(listView.getWindowToken(), 0);
	}

	class ViewHolder {
		CircularImage	head_portrait;
		TextView		mall_recom_item_tv_name;
		TextView		mall_recom_item_tv_time;
		TextView		mall_recom_item_tv_dec;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int userId = PreferencesUtils.getUserID();
		MLog.e("TAG", "userId" + userId);
		if (userId > 0 && (userId == mallRecommend.getRecommendUserID())) {
			getMenuInflater().inflate(R.menu.mall_cart, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_trash) {
			showDeleteDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showDeleteDialog() {
		new AlertDialog.Builder(context).setTitle("删除").setMessage("确定要删除推荐吗？")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteRecom(mallRecommend.getRecommendID());
					}

				}).setNegativeButton("取消", null).show();

	}

	/**
	 * 删除推荐
	 */
	public void deleteRecom(String id) {
		ResponseHandler<MallDeleteRecommendResp> responseHandler = new ResponseHandler<MallDeleteRecommendResp>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍等...");
			}

			@Override
			public void onFinish(MallDeleteRecommendResp response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					//删除成功
					closeProgressDialog();
					ToastUtil.show(context, "删除成功！");
					finish();
				}
			}
		};

		MallDeleteRecommendRequest request = new MallDeleteRecommendRequest();
		request.setRecommendID(id);
		HttpPacketClient.postPacketAsynchronous(request, MallDeleteRecommendResp.class, responseHandler, true);
	}
}

package com.e1858.wuye.activity.tabfragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.common.app.BaseApplication;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.LoginActivity;
import com.e1858.wuye.activity.NoticeActivity;
import com.e1858.wuye.adapter.FastCmdGridViewAdapter;
import com.e1858.wuye.protocol.http.BannerBean;
import com.e1858.wuye.protocol.http.ChangeCommunity;
import com.e1858.wuye.protocol.http.ChangeCommunityResp;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.GetNotices;
import com.e1858.wuye.protocol.http.GetNoticesResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.Notice;
import com.hg.android.widget.ImagePagerLayout;
import com.hg.android.widget.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 首页
 * 
 * @author jiajia 2014年7月22日
 */
public class HomeActivity extends BaseActivity {
	private MyGridView			fast_cmd_GridView;															// 快捷功能键
	private TextView			the_last_notice_TextView;
	private ImageView			first_shop_cmd;
//	private ImageView			second_shop_cmd;
//	private ImageView			third_shop_cmd;
	private final List<Integer>	list				= new ArrayList<Integer>();
//	private LinearLayout		shopLayout;

	private ImagePagerLayout	imagePagerLayout;

	DisplayImageOptions			displayImageOptions	= new DisplayImageOptions.Builder().cacheInMemory(true)
															.cacheOnDisk(true).considerExifParams(true)
															.bitmapConfig(Bitmap.Config.RGB_565).build();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		boolean first = imagePagerLayout == null;
		initView();
		if (first) {
			changeCommunity();
			loadLastNotice();
		}
		initData();
	}

	private void changeCommunity() {
		if (application.isNetworkAvailable()) {
			ChangeCommunity changeCommunity = new ChangeCommunity();
			changeCommunity.setCommunityID(PreferencesUtils.getCommunity().getID());
			changeCommunity.setToken(Encrypt.MD5(Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, changeCommunity, handler, HttpDefine.CHANGE_COMMUNITY_RESP);
		} else {
			ToastUtil.show(getActivity(), getResources().getString(R.string.network_fail));
		}
	}

	private void initView() {
		imagePagerLayout = (ImagePagerLayout) findViewById(R.id.imagePagerLayout);
		fast_cmd_GridView = (MyGridView) findViewById(R.id.home_fast_cmd_gridview);
		first_shop_cmd = (ImageView) findViewById(R.id.first_shop_cmd);
		the_last_notice_TextView = (TextView) findViewById(R.id.the_last_notice);
		if (null != application.getNotice() && application.getNotice().getID() != -1) {
			the_last_notice_TextView.setText(application.getNotice().getTitle());// 设置最后一条通知
		} else {
			the_last_notice_TextView.setText("暂无通知");

		}
		the_last_notice_TextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入通知详情页
				if (null != application.getNotice() && application.getNotice().getID() != -1) {
					if (PreferencesUtils.getUserID() > 0) {
						Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt(Constant.DETAIL_ID, application.getNotice().getID());
						bundle.putInt(Constant.PIC_REMARK, 1);
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
						startActivity(intent);
					}

				} else {
					ToastUtil.show(getActivity(), "对不起,暂无通知!");
				}

			}
		});
	}

	void filter() {
		for (int i = list.size() - 1; i >= 0; i--) {
			switch (list.get(i)) {
			case Constant.FASTCMD.NOTICE:
			case Constant.FASTCMD.PAY_WUYE:
			case Constant.FASTCMD.INTERACT:
			case Constant.FASTCMD.CALL_WUYE:
			case Constant.FASTCMD.COMMISSION:
			case Constant.FASTCMD.COMPLAINT:
			case Constant.FASTCMD.MAINTENANCE:
			case Constant.FASTCMD.SERVICE_PHONE:
			case Constant.FASTCMD.KANGEL:
			case Constant.FASTCMD.LOVEAROUND:
				break;
			default:
				list.remove(i);
				break;
			}
		}
		while (list.size() > 4) {
			list.remove(list.size() - 1);
		}
	}

	private void initData() {

		if (PreferencesUtils.getCommunity().getCommunityInfo().getShortcuts() != null
				&& PreferencesUtils.getCommunity().getCommunityInfo().getShortcuts().size() > 0) {
			list.addAll(PreferencesUtils.getCommunity().getCommunityInfo().getShortcuts());
		} else {
			list.add(Constant.FASTCMD.KANGEL);
			list.add(Constant.FASTCMD.COMMISSION);
			list.add(Constant.FASTCMD.COMPLAINT);
			list.add(Constant.FASTCMD.LOVEAROUND);
		}
		if (!list.contains(Constant.FASTCMD.KANGEL)) {
			list.add(0, Constant.FASTCMD.KANGEL);
		}
		filter();
		fast_cmd_GridView.setAdapter(new FastCmdGridViewAdapter(application, getActivity(), list));
		// 首页三个广告
		initAds();
		imagePagerLayout.setImageUrls(PreferencesUtils.getCommunity().getCommunityInfo().getImages());
	}

	private void loadLastNotice() {
		if (application.isNetworkAvailable()) {
			GetNotices getNotices = new GetNotices();
			getNotices.setCommand(HttpDefine.GET_NOTICES_SELF);
			getNotices.setToken(Encrypt.MD5(Constant.TokenSalt));
			getNotices.setCommunityID(PreferencesUtils.getCommunity().getID());
			getNotices.setOffset(0);
			getNotices.setCount(1);
			getNotices.setType(Constant.NOTICE_TYPE.PUBLIC);
			NetUtil.post(Constant.BASE_URL, getNotices, handler, HttpDefine.GET_NOTICES_SELF_RESP);
		} else {
			Notice notice = (Notice) DataFileUtils.readObject("home_notice" + PreferencesUtils.getCommunity().getID());
			application.setNotice(notice);
			the_last_notice_TextView.setText(notice.getTitle());
			ToastUtil.show(getActivity(), getResources().getString(R.string.network_fail));
		}
	}

	void initAds() {
		final List<BannerBean> ads = PreferencesUtils.getCommunity().getCommunityInfo().getAdvertisements();
		ImageLoader.getInstance().displayImage(ads.get(0).getImageUrl(), first_shop_cmd, displayImageOptions);
//		ImageLoader.getInstance().displayImage(ads.get(1).getImageUrl(), second_shop_cmd, displayImageOptions);
//		ImageLoader.getInstance().displayImage(ads.get(2).getImageUrl(), third_shop_cmd, displayImageOptions);
		first_shop_cmd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = 0;
				BaseApplication.getBaseInstance().judgeForBannerUrl(getActivity(), ads.get(index));
			}
		});
//		second_shop_cmd.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				int index = 1;
//				BaseApplication.getBaseInstance().judgeForBannerUrl(getActivity(), ads.get(index));
//			}
//		});
//		third_shop_cmd.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				int index = 2;
//				BaseApplication.getBaseInstance().judgeForBannerUrl(getActivity(), ads.get(index));
//			}
//		});
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadLastNotice();
		if (application.getIsCommunityChanged()) {
//			bar_title.setText(PreferencesUtils.getCommunity().getName());
			list.clear();
			initData();
		}
		application.setIsCommunityChanged(false);
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (getActivity() == null || isDetached()) {
			return true;
		}
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			return true;
		}
		switch (msg.what) {
		case HttpDefine.GET_NOTICES_SELF_RESP:
			if (null != (String) msg.obj) {
				GetNoticesResp resp = JsonUtil.fromJson((String) msg.obj, GetNoticesResp.class);
				if (resp == null) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					if (null == resp.getNotices() || resp.getNotices().size() == 0) {
						the_last_notice_TextView.setText("暂无通知");
						application.setNotice(new Notice());
						DataFileUtils.saveObject(new Notice(), "home_notice" + PreferencesUtils.getCommunity().getID());
					} else {
						DataFileUtils.saveObject(resp.getNotices().get(0), "home_notice"
								+ PreferencesUtils.getCommunity().getID());
						application.setNotice(resp.getNotices().get(0));
						the_last_notice_TextView.setText(resp.getNotices().get(0).getTitle());// 设置最后一条通知
					}
				} else {
					ToastUtil.show(getActivity(), resp.getError());
				}

			}
			msg.obj = null;
			break;
		case HttpDefine.CHANGE_COMMUNITY_RESP:
			if (null != (String) msg.obj) {
				ChangeCommunityResp resp = JsonUtil.fromJson((String) msg.obj, ChangeCommunityResp.class);
				if (null == resp) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					Community community = resp.getCommunity();
					if (community != null) {
						PreferencesUtils.setCommunity(community);
						initData();
					}
				} else {

				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}
}

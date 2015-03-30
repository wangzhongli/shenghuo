package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.common.Constant;
import com.e1858.common.LoginManager;
import com.e1858.common.app.AppManager;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.MLog;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.tabfragment.MainTabActivity;
import com.e1858.wuye.adapter.CommunityAdapter;
import com.e1858.wuye.protocol.http.ChangeCommunity;
import com.e1858.wuye.protocol.http.ChangeCommunityResp;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.GetCommunityByCityNewRequest;
import com.e1858.wuye.protocol.http.GetCommunityByCityNewResponse;
import com.e1858.wuye.protocol.http.GetCommunityByKeyRequest;
import com.e1858.wuye.protocol.http.GetCommunityByKeyResponse;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.utils.HGUtils;
import com.igexin.sdk.PushManager;

/**
 * 选择小区
 * 
 * @author momo
 */
public class SelectCommunityNewActivity extends BaseActivity implements TextWatcher {
	private TextView				about;
	private PullToRefreshListView	mDownListView;
	private List<Community>			lists			= new ArrayList<Community>();
	private List<Community>			search_lists	= new ArrayList<Community>();
	private CommunityAdapter		adapter;
	private String					currentCity		= "";
	private int						remark			= -1;							//-1表示进首页面   1表示可返回
	private Community				community		= new Community();
	public String					keyWords		= "";							//搜索关键字
	private ListView				search_listView;
	private EditText				search_editor;
	private ImageView				img_delete_search_edit;
	private Button					btn_search_ic;
	private SearchAdapter			searchAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_community);
		currentCity = getIntent().getExtras().getString("CurrentCity");
		remark = getIntent().getExtras().getInt(Constant.PIC_REMARK);
		initView();
		loadData(0);
	}

	private void loadData(int offset) {
		if (application.isNetworkAvailable()) {
			GetCommunityByCityNewRequest getCommunityByCityNewRequest = new GetCommunityByCityNewRequest();
			getCommunityByCityNewRequest.setCity(currentCity);
			getCommunityByCityNewRequest.setOffset(offset);
			getCommunityByCityNewRequest.setCount(10);
			getCommunityByCityNewRequest.setToken(Encrypt.MD5(Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getCommunityByCityNewRequest, handler,
					HttpDefine.GETCOMMUNITYBYCITYNEW_RESP);
		} else {
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}

	}

	private void initView() {
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(getResources().getString(R.string.text_bar_switchCommunity));
		mDownListView = (PullToRefreshListView) this.findViewById(R.id.communtity_listView);
		adapter = new CommunityAdapter(this, lists);
		mDownListView.setAdapter(adapter);
		mDownListView.setMode(Mode.BOTH);
		about = (TextView) findViewById(R.id.about);
		//搜索
		search_listView = (ListView) findViewById(R.id.search_listView);//搜索listView
		searchAdapter = new SearchAdapter(this, search_lists);
		search_editor = (EditText) findViewById(R.id.search_editor);
		search_editor.addTextChangedListener(this);
		img_delete_search_edit = (ImageView) findViewById(R.id.img_delete_search_edit);
		img_delete_search_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search_editor.setText("");
			}
		});
		btn_search_ic = (Button) findViewById(R.id.btn_search_ic);

		mDownListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(adapter == null ? 0 : adapter.getCount());
			}
		});
		mDownListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				community = (Community) adapter.getItem(position - 1);
				if (remark == 1 && !StringUtils.isEmpty(PreferencesUtils.getLoginKey())) {
					ChangeCommunity changeCommunity = new ChangeCommunity();
//					changeCommunity.setKey(application.getKey());
//					changeCommunity.setToken(application.getToken());
					changeCommunity.setKey(PreferencesUtils.getLoginKey());
					changeCommunity.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
					changeCommunity.setCommunityID(community.getID());
					NetUtil.post(Constant.BASE_URL, changeCommunity, handler, HttpDefine.CHANGE_COMMUNITY_RESP);
				} else {
					ChangeCommunity changeCommunity = new ChangeCommunity();
					changeCommunity.setToken(Encrypt.MD5(Constant.TokenSalt));
					changeCommunity.setCommunityID(community.getID());
					NetUtil.post(Constant.BASE_URL, changeCommunity, handler, HttpDefine.CHANGE_COMMUNITY_RESP);
				}
			}
		});
//		mDownListView.setOnItemClickListener(new MyListOnItemListener(adapter));
		search_listView.setAdapter(searchAdapter);
		search_listView.setOnItemClickListener(new MyListOnItemListener(searchAdapter));
	}

	class MyListOnItemListener implements OnItemClickListener {
		public BaseAdapter	adapter;

		public MyListOnItemListener(BaseAdapter adapter) {
			super();
			this.adapter = adapter;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			community = (Community) adapter.getItem(position);
			if (remark == 1 && !StringUtils.isEmpty(PreferencesUtils.getLoginKey())) {
				ChangeCommunity changeCommunity = new ChangeCommunity();
//				changeCommunity.setKey(application.getKey());
//				changeCommunity.setToken(application.getToken());
				changeCommunity.setKey(PreferencesUtils.getLoginKey());
				changeCommunity.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
				changeCommunity.setCommunityID(community.getID());
				NetUtil.post(Constant.BASE_URL, changeCommunity, handler, HttpDefine.CHANGE_COMMUNITY_RESP);
			} else {
				ChangeCommunity changeCommunity = new ChangeCommunity();
				changeCommunity.setToken(Encrypt.MD5(Constant.TokenSalt));
				changeCommunity.setCommunityID(community.getID());
				NetUtil.post(Constant.BASE_URL, changeCommunity, handler, HttpDefine.CHANGE_COMMUNITY_RESP);
			}

		}

	}

	/**
	 * 开始搜索
	 */
	public void startQuery(String keyWords) {
		if (application.isNetworkAvailable()) {
			openProgressDialog("请稍候...");
			GetCommunityByKeyRequest getCommunityByKeyRequest = new GetCommunityByKeyRequest();
			getCommunityByKeyRequest.setCity(currentCity);
			getCommunityByKeyRequest.setKeyWords(keyWords);
			getCommunityByKeyRequest.setToken(Encrypt.MD5(Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getCommunityByKeyRequest, handler, HttpDefine.GETCOMMUNITYBYKEY_RESP);
		} else {
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case HttpDefine.GETCOMMUNITYBYCITYNEW_RESP:
			if (null != (String) msg.obj) {
				GetCommunityByCityNewResponse resp = JsonUtil.fromJson((String) msg.obj,
						GetCommunityByCityNewResponse.class);
				mDownListView.onRefreshComplete();
				if (null == resp) {
					mDownListView.setVisibility(View.GONE);
					about.setVisibility(View.VISIBLE);
					about.setText("很抱歉,你所选择的城市暂没有相关小区");
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					if (resp.getCommunities() != null) {
						lists.addAll(resp.getCommunities());
						adapter.notifyDataSetChanged();
					}
				} else {
					ToastUtil.show(SelectCommunityNewActivity.this, "" + resp.getError());
				}
				if (adapter.getCount() == 0) {
					mDownListView.setVisibility(View.GONE);
					about.setVisibility(View.VISIBLE);
					about.setText("很抱歉,你所选择的城市暂没有相关小区");
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.GETCOMMUNITYBYKEY_RESP://搜索返回

			if (null != (String) msg.obj) {
				mDownListView.setVisibility(View.GONE);
				GetCommunityByKeyResponse resp = JsonUtil.fromJson((String) msg.obj, GetCommunityByKeyResponse.class);
				if (null == resp) {//如果返回为空的话，说明没有搜索到小区
					search_listView.setVisibility(View.GONE);
					about.setVisibility(View.VISIBLE);
					about.setText("很抱歉,没有搜索到相关小区");
					closeProgressDialog();
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					if (resp.getCommunities().size() == 0) {
						about.setVisibility(View.VISIBLE);
						about.setText("很抱歉,你所选择的城市暂没有相关小区");
						mDownListView.setVisibility(View.GONE);
						search_listView.setVisibility(View.GONE);
						closeProgressDialog();
					} else {
						search_lists.clear();
						about.setVisibility(View.GONE);
						search_lists.addAll(resp.getCommunities());
						search_listView.setVisibility(View.VISIBLE);
						searchAdapter.notifyDataSetChanged();
						closeProgressDialog();
					}
				} else {
					search_listView.setVisibility(View.GONE);
					ToastUtil.show(SelectCommunityNewActivity.this, "" + resp.getError());
					about.setVisibility(View.VISIBLE);
					mDownListView.setVisibility(View.GONE);
					search_listView.setVisibility(View.GONE);
					about.setText("很抱歉,没有搜索到相关小区");
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.CHANGE_COMMUNITY_RESP:
			if (null != (String) msg.obj) {
				ChangeCommunityResp changeCommunityResp = JsonUtil
						.fromJson((String) msg.obj, ChangeCommunityResp.class);
				if (null == changeCommunityResp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == changeCommunityResp.getRet()) {
					if (remark == 1) {
						Intent data = new Intent();
						data.putExtra(Constant.HOUSE_DATA, changeCommunityResp.getCommunity());
						SelectCommunityNewActivity.this.setResult(Constant.SELECT_COMMUNITY_RESULT_CODE, data);
						PreferencesUtils.setUserHouse(changeCommunityResp.getUserHouse());
						application.setIsCommunityChanged(true);
						application.setIsConvienceChanged(true);
						Intent intent = new Intent(this, MainTabActivity.class);
						startActivity(intent);
						AppManager.getAppManager().finishActivity();
					} else {
						Intent intent = new Intent(SelectCommunityNewActivity.this, MainTabActivity.class);
						SelectCommunityNewActivity.this.startActivity(intent);
						AppManager.getAppManager().finishActivity();
					}
					PreferencesUtils.setCommunity(changeCommunityResp.getCommunity());
					int userID = PreferencesUtils.getUserID();
					if (userID > 0) {
						LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), PushManager
								.getInstance().getClientid(getApplicationContext()), userID, PreferencesUtils
								.getLoginKey(), Encrypt.MD5(PreferencesUtils.getLoginKey() + Constant.TokenSalt));
					} else {
						LoginManager.setClientID(handler, PreferencesUtils.getCommunity().getID(), PushManager
								.getInstance().getClientid(getApplicationContext()), userID, "", Encrypt
								.MD5(Constant.TokenSalt));
					}

				} else {
					ToastUtil.show(this, changeCommunityResp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		switch (s.length()) {
		case 0:
			img_delete_search_edit.setVisibility(View.GONE);
			mDownListView.setVisibility(View.VISIBLE);
			break;

		default:
			img_delete_search_edit.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		keyWords = s.toString().trim();
		MLog.e("SelectCommunityNewActivity", keyWords);
		if (!TextUtils.isEmpty(keyWords)) {
			startQuery(keyWords);
		} else {
			MLog.e("TAG", "afterTextChanged+0");
			if (HGUtils.isListEmpty(lists)) {
				about.setVisibility(View.VISIBLE);
				about.setText("很抱歉,你所选择的城市暂没有相关小区");
			} else {
				about.setVisibility(View.GONE);
				mDownListView.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
			}
		}
	}

	class SearchAdapter extends BaseAdapter {
		private Context			context;
		private List<Community>	search_lists;

		public SearchAdapter(Context context, List<Community> search_lists) {
			super();
			this.context = context;
			this.search_lists = search_lists;
		}

		@Override
		public int getCount() {
			return search_lists.size();
		}

		@Override
		public Community getItem(int position) {
			return search_lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Community community = search_lists.get(position);
			Spannable text = StringUtils.HighlightText(keyWords, community.getName(), false);
			ViewHolder viewHolder;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.community_item, null);
				viewHolder.item_title = (TextView) convertView.findViewById(R.id.item_title);
				viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.item_title.setText(text);
			viewHolder.item_content.setText(community.getAddress());
			return convertView;
		}

		class ViewHolder {
			private TextView	item_title;
			private TextView	item_content;
		}
	}

}

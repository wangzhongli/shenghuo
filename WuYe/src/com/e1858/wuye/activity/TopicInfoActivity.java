package com.e1858.wuye.activity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.DateUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.PullToRefreshListView;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.TopicDetailAdapter;
import com.e1858.wuye.protocol.http.AddBbsComment;
import com.e1858.wuye.protocol.http.AddBbsCommentResp;
import com.e1858.wuye.protocol.http.BbsComments;
import com.e1858.wuye.protocol.http.BbsTopic;
import com.e1858.wuye.protocol.http.GetBbsComments;
import com.e1858.wuye.protocol.http.GetBbsCommentsResp;
import com.e1858.wuye.protocol.http.HttpDefine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 话题详细
 */
public class TopicInfoActivity extends BaseActivity implements OnClickListener {
	private PullToRefreshListView mDownListView;
	private ImageView emotionImageView;//表情
	private EditText replyContent;
	private Button sendBtn;
	private int direction = 1;
	private List<BbsComments> bbsComments = new ArrayList<BbsComments>();
	private TopicDetailAdapter adapter = null;
	private BbsTopic bbsTopic;
	private LinearLayout bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_detail);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		bbsTopic = (BbsTopic) getIntent().getExtras().getSerializable(
				"TOPIC_INFO");
		initView();
		loadData(direction, 0, true);
		footer_more.setVisibility(View.GONE);
	}

	private void initView() {
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText(bbsTopic.getTitle());
		mDownListView = (PullToRefreshListView) findViewById(R.id.listview);
		bottom=(LinearLayout)findViewById(R.id.rl_bottom);
		footer_view = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		footer_bar = (ProgressBar) footer_view
				.findViewById(R.id.listview_foot_progress);
		footer_more = (TextView) footer_view
				.findViewById(R.id.listview_foot_more);

		mDownListView.addFooterView(footer_view);
		adapter=new TopicDetailAdapter(this, application, bbsTopic, bbsComments);
		mDownListView.setAdapter(adapter);
		mDownListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mDownListView.onScrollStateChanged(view, scrollState);
				// 数据为空--不用继续下面代码了
				if (bbsComments.isEmpty())
					return;
				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(footer_view) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				if (scrollEnd) {
					if (view.getCount() >= Constant.PAGE_SIZE) {
						footer_more
								.setText(R.string.pull_to_refresh_refreshing_label);
						footer_bar.setVisibility(View.VISIBLE);
						direction = Constant.DIRECTION.BACKWARD;
						loadData(direction, bbsComments.size(), false);
					} else {
						footer_bar.setVisibility(View.GONE);
						footer_more.setText(getResources().getString(
								R.string.loading_full));
					}
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mDownListView.onScroll(view, firstVisibleItem,
						visibleItemCount, totalItemCount);
			}
		});
		mDownListView
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						if (application.isNetworkAvailable()) {
							direction = Constant.DIRECTION.FORWARD;
							loadData(direction, 0, false);
						} else {
							ToastUtil.show(
									getApplicationContext(),
									getResources().getString(
											R.string.network_fail));
						}
					}
				});
		emotionImageView = (ImageView) findViewById(R.id.emotion_imageview);
		replyContent = (EditText) findViewById(R.id.reply_content);
		sendBtn = (Button) findViewById(R.id.ok_btn);
		if(bbsTopic.getDisable()){
			bottom.setVisibility(View.GONE);
		}else{
			bottom.setVisibility(View.VISIBLE);
		}
		sendBtn.setOnClickListener(this);
	}

	private void loadData(int direction, int offset, boolean isLocalCache) {
		if (isLocalCache) {
			GetBbsCommentsResp resp = (GetBbsCommentsResp) DataFileUtils
					.readObject(TopicInfoActivity.class.getSimpleName()
							+ bbsTopic.getID());
			if (null != resp) {
				bbsComments.addAll(resp.getBbsComments());
				Message msg = handler.obtainMessage(
						HttpDefine.RESPONSE_SUCCESS, bbsComments);
				msg.arg1 = 1;
				msg.sendToTarget();
			}
		}
		if (application.isNetworkAvailable()) {
			GetBbsComments getBbsPosts = new GetBbsComments();
			getBbsPosts.setCommunityID(PreferencesUtils.getCommunity().getID());
//			getBbsPosts.setKey(application.getKey());
//			getBbsPosts.setToken(application.getToken());
			getBbsPosts.setKey(PreferencesUtils.getLoginKey());
			getBbsPosts.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			getBbsPosts.setTopicID(bbsTopic.getID());
			getBbsPosts.setOffset(offset);
			getBbsPosts.setCount(Constant.PAGE_SIZE);
			NetUtil.post(Constant.BASE_URL, getBbsPosts, handler,
					HttpDefine.GET_BBS_POSTS_RESP);
		} else {
			getResources().getString(R.string.network_fail);
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (null == msg.obj) {
			footer_bar.setVisibility(View.GONE);
			footer_more.setVisibility(View.GONE);
			mDownListView.onRefreshComplete();
			return true;
		}
		switch (msg.what) {
		case Constant.FAIL_CODE:
			if (msg.obj != null) {
				footer_bar.setVisibility(View.GONE);
				footer_more.setVisibility(View.GONE);
				mDownListView.onRefreshComplete();
				ToastUtil.show(this, msg.obj.toString());
			}
		case HttpDefine.RESPONSE_SUCCESS:
			if (msg.arg1 == 1) {
				if(!application.isNetworkAvailable()&&bbsComments.size()<Constant.PAGE_SIZE){
					mDownListView.removeFooterView(footer_view);
				}
				initList(bbsComments);
			}
			break;
		case HttpDefine.GET_BBS_POSTS_RESP:
			if (null != (String) msg.obj) {
				GetBbsCommentsResp resp = JsonUtil.fromJson((String) msg.obj,
						GetBbsCommentsResp.class);
				if (null == resp) {
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS) {
					if((resp.getBbsComments()==null||resp.getBbsComments().size()==0)&&direction==Constant.DIRECTION.BACKWARD){
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						footer_more.setText(getResources().getString(R.string.loading_full));
						break;
					}
					if((resp.getBbsComments()==null||resp.getBbsComments().size()==0)&&direction==Constant.DIRECTION.FORWARD){
						mDownListView.setDividerHeight(0);
						footer_bar.setVisibility(View.GONE);
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						break;
					}
					if (direction == Constant.DIRECTION.FORWARD) {
						
						// 下拉刷新
						List<BbsComments> forward_list = resp.getBbsComments();
						mDownListView.setDividerHeight(1);
						bbsComments.clear();
						bbsComments.addAll(forward_list);
						DataFileUtils.saveObject(resp,
								TopicInfoActivity.class.getSimpleName()
										+ bbsTopic.getID());
						mDownListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						if(forward_list.size()<Constant.PAGE_SIZE){
							mDownListView.removeFooterView(footer_view);
						}
						initList(bbsComments);
					} else {
						// 底部加载更多
						footer_bar.setVisibility(View.GONE);
						footer_more.setVisibility(View.VISIBLE);
						if(resp.getBbsComments().size()<Constant.PAGE_SIZE){
							footer_more.setText(getResources().getString(R.string.loading_full));
						}else{
							footer_more.setText(getResources().getString(R.string.footer_more));
						}
						mDownListView.setSelection(mDownListView.getCount()-resp.getBbsComments().size()+2);
						bbsComments.addAll(resp.getBbsComments());
						adapter.notifyDataSetChanged();

					}

				} else {
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		case HttpDefine.ADD_BBS_COMMENT_RESP:
			if (null != (String) msg.obj) {
				AddBbsCommentResp resp = JsonUtil.fromJson((String) msg.obj,
						AddBbsCommentResp.class);
				if (null == resp) {
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet()) {
					ToastUtil.show(this, "回复成功");
					BbsComments bbsComment = new BbsComments();
					bbsComment.setContent(replyContent.getText().toString());
					bbsComment.setCreateTime(DateUtil.getStringDate());
					bbsComment.setCreatorID(PreferencesUtils.getUserID());
					bbsComment.setCreatorNickname(PreferencesUtils.getUserInfo().getNickname());
					bbsComment.setHeadPortrait(PreferencesUtils.getUserInfo().getHeadPortrait());
					bbsComments.add(0, bbsComment);
					initList(bbsComments);
					replyContent.setText("");
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
		switch (v.getId()) {
		case R.id.ok_btn:
			if (StringUtils.isEmpty(replyContent.getText().toString())) {
				ToastUtil.show(this, "回复内容不能为空!");
			} else {
				if (application.isNetworkAvailable()) {
					AddBbsComment addBbsComment = new AddBbsComment();
//					addBbsComment.setKey(application.getKey());
//					addBbsComment.setToken(application.getToken());
					addBbsComment.setKey(PreferencesUtils.getLoginKey());
					addBbsComment.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
					addBbsComment.setTopicID(bbsTopic.getID());
					addBbsComment.setContent(replyContent.getText().toString());
					NetUtil.post(Constant.BASE_URL, addBbsComment, handler,
							HttpDefine.ADD_BBS_COMMENT_RESP);
				} else {
					ToastUtil.show(this,
							getResources().getString(R.string.network_fail));
				}
			}
			break;
		}
	}
	private void initList(List<BbsComments> list) {
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.SEE_PIC_RESULT_CODE:
			break;

		}
	}
}

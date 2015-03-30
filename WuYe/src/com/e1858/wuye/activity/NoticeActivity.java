package com.e1858.wuye.activity;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DateUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.GetNoticeById;
import com.e1858.wuye.protocol.http.GetNoticeByIdResp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
/**
 * 通知内容
 * @author jiajia 2014年8月22日
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class NoticeActivity extends BaseActivity
{
	private WebView webView;
	private int notice;
	private int remark=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_detail);
		notice = getIntent().getExtras().getInt(Constant.DETAIL_ID);
		remark=getIntent().getExtras().getInt(Constant.PIC_REMARK);
		initView();
		initData();
	}

	private void initView()
	{
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title.setText(getResources().getString(R.string.text_bar_notice_detail));
		bar_leftBtn.setVisibility(View.VISIBLE);
		webView = (WebView) findViewById(R.id.notice_web);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultFontSize(15);
	}

	private void initData()
	{
		if (application.isNetworkAvailable())
		{
			openProgressDialog("加载中...");
			GetNoticeById readNoticeById = new GetNoticeById();
			if(remark==1){
				if(!StringUtils.isEmpty(PreferencesUtils.getLoginKey())){
//					readNoticeById.setKey(application.getKey());
//					readNoticeById.setToken(application.getToken());
					readNoticeById.setKey(PreferencesUtils.getLoginKey());
					readNoticeById.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				}else
				{
					readNoticeById.setToken(Encrypt.MD5(Constant.TokenSalt));
				}
				
			}else{
				readNoticeById.setKey(PreferencesUtils.getLoginKey());
				readNoticeById.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			}
			readNoticeById.setCommunityID(PreferencesUtils.getCommunity().getID());
			readNoticeById.setID(notice);
			NetUtil.post(Constant.BASE_URL, readNoticeById, handler, HttpDefine.READ_NOTICE_BY_ID_RESP);
		}
		else
		{
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
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
		case HttpDefine.READ_NOTICE_BY_ID_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj)
			{
				GetNoticeByIdResp resp = JsonUtil.fromJson((String) msg.obj, GetNoticeByIdResp.class);
				if (null == resp)
				{
					break;
				}
				if (HttpDefine.RESPONSE_SUCCESS == resp.getRet())
				{
					// 用webview加载
					try
					{
						StringBuilder contentStr=new StringBuilder();
						contentStr.append("<div style='margin:5pt 0px;padding:0px;text-align:center; font-size:15pt;color:#000000;'>");
						contentStr.append(resp.getNotice().getTitle());
						contentStr.append("</div><div style='padding-bottom:3pt;border-bottom:1px solid #e0e0e0;margin-bottom:7pt;font-size:10pt;color:#767676;' ><span>");
						contentStr.append(DateUtil.dateToZh(resp.getNotice().getSendTime()));
						contentStr.append("</span><span style='float:right;'>");
						contentStr.append(PreferencesUtils.getCommunity().getName());
						contentStr.append("<span style='float:right;'>&nbsp;&nbsp;&nbsp;浏览:&nbsp;");
						contentStr.append(resp.getNotice().getViewCount());
						contentStr.append("</span>");
						contentStr.append("</span></div><html><body>");
						contentStr.append("<html>"+resp.getNotice().getContent()+"</html>");
						contentStr.append("</body></html>");
						webView.loadDataWithBaseURL(null, contentStr.toString(), "text/html", "utf-8", null);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

				}
				else
				{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

}

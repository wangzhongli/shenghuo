package com.e1858.wuye.activity;

import com.e1858.common.Constant;
import com.e1858.wuye.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
 * 协议
 * @author jiajia 2014年8月22日
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class AppProtocolActivity extends BaseActivity
{

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_protocol);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(R.string.text_bar_agreement_protocol));
		webView = (WebView) findViewById(R.id.app_protocol_webview);
	
		webView.setWebViewClient(new WebViewClient()
		{
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				openProgressDialog("加载中...");
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				closeProgressDialog();
			}
			
		});
		webView.setDownloadListener(new DownloadListener()
		{
			
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				//intent.setType(mimetype);
				startActivity(intent);
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(Constant.PROTOCOL_URL);
	}
}

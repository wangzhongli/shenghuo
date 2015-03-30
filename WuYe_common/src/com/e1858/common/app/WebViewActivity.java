package com.e1858.common.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;

public class WebViewActivity extends BaseActionBarActivity {

	public static final String	IntentKey_Title		= "IntentKey_Title";
	public static final String	IntentKey_URL		= "IntentKey_URL";
	public static final String	IntentKey_Content	= "IntentKey_Content";
	WebView						webView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		webView = new WebView(this);
		webView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultFontSize(15);
		setContentView(webView);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);

		Intent intent = getIntent();
		if (intent == null) {
			finish();
			return;
		}
		onNewIntent(intent);
	}

	@Override
	public void onNewIntent(Intent intent) {
		String title = intent.getStringExtra(IntentKey_Title);
		setTitle(title);
		String URL = intent.getStringExtra(IntentKey_URL);
		String content = intent.getStringExtra(IntentKey_Content);
		if (!TextUtils.isEmpty(URL)) {
			webView.loadUrl(URL);
		} else if (!TextUtils.isEmpty(content)) {
			webView.loadData(content, "text/plaint", "utf-8");
		} else {
			webView.loadData("Empty", null, null);
		}
	}

	/**
	 * Called when the fragment is no longer resumed. Pauses the WebView.
	 */
	@Override
	public void onResume() {
		webView.onResume();
		super.onResume();
	}

	/**
	 * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
	 */
	@Override
	public void onDestroy() {
		if (webView != null) {
			webView.destroy();
			webView = null;
		}
		super.onDestroy();
	}

}

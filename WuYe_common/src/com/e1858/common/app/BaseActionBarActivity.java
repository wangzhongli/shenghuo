package com.e1858.common.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.hg.android.utils.HGUtils;
import com.umeng.analytics.MobclickAgent;

public class BaseActionBarActivity extends ActionBarActivity {
	protected Handler		uiHandler;
	public ProgressDialog	progressDialog	= null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHandler = new Handler();
		AppManager.getAppManager().addActivity(this);
	}

	public void openProgressDialog(final String message) {
		if (null == progressDialog) {
			progressDialog = new ProgressDialog(getActivity());
		}
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onDestroy() {
		uiHandler = null;
		AppManager.getAppManager().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean hasDestroyed() {
		return uiHandler == null;
	}

	@Override
	public void finish() {
		HGUtils.hideKeyboard(this);
		super.finish();
	}

	public BaseActionBarActivity getActivity() {
		return this;
	}
}

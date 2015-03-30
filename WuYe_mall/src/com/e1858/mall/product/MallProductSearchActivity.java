package com.e1858.mall.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.e1858.common.app.BaseActionBarActivity;
import com.e1858.wuye.mall.R;

/**
 * 日期: 2015年2月3日 下午6:55:53
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class MallProductSearchActivity extends BaseActionBarActivity {
	EditText	editText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mall_activity_empty);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		setTitle("");
//		editText = new EditText(getActivity());
//		actionBar.setCustomView(editText, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
//				ActionBar.LayoutParams.MATCH_PARENT));
//		editText.setHint("搜索商品");
//		editText.setFocusable(true);

		View custom = View.inflate(getActivity(), R.layout.mall_view_search, null);
		actionBar.setCustomView(custom, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT));
		editText = (EditText) custom.findViewById(R.id.editText);
		editText.setHint("搜索商品");
		custom.findViewById(R.id.button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MallProductActivity.class);
				intent.putExtra(MallProductActivity.IntentKey_Keyword, editText.getText().toString());
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.mall_productsearch, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mall_action_search) {
			//if (!TextUtils.isEmpty(editText.getText())) //
			{
				Intent intent = new Intent(getActivity(), MallProductActivity.class);
				intent.putExtra(MallProductActivity.IntentKey_Keyword, editText.getText().toString());
				startActivity(intent);
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

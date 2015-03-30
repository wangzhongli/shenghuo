package com.e1858.wuye.activity;

import com.e1858.wuye.R;

import android.os.Bundle;
import android.widget.TextView;
/**
 * 商城
 * @author jiajia
 *
 */
public class ShoppingActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping);
		initView();
	}
	private void initView(){
		bar_title=(TextView)findViewById(R.id.bar_title);
		bar_title.setText(getResources().getString(R.string.text_bar_shopping));
	}

}

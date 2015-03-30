package com.e1858.wuye.activity;

import com.e1858.wuye.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 我的便民记录
 * @author jiajia
 *
 */
public class MyConvenienceRecordActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout kangel_record;//洗衣记录
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_convenience_record);
		initView();
	}

	private void initView(){
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title.setText("我的便民记录");
		kangel_record=(RelativeLayout)findViewById(R.id.kangel_record);
		kangel_record.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		switch(v.getId()){
		case R.id.kangel_record:
			intent.setClass(this, KangelRecordsActivity.class);
			startActivity(intent);
			break;
		}
	}

}

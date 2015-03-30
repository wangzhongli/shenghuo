package com.e1858.wuye.activity;

import com.e1858.wuye.R;
import com.hg.android.widget.MyGridView;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
/**
 * 报修评价
 * @author jiajia
 *
 */
public class MaintenanceEvaluationActivity extends BaseActivity implements OnClickListener
{
	private TextView mType;
	private TextView mStatus;
	private RatingBar mTimelyBar;
	private RatingBar mAtiitudeBar;
	private RatingBar mQualityBar;
	private MyGridView myGridView;
	private EditText mEvaluation;
	private Button  mOkBtn;
	private LinearLayout exit_pic_lin;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintenance_evaluation);
		initView();
		loadData();
	}

	
	private void initView()
	{
		// TODO Auto-generated method stub
		mType=(TextView)findViewById(R.id.maintenance_type);
		mStatus=(TextView)findViewById(R.id.maintenance_status);
		mTimelyBar=(RatingBar)findViewById(R.id.timely_bar);
		mAtiitudeBar=(RatingBar)findViewById(R.id.attitude_bar);
		mQualityBar=(RatingBar)findViewById(R.id.quality_bar);
		myGridView=(MyGridView)findViewById(R.id.gridview);
		mEvaluation=(EditText)findViewById(R.id.input_evaluation);
		mOkBtn=(Button)findViewById(R.id.ok_btn);
		exit_pic_lin=(LinearLayout)findViewById(R.id.exit_pic_lin);
	}
	private void loadData()
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ok_btn:
			break;
		}
	}

	

}

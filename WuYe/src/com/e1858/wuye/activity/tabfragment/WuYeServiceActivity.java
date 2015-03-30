package com.e1858.wuye.activity.tabfragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.e1858.common.Constant;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.FastCmdGridViewAdapter;
/**
 * 物业服务
 * @author jiajia
 *
 */
public class WuYeServiceActivity extends BaseActivity
{

	private GridView mGridView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.wuye_service, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);	
		initView();
		initData();
	}

	private void initView(){
		mGridView=(GridView)findViewById(R.id.gridview);
//		bar_title=(TextView)findViewById(R.id.bar_title);
//		bar_title.setText(getResources().getString(R.string.text_bar_wuye_service));
	}
	private void initData(){
		List<Integer> list=new ArrayList<Integer>();
		list.add(Constant.FASTCMD.PAY_WUYE);
		list.add(Constant.FASTCMD.INTERACT);
		list.add(Constant.FASTCMD.COMPLAINT);
		list.add(Constant.FASTCMD.MAINTENANCE);
		list.add(Constant.FASTCMD.NOTICE);
		list.add(Constant.FASTCMD.COMMISSION);
		list.add(Constant.FASTCMD.SERVICE_PHONE);
		mGridView.setAdapter(new FastCmdGridViewAdapter(application,getActivity(), list));
	}

}

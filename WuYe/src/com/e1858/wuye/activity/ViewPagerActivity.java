package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.common.app.AppManager;
import com.e1858.widget.AutoScrollViewPager;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.ViewPagerAdapter;
import com.e1858.wuye.adapter.ViewPagerUrlAdapter;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * 图片查看器
 * @author jiajia
 *
 */
public class ViewPagerActivity extends BaseActivity
{
	private AutoScrollViewPager viewPager;
	private int index = 0;
	private int count = 0;
	private LinkedList<Bitmap> bitmaps = new LinkedList<Bitmap>();
	private List<String> bitmap_urls=new ArrayList<String>();
	private int remark;
	private Dialog deleteDialog;
	private ViewPagerAdapter adapter;
	private ViewPagerUrlAdapter adUrlAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);
		remark=getIntent().getExtras().getInt(Constant.PIC_REMARK);
		if(remark==Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE){
			bitmaps.addAll(AddMaintenanceActivity.pics);
			bitmaps.removeLast();
			count=bitmaps.size();
		}else if(remark==Constant.SEE_PIC_RESULT_CODE){
			bitmap_urls.addAll(application.getBitmaps());
			count=bitmap_urls.size();
		}else if(remark==Constant.TOPIC_EDIT_PIC_RESULT_CODE){
			bitmaps.addAll(AddTopicActivity.pics);
			bitmaps.removeLast();
			count=bitmaps.size();
		}
		initView();
		initPagers();
		
	}
	private void initView(){
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_leftBtn.setVisibility(View.VISIBLE);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_rightBtn=(Button)findViewById(R.id.bar_right_btn);
		bar_rightBtn.setBackgroundResource(R.drawable.delete_pic_bar_btn_background);
		if(remark==Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE||remark==Constant.TOPIC_EDIT_PIC_RESULT_CODE){
			
			bar_rightBtn.setVisibility(View.VISIBLE);
		}else{
			bar_rightBtn.setBackgroundResource(R.color.titleBar_background);
		}
		bar_title.setText(((index%count)+1)+"/"+count);
		bar_rightBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				createDeleteDialog();
			}
		});
		bar_leftBtn.setOnClickListener(new OnClickListener()
		{
			
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(remark==Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE){
					ViewPagerActivity.this.setResult(Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE);
					AppManager.getAppManager().finishActivity();
				}else if(remark==Constant.TOPIC_EDIT_PIC_RESULT_CODE){
					ViewPagerActivity.this.setResult(Constant.TOPIC_EDIT_PIC_RESULT_CODE);
					AppManager.getAppManager().finishActivity();
				}
				else{
					ViewPagerActivity.this.setResult(Constant.SEE_PIC_RESULT_CODE);
					AppManager.getAppManager().finishActivity();
				}
				
			}
		});
		viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
	}
	private void initPagers()
	{
		if(remark==Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE||remark==Constant.TOPIC_EDIT_PIC_RESULT_CODE){
			adapter=new ViewPagerAdapter(ViewPagerActivity.this, bitmaps).setInfiniteLoop(true);
			viewPager.setAdapter(adapter);
			viewPager.setCycle(false);
			viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
			viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % count);
			viewPager.setOnPageChangeListener(new OnPageChangeListener()
			{

				@Override
				public void onPageSelected(int position)
				{
					// TODO Auto-generated method stub
					index = position;
					bar_title.setText(((index%count)+1)+"/"+count);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0)
				{
					// TODO Auto-generated method stub

				}
			});
		}else{
			adUrlAdapter=new ViewPagerUrlAdapter(ViewPagerActivity.this, bitmap_urls).setInfiniteLoop(true);
			viewPager.setAdapter(adUrlAdapter);
			viewPager.setCycle(false);
			viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
			viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % count);
			viewPager.setOnPageChangeListener(new OnPageChangeListener()
			{

				@Override
				public void onPageSelected(int position)
				{
					// TODO Auto-generated method stub
					index = position;
					bar_title.setText(((index%count)+1)+"/"+count);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0)
				{
					// TODO Auto-generated method stub

				}
			});
		}
		
	}

	private void createDeleteDialog(){
		deleteDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		deleteDialog.setContentView(R.layout.delete_pic_dialog);
		// 设置样式
		Window window = deleteDialog.getWindow();
		WindowManager.LayoutParams lp=window.getAttributes(); 
		lp.dimAmount=0.8f;  
		window.setAttributes(lp); 
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.pic_dialog_style);
		Button deleteBtn = (Button) deleteDialog.findViewById(R.id.delete_btn);
		Button cancleBtn = (Button) deleteDialog.findViewById(R.id.cancel_btn);
		cancleBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				deleteDialog.dismiss();
			}
		});
		deleteBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(remark==Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE){
					deleteDialog.dismiss();
					bitmaps.remove(index % count);
					AddMaintenanceActivity.pics.remove(index % count);
					AddMaintenanceActivity.picPaths.remove(index%count);
					count=count-1;
					if(count==0){
						ViewPagerActivity.this.setResult(Constant.MAINTENANCE_EDIT_PIC_RESULT_CODE);
						AppManager.getAppManager().finishActivity();
					}else{
						bar_title.setText(((index%count)+1)+"/"+count);
						adapter.notifyDataSetChanged();
					}
				}else if(remark==Constant.TOPIC_EDIT_PIC_RESULT_CODE){
					deleteDialog.dismiss();
					bitmaps.remove(index % count);
					AddTopicActivity.pics.remove(index % count);
					AddTopicActivity.picPaths.remove(index%count);
					count=count-1;
					if(count==0){
						ViewPagerActivity.this.setResult(Constant.TOPIC_EDIT_PIC_RESULT_CODE);
						AppManager.getAppManager().finishActivity();
					}else{
						bar_title.setText(((index%count)+1)+"/"+count);
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
		deleteDialog.show();
		
	}
}

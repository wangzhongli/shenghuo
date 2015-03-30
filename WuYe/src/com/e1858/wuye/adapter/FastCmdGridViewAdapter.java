package com.e1858.wuye.adapter;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e1858.common.CallDialgoManager;
import com.e1858.common.Constant;
import com.e1858.common.UIHelper;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.activity.LoginActivity;
import com.e1858.wuye.activity.PaymentMainActivity;

public class FastCmdGridViewAdapter extends BaseAdapter {
	private final Context			context;
	private final List<Integer>		list;
	private final MainApplication	application;
	private Dialog					callWuYeDialog;

	public FastCmdGridViewAdapter(MainApplication application, Context context, List<Integer> list) {
		this.context = context;
		this.list = list;
		this.application = application;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.fast_cmd_gridview_item, null);
			viewHolder.fast_grid_textview = (TextView) convertView.findViewById(R.id.fast_grid_title);
			viewHolder.item_icon = (ImageView) convertView.findViewById(R.id.fast_grid_icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		switch (list.get(position)) {
		case Constant.FASTCMD.NOTICE:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_notice));
			viewHolder.item_icon.setBackgroundResource(R.drawable.notice_icon);
			break;
		case Constant.FASTCMD.PAY_WUYE:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_pay_wuye));
			viewHolder.item_icon.setBackgroundResource(R.drawable.pay_wuye_icon);
			break;
		case Constant.FASTCMD.INTERACT:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_interact));
			viewHolder.item_icon.setBackgroundResource(R.drawable.interact_icon);
			break;
		case Constant.FASTCMD.CALL_WUYE:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_call_wuye));
			viewHolder.item_icon.setBackgroundResource(R.drawable.call_wuye_icon);
			break;
		case Constant.FASTCMD.COMMISSION:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_commissions));
			viewHolder.item_icon.setBackgroundResource(R.drawable.commission_icon);
			break;
		case Constant.FASTCMD.COMPLAINT:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_complaints));
			viewHolder.item_icon.setBackgroundResource(R.drawable.complaint_icon);
			break;
		case Constant.FASTCMD.MAINTENANCE:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_maintenance));
			viewHolder.item_icon.setBackgroundResource(R.drawable.maintenance_icon);
			break;
		case Constant.FASTCMD.SERVICE_PHONE:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_service_phone));
			viewHolder.item_icon.setBackgroundResource(R.drawable.servicephone_icon);
			break;
		case Constant.FASTCMD.KANGEL:
			viewHolder.fast_grid_textview.setText("我要洗衣");
			viewHolder.item_icon.setBackgroundResource(R.drawable.kangel_icon);
			break;
		case Constant.FASTCMD.LOVEAROUND:
			viewHolder.fast_grid_textview.setText(context.getResources().getString(R.string.text_bar_child_safe));
			viewHolder.item_icon.setBackgroundResource(R.drawable.love_around);
			break;
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(PreferencesUtils.getLoginKey())) {
					Intent intent = new Intent();
					intent.setClass(context, LoginActivity.class);
					context.startActivity(intent);
				} else {
					Intent intent = new Intent();
//					UserHouse userHouse = (UserHouse) application.readObject(application.getUserID() + "_@");
					switch (list.get(position)) {
					case Constant.FASTCMD.PAY_WUYE:
//						ToastUtil.show(context, context.getResources().getString(R.string.text_no_open));
						intent.setClass(context, PaymentMainActivity.class);
						context.startActivity(intent);
						break;
					case Constant.FASTCMD.CALL_WUYE:
						createCallWuyeDialog(context);
						break;
					case Constant.FASTCMD.SERVICE_PHONE:
						intent.setClassName(context, UIHelper.doIntent(list.get(position)));
						Bundle bundle = new Bundle();
						bundle.putInt(Constant.PIC_REMARK, 2);
						intent.putExtras(bundle);
						context.startActivity(intent);
						break;
					case Constant.FASTCMD.COMMISSION:
//						if (null == userHouse) {
//							ToastUtil.show(context, "请先选择房屋信息", 1000);
//							intent.setClass(context, SwitchResidentAddressActivity.class);
//						} else {
						intent.setClassName(context, UIHelper.doIntent(list.get(position)));
//						}
						context.startActivity(intent);
						break;
					case Constant.FASTCMD.COMPLAINT:
//						if (null == userHouse) {
//							ToastUtil.show(context, "请先选择房屋信息", 1000);
//							intent.setClass(context, SwitchResidentAddressActivity.class);
//						} else {
						intent.setClassName(context, UIHelper.doIntent(list.get(position)));
//						}
						context.startActivity(intent);
						break;
					case Constant.FASTCMD.MAINTENANCE:
//						if (null == userHouse) {
//							ToastUtil.show(context, "请先选择房屋信息", 1000);
//							intent.setClass(context, SwitchResidentAddressActivity.class);
//						} else {
						intent.setClassName(context, UIHelper.doIntent(list.get(position)));
//						}
						context.startActivity(intent);
						break;
					case Constant.FASTCMD.INTERACT:
						intent.setClassName(context, UIHelper.doIntent(list.get(position)));
						context.startActivity(intent);
						break;
					case Constant.FASTCMD.NOTICE:
						intent.setClassName(context, UIHelper.doIntent(list.get(position)));
						bundle = new Bundle();
						bundle.putInt(Constant.PIC_REMARK, -1);
						intent.putExtras(bundle);
						context.startActivity(intent);
						break;
					case Constant.FASTCMD.KANGEL:
						intent.setClassName(context, UIHelper.doIntent(list.get(position)));
						context.startActivity(intent);
						break;
					case Constant.FASTCMD.LOVEAROUND:
						String username = PreferencesUtils.getUserID() + "cxsh";
						com.cwtcn.loveroundlibs.LoveAroundManager manager = new com.cwtcn.loveroundlibs.LoveAroundManager(
								context, username);
						manager.init();
						break;
					default:
						ToastUtil.show(context, context.getResources().getString(R.string.text_no_open));
						break;
					}
				}
			}
		});
		return convertView;
	}

	private void createCallWuyeDialog(final Context context) {
		callWuYeDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		callWuYeDialog.setContentView(R.layout.home_call_wuye_dialog);
		// 设置样式
		Window window = callWuYeDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.8f;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.pic_dialog_style);
		RelativeLayout call_phone = (RelativeLayout) callWuYeDialog.findViewById(R.id.call_phone);
		RelativeLayout call_mobile = (RelativeLayout) callWuYeDialog.findViewById(R.id.call_mobile);
		TextView wuye_phone = (TextView) callWuYeDialog.findViewById(R.id.wuye_phone);
		TextView wuye_mobile = (TextView) callWuYeDialog.findViewById(R.id.wuye_mobile);
		Button cancle = (Button) callWuYeDialog.findViewById(R.id.cancel_btn);
		if (!StringUtils.isEmpty(PreferencesUtils.getCommunity().getCommunityInfo().getPhone())) {
			wuye_phone.setText("物业固话:" + PreferencesUtils.getCommunity().getCommunityInfo().getPhone());
		} else {
			call_phone.setVisibility(View.GONE);
		}
		if (!StringUtils.isEmpty(PreferencesUtils.getCommunity().getCommunityInfo().getMobile())) {
			wuye_mobile.setText("物业手机:" + PreferencesUtils.getCommunity().getCommunityInfo().getMobile());
		} else {
			call_mobile.setVisibility(View.GONE);
		}
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callWuYeDialog.dismiss();
			}
		});
		call_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callWuYeDialog.dismiss();
				CallDialgoManager.createCallDialog(context, PreferencesUtils.getCommunity().getCommunityInfo()
						.getPhone());
			}
		});
		call_mobile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callWuYeDialog.dismiss();
				CallDialgoManager.createCallDialog(context, PreferencesUtils.getCommunity().getCommunityInfo()
						.getMobile());
			}
		});
		callWuYeDialog.show();
	}

	class ViewHolder {
		private TextView	fast_grid_textview;
		private ImageView	item_icon;
	}
}

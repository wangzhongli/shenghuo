package com.e1858.wuye.activity;

import java.util.List;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DateUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.Commission;
import com.e1858.wuye.protocol.http.CommissionResponse;
import com.e1858.wuye.protocol.http.GetCommissionById;
import com.e1858.wuye.protocol.http.GetCommissionByIdResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.hg.android.widget.MyListView;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 代办详细
 * @author jiajia 2014年8月22日
 *
 */
public class CommissionDetailActivity extends BaseActivity
{
	private TextView detail_content;
	private TextView detail_time;
	private TextView detail_comment_num;
	private TextView detail_type;
	private MyListView respListView;
	private int commission;
	private LinearLayout reply_lin;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commission_detail);
		commission=getIntent().getExtras().getInt(Constant.DETAIL_ID);
		initView();
		initData(commission);
	}
	private void initView(){
		bar_title=(TextView)findViewById(R.id.bar_title);
		bar_leftBtn=(Button)findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title.setText(getResources().getString(R.string.text_bar_commission_detail));
		bar_leftBtn.setVisibility(View.VISIBLE);
		detail_content=(TextView)findViewById(R.id.detail_content);
		detail_comment_num=(TextView)findViewById(R.id.detail_comment_num);
		detail_type=(TextView)findViewById(R.id.detail_type);
		detail_time=(TextView)findViewById(R.id.detail_time);
		reply_lin=(LinearLayout)findViewById(R.id.reply_lin);
		respListView=(MyListView)findViewById(R.id.listview);
	}

	private void initData(int commission){
		if(application.isNetworkAvailable()){
			openProgressDialog("加载中...");
			GetCommissionById getCommissionById=new GetCommissionById();
			getCommissionById.setCommunityID(PreferencesUtils.getCommunity().getID());
			getCommissionById.setID(commission);
			getCommissionById.setKey(PreferencesUtils.getLoginKey());
			getCommissionById.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
//			getCommissionById.setKey(application.getKey());
//			getCommissionById.setToken(application.getToken());
			NetUtil.post(Constant.BASE_URL, getCommissionById, handler, HttpDefine.GET_COMMISSION_BY_ID_RESP);
			
		}else{
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}
	private void initData(Commission commission)
	{
		if(commission.getType()!=null&&!StringUtils.isEmpty(commission.getType().getName())){
			detail_type.setText(commission.getType().getName());
		}else{
			detail_type.setText("代办类型");
		}
		detail_content.setText(Html.fromHtml(commission.getContent()));
		detail_time.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.time_icon), null, null, null);
		detail_time.setText(DateUtil.dateToZh(commission.getCreateTime()));
		detail_comment_num.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.resp_icon), null, null, null);
		
		if(commission!=null&&commission.getResponses()!=null&&commission.getResponses().size()>0)
		{
			detail_comment_num.setText(""+commission.getResponses().size());
			respListView.setAdapter(new RespAdapter(this, commission.getResponses()));
		}else{
			reply_lin.setVisibility(View.GONE);
			detail_comment_num.setText("0");
		}
		
	}
	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		if(null==msg.obj){
			closeProgressDialog();
			return true;
		}
		switch(msg.what){
		case HttpDefine.GET_COMMISSION_BY_ID_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj)
			{
				GetCommissionByIdResp resp=JsonUtil.fromJson((String)msg.obj, GetCommissionByIdResp.class);
				if(resp==null){
					break;
				}
				if(resp.getRet()==HttpDefine.RESPONSE_SUCCESS){
					initData(resp.getCommission());
				}else{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj=null;
			break;
		}
		return super.handleMessage(msg);
	}
	private class RespAdapter extends BaseAdapter
	{
		private Context context;
		private List<CommissionResponse> list;

		public RespAdapter(Context context, List<CommissionResponse> list)
		{
			this.context=context;
			this.list=list;
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=LayoutInflater.from(context).inflate(R.layout.reply_list_item, null);
				viewHolder.time=(TextView)convertView.findViewById(R.id.detail_time);
				viewHolder.content=(TextView)convertView.findViewById(R.id.detail_content);
				viewHolder.reply=(TextView)convertView.findViewById(R.id.detail_reply);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder)convertView.getTag();
			}
			if(StringUtils.isEmpty(list.get(position).getCreatorName())){
				viewHolder.reply.setText("物业客服");
			}else{
				viewHolder.reply.setText(list.get(position).getCreatorName());
			}
			viewHolder.time.setText(DateUtil.dateToZh(list.get(position).getCreateTime()));
			viewHolder.content.setText(list.get(position).getContent());
			return convertView;
		}
	}
	class ViewHolder{
		private TextView reply;
		private TextView time;
		private TextView content;
	}
}

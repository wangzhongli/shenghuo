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
import com.e1858.wuye.protocol.http.Complaint;
import com.e1858.wuye.protocol.http.ComplaintResponse;
import com.e1858.wuye.protocol.http.GetComplaintById;
import com.e1858.wuye.protocol.http.GetComplaintByIdResp;
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
 * 投诉与建议详细
 * @author jiajia 2014年8月22日
 *
 */
public class ComplaintDetailActivity extends BaseActivity
{

	private TextView detail_content;
	private TextView detail_time;
	private TextView detail_comment_num;
	private MyListView respListView;
	private int complaint;
	private LinearLayout reply_lin;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complaint_detail);
		complaint = getIntent().getExtras().getInt(Constant.DETAIL_ID);
		initView();
		loadData(complaint);
	}

	private void initView()
	{
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_title.setText(getResources().getString(R.string.text_bar_complaint_detail));
		bar_leftBtn.setVisibility(View.VISIBLE);
		detail_content = (TextView) findViewById(R.id.detail_content);
		detail_comment_num = (TextView) findViewById(R.id.detail_comment_num);
		detail_time = (TextView) findViewById(R.id.detail_time);
		reply_lin=(LinearLayout)findViewById(R.id.reply_lin);
		respListView = (MyListView) findViewById(R.id.listview);
	}

	private void loadData(int complaint)
	{
		if (application.isNetworkAvailable())
		{
			openProgressDialog("加载中...");
			GetComplaintById getComplaintById = new GetComplaintById();
			getComplaintById.setCommunityID(PreferencesUtils.getCommunity().getID());
			getComplaintById.setID(complaint);
			getComplaintById.setKey(PreferencesUtils.getLoginKey());
			getComplaintById.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getComplaintById, handler, HttpDefine.GET_COMPLAINT_BY_ID_RESP);
		}
		else
		{
			ToastUtil.show(this, getResources().getString(R.string.network_fail));
		}
	}

	private void initData(Complaint complaint)
	{
		detail_content.setText(Html.fromHtml(complaint.getContent()));
		detail_time.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.time_icon), null, null, null);
		detail_time.setText(DateUtil.dateToZh(complaint.getCreateTime()));
		detail_comment_num.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.resp_icon), null, null, null);
		if(complaint!=null&&complaint.getResponses().size()>0)
		{
			respListView.setAdapter(new RespAdapter(this, complaint.getResponses()));
			detail_comment_num.setText(""+complaint.getResponses().size());
		}else{
			reply_lin.setVisibility(View.GONE);
			detail_comment_num.setText("0");
		}
		
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		if (null == msg.obj)
		{
			closeProgressDialog();
			return true;
		}
		switch (msg.what)
		{
		case HttpDefine.GET_COMPLAINT_BY_ID_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj)
			{
				GetComplaintByIdResp resp = JsonUtil.fromJson((String) msg.obj, GetComplaintByIdResp.class);
				if (resp == null)
				{
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS)
				{
					if (null == resp.getComplaint())
					{
						ToastUtil.show(this, "空数据");
					}
					else
					{
						initData(resp.getComplaint());
					}

				}
				else
				{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	private class RespAdapter extends BaseAdapter
	{
		private Context context;
		private List<ComplaintResponse> list;

		public RespAdapter(Context context, List<ComplaintResponse> list)
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

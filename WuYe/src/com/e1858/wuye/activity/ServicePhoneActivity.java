package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.e1858.common.Constant;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.widget.PullToRefreshListView;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.ServicePhoneAdapter;
import com.e1858.wuye.protocol.http.GetServicePhones;
import com.e1858.wuye.protocol.http.GetServicePhonesResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.e1858.wuye.protocol.http.Phone;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 服务电话
 * @author jiajia
 *
 */
public class ServicePhoneActivity extends BaseActivity
{
	private PullToRefreshListView mListView;
	private List<Phone> phones = new ArrayList<Phone>();
	private String object_key;
	private int remark;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_phone);
		remark = getIntent().getExtras().getInt(Constant.PIC_REMARK);
		object_key = ServicePhoneActivity.class.getSimpleName() + "_" + PreferencesUtils.getCommunity().getID() + remark+"_";
		initView();
		loadData(remark, true);
	}

	private void initView()
	{
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		if (remark == 1)
		{
			bar_title.setText(getResources().getString(R.string.text_bar_call_wuye));
		}
		else
		{
			bar_title.setText(getResources().getString(R.string.text_bar_service_phone));
		}
		bar_leftBtn.setVisibility(View.VISIBLE);
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
				if (application.isNetworkAvailable()) {
					loadData(remark,true);
				} else {
					ToastUtil.show(getApplicationContext(), getResources()
							.getString(R.string.network_fail));
				}
            }
        });		
		/*mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO Auto-generated method stub
			}
		});*/
	}

	private void loadData(int remark,boolean localCache)
	{
		if (remark == 1)
		{
			if (!StringUtils.isEmpty(PreferencesUtils.getCommunity().getCommunityInfo().getPhone()))
			{
				Phone phone = new Phone();
				phone.setTitle("物业固话");
				phone.setPhone(PreferencesUtils.getCommunity().getCommunityInfo().getPhone());
				phones.add(phone);
			}
			if (!StringUtils.isEmpty(PreferencesUtils.getCommunity().getCommunityInfo().getMobile()))
			{
				Phone phone = new Phone();
				phone.setTitle("物业手机");
				phone.setPhone(PreferencesUtils.getCommunity().getCommunityInfo().getMobile());
				phones.add(phone);
			}
			mListView.setAdapter(new ServicePhoneAdapter(this, phones));
		}
		else
		{
			if (localCache)
			{
				GetServicePhonesResp resp = (GetServicePhonesResp) DataFileUtils.readObject(object_key);
				if (null != resp)
				{
					phones.addAll(resp.getPhones());
					Message msg = handler.obtainMessage(HttpDefine.RESPONSE_SUCCESS, phones);
					msg.arg1 = 1;
					msg.sendToTarget();
				}
			}
			if (application.isNetworkAvailable())
			{
				// openProgressDialog("加载中...");
				GetServicePhones getServicePhones = new GetServicePhones();
//				getServicePhones.setKey(application.getKey());
//				getServicePhones.setToken(application.getToken());
				getServicePhones.setKey(PreferencesUtils.getLoginKey());
				getServicePhones.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				getServicePhones.setCommunityID(PreferencesUtils.getCommunity().getID());
				NetUtil.post(Constant.BASE_URL, getServicePhones, handler, HttpDefine.GET_SERVICE_PHONES_RESP);
			}
			else
			{
				ToastUtil.show(this, getResources().getString(R.string.network_fail));
			}
		}

	}


	@Override
	public boolean handleMessage(Message msg)
	{
		// TODO Auto-generated method stub
		if (null == msg.obj)
		{
			closeProgressDialog();
			mListView.onRefreshComplete();
			return true;
		}
		switch (msg.what)
		{
		case HttpDefine.RESPONSE_SUCCESS:
			if (msg.arg1 == 1)
			{
				mListView.setAdapter(new ServicePhoneAdapter(this, phones));
			}
			break;
		case HttpDefine.GET_SERVICE_PHONES_RESP:
			// closeProgressDialog();
			if (null != (String) msg.obj)
			{
				GetServicePhonesResp resp = JsonUtil.fromJson((String) msg.obj, GetServicePhonesResp.class);
				if (null == resp)
				{
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS)
				{
					if(resp.getPhones()==null||resp.getPhones().size()==0){
						mListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
						break;
					}
					phones.clear();
					phones = resp.getPhones();
					DataFileUtils.saveObject(resp, object_key);
					mListView.setAdapter(new ServicePhoneAdapter(this, phones));
					mListView.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
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

}

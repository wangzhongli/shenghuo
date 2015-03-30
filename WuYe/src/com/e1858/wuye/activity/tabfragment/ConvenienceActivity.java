package com.e1858.wuye.activity.tabfragment;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e1858.common.Constant;
import com.e1858.fragment.PagerStripFragment;
import com.e1858.network.NetUtil;
import com.e1858.utils.DataFileUtils;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.wuye.MainApplication;
import com.e1858.wuye.R;
import com.e1858.wuye.protocol.http.Community;
import com.e1858.wuye.protocol.http.ConvenientType;
import com.e1858.wuye.protocol.http.GetConvenientTypes;
import com.e1858.wuye.protocol.http.GetConvenientTypesResp;
import com.e1858.wuye.protocol.http.HttpDefine;
 /**
 * 便民
 * @author jiajia 2014年8月22日
 *
 */
public class ConvenienceActivity extends BaseActivity
{
	private String object_key=ConvenienceActivity.class.getSimpleName();
	private PagerStripFragment mPagerStripFragment;
	private final  List<ConvenientType> lists = new ArrayList<ConvenientType>();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.convenience, null);
	}
	
	Community getCommunity()
	{
		return PreferencesUtils.getCommunity();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Community community=getCommunity();
		if(community!=null){
			object_key = ConvenienceActivity.class.getSimpleName()+community.getID();
		}
		loadData(true);
	}

	private void initData(){
		if (getActivity() == null || isDetached()) {
			return;
		}
		if(mPagerStripFragment!=null){
			if (mPagerStripFragment.isAdded()) {
				getChildFragmentManager().beginTransaction().remove(mPagerStripFragment).commitAllowingStateLoss();
				mPagerStripFragment=new PagerStripFragment();
				getChildFragmentManager()
						.beginTransaction()
						.replace(R.id.container, mPagerStripFragment)
						.commitAllowingStateLoss();
			} else {
				getChildFragmentManager().beginTransaction().remove(mPagerStripFragment).commitAllowingStateLoss();
				mPagerStripFragment=new PagerStripFragment();
				getChildFragmentManager()
						.beginTransaction()
						.replace(R.id.container, mPagerStripFragment)
						.commitAllowingStateLoss();
			}
		}else{
			mPagerStripFragment = new PagerStripFragment();
			getChildFragmentManager()
			.beginTransaction()
			.add(R.id.container, mPagerStripFragment)
			.commitAllowingStateLoss();
		}
	}
	private void loadData(boolean isLocalCache){
		if (isLocalCache)
		{
			// 本地缓存
			GetConvenientTypesResp resp = (GetConvenientTypesResp) DataFileUtils.readObject(object_key);
			if (null != resp)
			{
				lists.clear();
				lists.addAll(resp.getTypes());
				Message msg = MainApplication.getInstance().getCurrentHandler().obtainMessage(HttpDefine.RESPONSE_SUCCESS, lists);
				msg.arg1 = 1;
				msg.sendToTarget();
			}
		}
		if (MainApplication.getInstance().isNetworkAvailable())
		{
			// 网络加载
			GetConvenientTypes getConvenientTypes = new GetConvenientTypes();
			getConvenientTypes.setCommunityID(getCommunity().getID());
			getConvenientTypes.setToken(Encrypt.MD5(Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getConvenientTypes, MainApplication.getInstance().getCurrentHandler(), HttpDefine.GET_CONVENIENT_TYPES_RESP);
		}
		else
		{
			ToastUtil.show(getActivity(), getResources().getString(R.string.network_fail));
		}
	}
	
	@Override
	public boolean handleMessage(Message msg)
	{
		if (getActivity() == null || isDetached()) {
			return true;
		}
		// TODO Auto-generated method stub
		if (msg.obj == null)
		{
			return true;
		}
		switch (msg.what)
		{
		case HttpDefine.RESPONSE_SUCCESS:
			if (msg.arg1 == 1)
			{
				application.setConvenientTypes(lists);
				initData();
			}
			break;
		case HttpDefine.GET_CONVENIENT_TYPES_RESP:
			if (null != (String) msg.obj)
			{
				GetConvenientTypesResp resp = JsonUtil.fromJson((String) msg.obj, GetConvenientTypesResp.class);
				if (null == resp)
				{
					break;
				}else{
					if(HttpDefine.RESPONSE_SUCCESS==resp.getRet()){
						lists.clear();
						lists.addAll(resp.getTypes());
						DataFileUtils.saveObject(resp, object_key);
						application.setConvenientTypes(lists);
						initData();
					}else{
						ToastUtil.show(getActivity(), resp.getError());
					}
				}
			}
			msg.obj=null;
			break;
		}
		return false;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(application.getIsConvienceChanged()){
			loadData(false);
			application.setIsConvienceChanged(false);
		}
	}
	
	
}

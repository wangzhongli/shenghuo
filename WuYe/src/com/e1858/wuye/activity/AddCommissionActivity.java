package com.e1858.wuye.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.e1858.common.AddSuccessDialogManager;
import com.e1858.common.Constant;
import com.e1858.common.app.BaseApplication;
import com.e1858.network.NetUtil;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.e1858.utils.PreferencesUtils;
import com.e1858.utils.StringUtils;
import com.e1858.utils.ToastUtil;
import com.e1858.utils.Util;
import com.e1858.wuye.R;
import com.e1858.wuye.adapter.SysAreaAdapter;
import com.e1858.wuye.protocol.http.AddCommission;
import com.e1858.wuye.protocol.http.AddCommissionResp;
import com.e1858.wuye.protocol.http.CommissionType;
import com.e1858.wuye.protocol.http.GetCommissionTypes;
import com.e1858.wuye.protocol.http.GetCommissionTypesResp;
import com.e1858.wuye.protocol.http.HttpDefine;
import com.hg.android.widget.CityDBManager.AreaEntity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * 新增代办事务
 * @author jiajia 2014年8月22日
 *
 */
public class AddCommissionActivity extends BaseActivity implements OnClickListener
{
	private Spinner type_Spinner;
	private EditText mContent;
	private Button okBtn;
	private int typeID=-1;
	
	private TextView house_info;
	private EditText phone_info;
	private EditText name_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_commission);
		initView();
		initTypeSpinner(true);
	}

	private void initView()
	{
		// TODO Auto-generated method stub
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_leftBtn = (Button) findViewById(R.id.bar_left_btn);
		bar_leftBtn.setBackgroundResource(R.drawable.bar_back_background);
		bar_rightBtn = (Button) findViewById(R.id.bar_right_btn);
		type_Spinner = (Spinner) findViewById(R.id.commission_type_spinner);
		mContent = (EditText) findViewById(R.id.input_commission);
		okBtn = (Button) findViewById(R.id.ok_btn);
		bar_title.setText(getResources().getString(R.string.text_bar_add_commission));
		bar_leftBtn.setVisibility(View.VISIBLE);
		okBtn.setOnClickListener(this);
		type_Spinner.setPrompt("选择类型");
		type_Spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				typeID = Integer.parseInt(((AreaEntity) type_Spinner.getSelectedItem()).getCode());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub

			}
		});
		
		house_info = (TextView) findViewById(R.id.house_info);
		phone_info = (EditText) findViewById(R.id.input_phone);
		name_info = (EditText) findViewById(R.id.editText_name);
		name_info.setText(PreferencesUtils.getUserInfo().getName());
		phone_info.setText(PreferencesUtils.getUserInfo().getMobile());
		
		if (null != PreferencesUtils.getUserHouse())
		{
			house_info.setText(Util.getHouseInfo(true));
		}

		house_info.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SwitchResidentAddressActivity.class);
				startActivityForResult(intent, Constant.HOUSE_INFO_RESULT_CODE);
			}
		});
	}

	private void initTypeSpinner(boolean isLocalCache)
	{
		// TODO Auto-generated method stub
		try
		{
			openProgressDialog("请稍候...");
			GetCommissionTypes getCommissionTypes = new GetCommissionTypes();
			getCommissionTypes.setCommunityID(PreferencesUtils.getCommunity().getID());
			getCommissionTypes.setKey(PreferencesUtils.getLoginKey());
			getCommissionTypes.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
			NetUtil.post(Constant.BASE_URL, getCommissionTypes, handler, HttpDefine.GET_COMMISSION_TYPES_RESP);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		case Constant.FAIL_CODE:
			closeProgressDialog();
			if (msg.obj != null) {
				ToastUtil.show(this, msg.obj.toString());
			}
			break;
		case HttpDefine.GET_COMMISSION_TYPES_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj)
			{
				GetCommissionTypesResp resp = JsonUtil.fromJson((String) msg.obj, GetCommissionTypesResp.class);
				if (resp == null)
				{
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS)
				{
					List<AreaEntity> sysAreas = new ArrayList<AreaEntity>();
					List<CommissionType> list = resp.getTypes();
					if (null==resp.getTypes()||resp.getTypes().size()==0)
					{
						AreaEntity sysArea = new AreaEntity();
						sysArea.setCode("-1");
						sysArea.setName("暂无类型");
						sysAreas.add(sysArea);
					}
					else
					{

						for (CommissionType type : list)
						{ 
							AreaEntity sysArea = new AreaEntity();
							sysArea.setCode(type.getID()+ "");
							sysArea.setName(type.getName());
							sysAreas.add(sysArea);
						}
					}
					type_Spinner.setAdapter(new SysAreaAdapter(this, sysAreas));
				}
				else
				{
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		/*case HttpDefine.GET_COMMISSION_TEMPLETES_RESP:
			if (null != (String) msg.obj)
			{
				GetCommissionTemplatesResp resp = JsonUtil.fromJson((String) msg.obj, GetCommissionTemplatesResp.class);
				if (resp == null)
				{
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS)
				{
					List<AreaEntity> sysAreas = new ArrayList<AreaEntity>();
					List<CommissionTemplate> list = resp.getCommissionTemplates();
					if (list.size() == 0)
					{
						AreaEntity sysArea = new AreaEntity();
						sysArea.setCode("-1");
						sysArea.setName("选择模板");
						sysAreas.add(sysArea);
					}
					else
					{

						for (CommissionTemplate template : list)
						{
							AreaEntity sysArea = new AreaEntity();
							sysArea.setCode(template.getTemplate() + "");
							sysArea.setName(template.getContent());
							sysAreas.add(sysArea);
						}
						type_Spinner.setAdapter(new SysAreaAdapter(this, sysAreas));
					}
				}
				else
				{
					ToastUtil.show(this, resp.getErr());
				}
			}
			msg.obj = null;
			break;*/
		case HttpDefine.ADD_COMMISSION_RESP:
			closeProgressDialog();
			if (null != (String) msg.obj)
			{
				AddCommissionResp resp = JsonUtil.fromJson((String) msg.obj, AddCommissionResp.class);
				if (null == resp)
				{
					break;
				}
				if (resp.getRet() == HttpDefine.RESPONSE_SUCCESS)
					
				{
//					ToastUtil.show(this, "新增代办成功!");
					bar_leftBtn.setEnabled(false);
					//延迟一秒跳转
//					new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							AppManager.getAppManager().finishActivity();
//						}
//					}, 1000);
					application.setIsRefresh(true);
					AddSuccessDialogManager.createCallDialog(AddCommissionActivity.this, Constant.ADDSUCCESS_MODULE.COMMISSION,resp.getID(),null);
				}
				else
				{
					okBtn.setEnabled(true);
					ToastUtil.show(this, resp.getError());
				}
			}
			msg.obj = null;
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.ok_btn:
			if (StringUtils.isBlank(house_info.getText().toString()))
			{
				ToastUtil.show(this, "请选择房屋信息");
			}
			if (StringUtils.isBlank(phone_info.getText().toString()))
			{
				ToastUtil.show(this, "请填写联系方式");
			}else if(!Pattern.matches(Constant.MOBILE_REGP, phone_info.getText().toString())){
				ToastUtil.show(this, Constant.ToastMessage.MOBILE_REGP_ERROR);
			}
			else if (typeID==-1)
			{
				ToastUtil.show(this, "代办类型不能为空");
			}
			else if (StringUtils.isBlank(mContent.getText().toString()))
			{
				ToastUtil.show(this, "代办内容不能为空");
			}
			else
			{
				// 发包
				openProgressDialog("新增提交中...");
				okBtn.setEnabled(false);
				AddCommission addCommission = new AddCommission();
				addCommission.setCommunityID(PreferencesUtils.getCommunity().getID());
				addCommission.setPhone(phone_info.getText().toString());
				addCommission.setName(name_info.getText().toString());
//				addCommission.setKey(application.getKey());
//				addCommission.setToken(Encrypt.MD5(application.getKey()+Constant.TokenSalt));
				addCommission.setKey(PreferencesUtils.getLoginKey());
				addCommission.setToken(Encrypt.MD5(PreferencesUtils.getLoginKey()+Constant.TokenSalt));
				addCommission.setTypeID(typeID);
				addCommission.setContent(mContent.getText().toString());
				NetUtil.post(Constant.BASE_URL, addCommission, handler, HttpDefine.ADD_COMMISSION_RESP);
				
			}
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case Constant.HOUSE_INFO_RESULT_CODE:
				house_info.setText(Util.getHouseInfo(true));
			break;
		}
	}
}
